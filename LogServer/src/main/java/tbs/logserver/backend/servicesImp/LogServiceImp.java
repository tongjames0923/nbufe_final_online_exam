package tbs.logserver.backend.servicesImp;

import org.springframework.stereotype.Service;
import tbs.api_server.objects.simple.LogPojo;
import tbs.api_server.objects.simple.LogVo;
import tbs.logserver.backend.mappers.LogMapper;
import tbs.logserver.config.BatchCenter;
import tbs.logserver.services.ILogService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LogServiceImp implements ILogService {
    @Resource
    LogMapper logMapper;
    @Override
    public List<LogPojo> listLogInPage(int from, int num, int fied, String val) {
        BatchCenter.Center.flush(true);
        String[] avaliables={"log_type","log_invoker","log_function"};
        if(fied>=0&&fied<avaliables.length)
        {
          return logMapper.select(from,num,avaliables[fied],val);
        }
        return null;
    }

    @Override
    public List<LogVo> listTopCost(int num) {
        BatchCenter.Center.flush(true);
        return logMapper.listTopCostFunction(num);
    }
}
