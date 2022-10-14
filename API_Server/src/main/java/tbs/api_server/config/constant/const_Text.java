package tbs.api_server.config.constant;

public class const_Text
{
public static final String NET_success = "Success",NET_FAILURE = "Failure",NET_UNKNOWN = "Unknown error";
    public static String ERRROR_CODE_TEXT(int error)
    {
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
