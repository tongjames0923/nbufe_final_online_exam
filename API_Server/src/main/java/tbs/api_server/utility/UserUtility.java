package tbs.api_server.utility;


import static tbs.api_server.config.constant.const_User.min_password_length;
import static tbs.api_server.config.constant.const_User.min_username_length;
import static tbs.api_server.utility.Error._ERROR;

public final class UserUtility {
    public static String passwordEncode(String password) throws Exception {
        return SecurityTools.Encrypt_str(password).substring(64, 64 + 32);
    }

    public static boolean isValidForUsername(String username) {
        return Error.lengthCheck(username, min_username_length);
    }

    public static boolean isValidForPassword(String username) {
        return Error.lengthCheck(username, min_password_length);
    }
}
