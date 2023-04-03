package tbs.logserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tbs.api_server.objects.simple.LogPojo;
import tbs.api_server.utils.MybatisBatchUtils;
import tbs.logserver.backend.mappers.LogMapper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@Component
public class BatchCenter {
    List<LogPojo> list=new ArrayList<>();

    @Value("${tbs.log.batchsize}")
    int max;
    public static BatchCenter Center;

    @Resource
    MybatisBatchUtils utils;

    @Autowired
    public BatchCenter()
    {
        Center=this;
    }
    @Autowired
    void check() throws Exception {
        if(max> MybatisBatchUtils.BATCH_SIZE)
            throw new  Exception("批量处理上限容量过大");
        Center=this;
    }
    public void write(LogPojo logPojo)
    {
        write(logPojo,false);
    }
    public void writeForce(LogPojo pojo)
    {
        write(pojo,true);
    }
    public void flush(boolean force)
    {
        flush(null,force);
    }
    public void flush(List<LogPojo> res,boolean force)
    {
        if(res==null)
        {
            res=list;
        }
        if((res.size()>=max||force)&&!res.isEmpty())
        {
            utils.batchUpdateOrInsert(list, LogMapper.class, new BiFunction<LogPojo, LogMapper, Object>() {
                @Override
                public Object apply(LogPojo logPojo, LogMapper logMapper) {
                    return logMapper.writeLog(logPojo);
                }
            });
            list.clear();
        }
    }
    public void write(LogPojo logPojo,boolean force)
    {
        list.add(logPojo);
        flush(force);
    }
}
