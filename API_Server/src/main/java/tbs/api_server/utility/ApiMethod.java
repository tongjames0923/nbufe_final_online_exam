package tbs.api_server.utility;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tbs.api_server.config.AccessManager;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.simple.UserSecurityInfo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Enumeration;

import static tbs.api_server.utility.Error.EC_UNKNOWN;
import static tbs.api_server.utility.Error._ERROR;

@Component
@Scope("prototype")
public class ApiMethod
{

    public  static interface IAction
    {
        NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception;

        default NetResult CatchBackendError(Error.BackendError error)
        {
            _ERROR.rollback();
            return NetResult.makeResult(error.getCode(),error.getDetail(),error.getData());
        }
        default NetResult CatchException(Exception e)
        {
            _ERROR.rollback();
            return NetResult.makeResult(EC_UNKNOWN,e.getMessage());
        }
    }
    IAction action=null;

    public static ApiMethod make(IAction action)
    {
        ApiMethod method= SpringUtil.getBean(ApiMethod.class);
        method.action=action;
        return method;
    }

    public static NetResult makeResult(IAction action)
    {
        ApiMethod method= SpringUtil.getBean(ApiMethod.class);
        method.action=action;
        return method.method();
    }


    private ApiMethod()
    {

    }

    @Resource
    HttpServletRequest request;

    @Resource
    AccessManager manager;

    public NetResult method()
    {
        try
        {
            return action.action(AccessManager.ACCESS_MANAGER.getLoginedFromHttp());
        }
        catch (Error.BackendError error)
        {
           return action.CatchBackendError(error);
        }catch (Exception e)
        {
            return action.CatchException(e);
        }
    }
}
