package tbs.api_server.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static tbs.api_server.utility.Error.FC_DUPLICATE;
import static tbs.api_server.utility.Error._ERROR;

@Component
public class AsyncTaskCenter {
    @Resource
    RedisTemplate<String, String> str_redis;

    ValueOperations<String,String> ops;

    @Autowired
    void init()
    {
        ops= str_redis.opsForValue();
    }
    public static interface AsyncToDo
    {
        String key();
        void doSomeThing();
    }
    @Async
    public void doWithAsync(AsyncToDo asyncToDo) throws Error.BackendError {
        String key=asyncToDo.key();
        if(ops.get(key)!=null)
            throw _ERROR.throwError(FC_DUPLICATE,"重复任务");
        asyncToDo.doSomeThing();
        str_redis.delete(key);
    }
}
