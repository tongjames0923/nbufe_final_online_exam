package tbs.api_server.utils;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tbs.api_server.objects.simple.LogPojo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
@Component
public class BatchUtil<T> {
    List<T> list = new ArrayList<>();

    @Value("${tbs.batchsize}")
    int max;

    @Resource
    MybatisBatchUtils utils;


    public static interface Activitor<T> {
        void flush(MybatisBatchUtils obj,List<T> list);

        default boolean isok()
        {
            return true;
        }
    }

    @Autowired
    void check() throws Exception {
        if (max > MybatisBatchUtils.BATCH_SIZE)
            throw new Exception("批量处理上限容量过大");
    }

    public List<T> getList() {
        return list;
    }

    public void write(T obj, Activitor<T> activitor, boolean f) {
        list.add(obj);
        flush(activitor, f);
    }

    public void flush(Activitor<T> activitor, boolean force) {
        flush(null, activitor, force);
    }

    public void flush(List<T> res, Activitor<T> activitor, boolean force) {
        if (res == null) {
            res = list;
        }
        if ((res.size() >= max || force) && !res.isEmpty()) {
            if (activitor != null) {
                activitor.flush(utils,res);
                if (activitor.isok())
                    list.clear();
            }
        }
    }
}
