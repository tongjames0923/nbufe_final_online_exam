package tbs.api_server.backend.utility;

public final class Error
{
    public static final Error INSTANCE = new Error();
    public static final int EC_UNKNOWN = 40001;
    private int[] errorCode = {EC_UNKNOWN};
    private String[] error_msg = {"未知错误，请联系管理员"};

}
