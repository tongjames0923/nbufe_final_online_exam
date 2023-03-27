package tbs.api_server.publicAPI;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tbs.api_server.config.AccessLimit;
import tbs.api_server.config.NoLog;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.utility.ApiMethod;
import tbs.api_server.utility.Error;
import tbs.logserver.services.ILogService;

import javax.annotation.Resource;

import static tbs.api_server.utility.Error.SUCCESS;

@RestController
@RequestMapping("log/*")
public class LogController {


    @RequestMapping("get")
    @NoLog
    @AccessLimit
    public NetResult log(int field, String val) {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                ILogService service= SpringUtil.getBean(ILogService.class);
                return new NetResult(SUCCESS, service.listLogInPage(0, 200, field, val), null);
            }
        });
    }
}
