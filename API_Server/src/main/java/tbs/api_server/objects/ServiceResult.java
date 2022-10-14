package tbs.api_server.objects;

public class ServiceResult<T>
{
    private int code;
    T obj;

    public static ServiceResult makeResult(int code,Object obj)
    {
        return new ServiceResult(code, obj);
    }
    public static ServiceResult makeResult(int code)
    {
        return makeResult(code,null);
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    @Override
    public String toString()
    {
        return "ServiceResult{" +
                "code=" + code +
                ", obj=" + obj +
                '}';
    }

    public ServiceResult(int code, T obj)
    {
        this.code = code;
        this.obj = obj;
    }

    public T getObj()
    {
        return obj;
    }

    public void setObj(T obj)
    {
        this.obj = obj;
    }
}
