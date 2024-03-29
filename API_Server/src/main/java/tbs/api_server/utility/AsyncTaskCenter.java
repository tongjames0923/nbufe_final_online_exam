package tbs.api_server.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static tbs.api_server.utility.Error.FC_DUPLICATE;
import static tbs.api_server.utility.Error._ERROR;

@Component
public class AsyncTaskCenter {
    @Resource
    RedisTemplate<String, String> str_redis;

    ValueOperations<String, String> ops;

    @Autowired
    void init() {
        ops = str_redis.opsForValue();
    }

    @Async("asyncServiceExecutor")
    public void doWithAsync(AsyncToDo asyncToDo) throws Exception {
        String key = asyncToDo.key();
        Exception ex = null;
        if (ops.get(key) != null)
            throw _ERROR.throwError(FC_DUPLICATE, "重复任务");
        ops.set(key, "doing");
        try {
            asyncToDo.doSomeThing();
        } catch (Exception e) {

            ex = e;
        }

        str_redis.delete(key);
        if (ex != null)
            throw ex;
    }

    @Async("asyncServiceExecutor")
    public <T> Future<T> getWithAsync(AsyncToGet<T> asyncToGet) throws Exception {
        return AsyncResult.forValue(asyncToGet.doSomeThing());

    }


    public interface AsyncToDo {
        String key();

        void doSomeThing();
    }

    public interface AsyncToGet<T> {
        T doSomeThing();
    }



}
