package tbs.api_server.publicAPI;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tbs.api_server.config.NoLog;
import tbs.api_server.config.NoNeedAccess;
import tbs.api_server.objects.NetResult;

import static tbs.api_server.utility.Error.SUCCESS;

@RestController
public class OkController {

    @RequestMapping("/")
    @NoNeedAccess
    @NoLog
    public NetResult ok()
    {
        return NetResult.makeResult(SUCCESS,"every thing is fine");
    }
}
