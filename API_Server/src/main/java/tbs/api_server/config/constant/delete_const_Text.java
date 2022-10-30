package tbs.api_server.config.constant;

import java.util.HashMap;

public class delete_const_Text
{
public static final String NET_INSERT_FAIL="can not insert this",NET_NOT_FIND="can not find anythings",NET_UPDATE_FIAL="can not update this field",NET_FIND_QUES_ERROR="not a good type for find question",NET_success = "Success",NET_FAILURE = "Failure",NET_UNKNOWN = "Unknown error",NET_NO_WRITE="File Not Write in System";

   private static int[] ck={102,103,112,105,106,108,109,110};
 private static    String[] text={"登录失败，用户不存在","登录失败,密码错误","权限不足","注册失败,用户名已存在",
            "注册失败,未知错误","用户密码修改失败，原密码错误","用户密码设置失败,未设置密保问题","用户密码修改失败,密保答案错误"};
private static HashMap<Integer,String> errorTEXT;

    public static String ERROR_BAND_COLUMN_NAME(String name)
    {
        return String.format("you set a invalid column name :%s",name);
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
