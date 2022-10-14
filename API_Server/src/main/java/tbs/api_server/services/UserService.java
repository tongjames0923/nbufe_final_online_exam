package tbs.api_server.services;

import org.springframework.lang.NonNull;
import tbs.api_server.objects.ServiceResult;
public interface UserService
{
    ServiceResult registerUser(@NonNull String username, @NonNull String password, String question, String answer);

    ServiceResult UpdateUserDetails(int userid,String address,
                              String phone,
                              String email,
                              String note);
    ServiceResult UpdateUserPassword(int userid,String password,String old);

    ServiceResult updateUserPasswordByQuestion(int userid,String password,String ans);


    ServiceResult UpdateUserSecQuestion(int userid,String question,String answer);

    ServiceResult updateUserLevel(int userid,int target,int level);


   ServiceResult loginUser(@NonNull String username, @NonNull String password);


   ServiceResult getUserInfo(int userid);

   ServiceResult pullUserInfo(int id,int from,int num);

}
