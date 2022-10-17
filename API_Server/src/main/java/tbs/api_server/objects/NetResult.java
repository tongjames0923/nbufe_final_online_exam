package tbs.api_server.objects;

public class NetResult <T>{
    private boolean isSuccess;
    private T data;
    private String message;

    public static NetResult makeResult(boolean isSuccess,String msg)
    {
        return new NetResult(isSuccess,null,msg);
    }
    public static NetResult makeResult(boolean isSuccess,String msg,Object data)
    {
        return new NetResult(isSuccess,data,msg);
    }

    public NetResult(boolean isSuccess, T data, String message)
    {
        this.isSuccess = isSuccess;
        this.data = data;
        this.message = message;
    }

    @Override
    public String toString() {
        return "NetResult{" +
                "isSuccess=" + isSuccess +
                ", data=" + data.toString() +
                ", message='" + message + '\'' +
                '}';
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
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
