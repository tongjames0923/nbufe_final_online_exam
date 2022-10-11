package tbs.api_server.objects;

public class NetResult <T>{
    private boolean isSuccess;
    private T data;
    private String message;

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
