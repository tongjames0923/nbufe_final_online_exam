package tbs.api_server.publicAPI;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tbs.api_server.ai.AI_Client;
import tbs.api_server.config.NoLog;
import tbs.api_server.config.NoNeedAccess;
import tbs.api_server.objects.NetResult;

import javax.annotation.Resource;

import static tbs.api_server.utility.Error.SUCCESS;

@RestController
public class OkController {

    @Resource
    AI_Client aiClient;

    @RequestMapping("/")
    @NoNeedAccess
    @NoLog
    public NetResult ok(@RequestParam(required = false,defaultValue = "光走一年叫光年，狗走一天叫什么？") String text)
    {
        return NetResult.makeResult(SUCCESS,"every thing is fine",
                aiClient.request(text));
    }
}
