package tbs.api_server.utility;

import tbs.api_server.objects.NetResult;

import static tbs.api_server.utility.Error.EC_UNKNOWN;
import static tbs.api_server.utility.Error._ERROR;

public class ApiMethod
{
   public  static interface IAction
    {
        NetResult action() throws Error.BackendError, Exception;

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
        ApiMethod method=new ApiMethod();
        method.action=action;
        return method;
    }
    private ApiMethod()
    {

    }
    public NetResult method()
    {
        try
        {
            return action.action();
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
