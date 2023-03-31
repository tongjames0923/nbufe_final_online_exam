package tbs.api_server.publicAPI;

import com.alibaba.fastjson.JSON;
import io.netty.util.internal.StringUtil;
import org.springframework.web.bind.annotation.*;
import tbs.api_server.ai.AI_Client;
import tbs.api_server.ai.model.FillBlank_AI_Model;
import tbs.api_server.ai.model.Select_AI_Model;
import tbs.api_server.config.AccessLimit;
import tbs.api_server.config.NoNeedAccess;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.utility.ApiMethod;
import tbs.api_server.utility.Error;

import javax.annotation.Resource;

import java.util.List;

import static tbs.api_server.utility.Error.FC_UNAVALIABLE;
import static tbs.api_server.utility.Error.SUCCESS;

@RestController
@RequestMapping("ai/*")
public class AiController {
    @Resource
    AI_Client aiClient;

    @RequestMapping(value = "ask", method = RequestMethod.POST)
    @AccessLimit
    public NetResult ask(String text) {
        return ApiMethod.makeResultNoLogin(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                return NetResult.makeResult(SUCCESS, null, aiClient.request(text));
            }
        });
    }

    @RequestMapping(value = "makeFillBlankQ", method = RequestMethod.POST)
    public NetResult makeFillBlankQuestion(@RequestBody String text) {
        return ApiMethod.makeResultNoLogin(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                if (StringUtil.isNullOrEmpty(text))
                    return NetResult.makeResult(FC_UNAVALIABLE, "请输入您想出的有关知识点");
                String ask = String.format("你现在是一个json生成器。生成一道填空题。大致要求如下:%s。以类似以下的格式输出，不要有多余的废话。{text:'题目___',answer:'答案'}", text);
                String obj = aiClient.request(ask);
                return parase(obj, FillBlank_AI_Model.class);
            }
        });
    }

    static <T> NetResult parase(String obj, Class<? extends T> cs) {
        T selectAiModel = JSON.parseObject(obj, cs);
        return NetResult.makeResult(SUCCESS, null, selectAiModel);
    }

    @RequestMapping(value = "makeSelectQ", method = RequestMethod.POST)
    public NetResult makeSelectQuestion(@RequestBody String text) {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                if (StringUtil.isNullOrEmpty(text))
                    return NetResult.makeResult(FC_UNAVALIABLE, "请输入您想出的有关知识点");
                String ask = String.format("你现在是一个json生成器，不要有多余的废话。生成一道选择题。大致要求如下:%s。以类似以下的格式输出。{text:'题目',options:[{answer:'答案A',right:false},{answer:'答案B',right:true}]}",
                        text
                );
                String obj = aiClient.request(ask);
                return parase(obj,Select_AI_Model.class);
            }
        });
    }
}
