package tbs.api_server.backend.utility;

import java.util.HashMap;

public final  class Error
{
    public static final Error _ERROR = new Error();
    public static final int EC_UNKNOWN = 40001,EC_InvalidParameter=40002;
    private int[] errorCode = {EC_UNKNOWN,EC_InvalidParameter};
    private String[] error_msg = {"未知错误，请联系开发人员","非法参数,请检查传参"};
    private HashMap<Integer,String> map=new HashMap<>();

    private Error()
    {
        for(int i=0; i<errorCode.length;++i)
            map.put(errorCode[i],error_msg[i]);
    }
    public void throwError(int error,String detail) throws RuntimeException
    {
        if(map.containsKey(error))
        {
            throw new RuntimeException(String.format("error code: %d,msg=%s,Detail:%s",error,map.get(error),detail));
        }
        else
            throw new RuntimeException("bad error code for "+error);
    }



}
