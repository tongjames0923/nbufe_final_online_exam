package tbs.api_server.publicAPI;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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


    @Resource
    ILogService service;
    @RequestMapping("top")
    @NoLog
    @AccessLimit
    public NetResult top(@RequestParam(required = false,defaultValue = "3")Integer num)
    {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                return NetResult.makeResult(SUCCESS,null,service.listTopCost(num));
            }
        });
    }

    @RequestMapping("get")
    @NoLog
    @AccessLimit
    public NetResult log(int field, String val,@RequestParam(required = false,defaultValue = "200") Integer maxpage) {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {

                return new NetResult(SUCCESS, service.listLogInPage(0, maxpage, field, val), null);
            }
        });
    }
}
