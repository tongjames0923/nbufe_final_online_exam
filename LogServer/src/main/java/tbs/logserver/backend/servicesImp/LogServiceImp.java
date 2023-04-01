package tbs.logserver.backend.servicesImp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tbs.api_server.objects.simple.LogPojo;
import tbs.api_server.objects.simple.LogVo;
import tbs.logserver.backend.Specifications.LogpageSpec;
import tbs.logserver.backend.mappers.LogMapper;
import tbs.logserver.services.ILogService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogServiceImp implements ILogService {
    @Resource
    LogMapper logMapper;
    @Override
    public List<LogPojo> listLogInPage(int from, int num, int fied, String val) {
        String[] avaliables={"log_type","log_invoker","log_function"};
        if(fied>=0&&fied<avaliables.length)
        {
            LogpageSpec spec=new LogpageSpec(avaliables[fied],val);
            Page<LogPojo> result= logMapper.findAllWithoutTotal(spec,PageRequest.of(from,num));
            return  result.getContent();
        }
        return null;
    }

    @Override
    public List<LogVo> listTopCost(int num) {
        return logMapper.listTopCostFunction(num);
    }
}
