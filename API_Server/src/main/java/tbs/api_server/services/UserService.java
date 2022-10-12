package tbs.api_server.services;

import javafx.util.Pair;
import org.springframework.context.annotation.Import;
import org.springframework.lang.NonNull;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.objects.simple.UserSecurityInfo;
public interface UserService
{
    ServiceResult<UserSecurityInfo> registerUser(@NonNull String username, @NonNull String password, String question, String answer);

    ServiceResult<UserDetailInfo> UpdateUserDetails(int userid,String address,
                              String phone,
                              String email,
                              String note);
    ServiceResult<UserSecurityInfo> UpdateUserPassword(int userid,String password);

    ServiceResult<UserSecurityInfo> UpdateUserSecQuestion(int userid,String question,String answer);


   ServiceResult<UserSecurityInfo> loginUser(@NonNull String username, @NonNull String password);


   ServiceResult<UserDetailInfo> getUserInfo(int userid);

}
