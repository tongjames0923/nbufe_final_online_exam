package tbs.api_server.objects;

public class NetResult <T>{
    private int code;
    private T data;
    private String message;

    @Override
    public String toString() {
        return "NetResult{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }

    public static NetResult makeResult(int code, String msg)
    {
        return new NetResult(code,null,msg);
    }
    public static NetResult makeResult(int code,String msg,Object data)
    {
        return new NetResult(code,data,msg);
    }

    public static NetResult makeResult(ServiceResult result,String msg)
    {
        return new NetResult(result.getCode(),result.getObj(),msg);
    }
    public NetResult(int code, T data, String message)
    {
        this.code= code;
        this.data = data;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
