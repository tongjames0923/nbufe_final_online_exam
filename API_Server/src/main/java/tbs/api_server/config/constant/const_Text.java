package tbs.api_server.config.constant;

import java.util.HashMap;

public class const_Text
{
public static final String NET_success = "Success",NET_FAILURE = "Failure",NET_UNKNOWN = "Unknown error";

//    public static final int userLogin_Success = 100,userLogin_NotFound=102,userLogin_WrongPassword=103;
//    public static final int plUser_NO_RIGHTS=112,plUser_SUCCESS=113;
//    public static final int userregister_Success = 104,userregister_Duplicate=105,userregister_UnexceptError=106;
//    public static final int userpdchange_Success = 107,userpdchange_WrongPassword=108,userpdchange_NotFound=109,
//            userpdchange_WrongAnswer=110
private static HashMap<Integer,String> errorTEXT;
    public static String ERRROR_CODE_TEXT(int error)
    {
        if(errorTEXT==null)
        {
            errorTEXT=new HashMap<>();
            int[] ck={102,103,112,105,106,108,109,110};
            String[] text={"登录失败，用户不存在","登录失败,密码错误","提取用户失败,您没有权限","注册失败,用户名已存在",
            "注册失败,未知错误","用户密码修改失败，原密码错误","用户密码设置失败,未设置密保问题","用户密码修改失败,密保答案错误"};
            int j=0;
            for(int i:ck)
            {
                errorTEXT.put(i,text[j++]);
            }
        }
        if(errorTEXT.containsKey(error))
        {
            return errorTEXT.get(error);
        }
        return String.format("error code: %d", error);
    }
    public static String ERROR_BAD_USERNAME_OR_PASSWORD(String u,String p)
    {
        return String.format("your username length is %d characters,password length is %d  " +
                                     "your username length must be longer than %d characters and password length must be" +
                                     "longer than %d characters"
                , u.length(), p.length(),const_User.min_username_length,const_User.min_password_length);
    }
    public static String ERROR_LEVEL_LIMIT()
    {
        return String.format("level only available for 0-2");
    }
}
