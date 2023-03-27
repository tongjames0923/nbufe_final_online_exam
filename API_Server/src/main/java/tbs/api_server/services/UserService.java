package tbs.api_server.services;

import org.springframework.lang.NonNull;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.utility.Error;

public interface UserService
{

    ServiceResult logOut(String access);
    ServiceResult registerUser(@NonNull String username, @NonNull String password, String question, String answer) throws Error.BackendError;


    ServiceResult UpdateUserDetails(int userid,String address,
                              String phone,
                              String email,
                              String note) throws Error.BackendError;

    ServiceResult UpdateUserPassword(int userid,String password,String old) throws Error.BackendError;


    ServiceResult updateUserPasswordByQuestion(String name,String password,String ans) throws Error.BackendError;


    ServiceResult replySecQuestion(String name,String ans) throws Error.BackendError;

    ServiceResult getUserSecQuestion(String name);

    ServiceResult UpdateUserSecQuestion(int userid,String question,String answer) throws Error.BackendError;


    ServiceResult updateUserLevel(int userid,int target,int level) throws Error.BackendError;

   ServiceResult loginUser(@NonNull String username, @NonNull String password) throws Error.BackendError;


   ServiceResult getUserInfo(int userid);

   ServiceResult pullUserInfo(int from,int num) throws Error.BackendError;

   ServiceResult total();

   ServiceResult<UserDetailInfo> findUserByNameLike(String name);

}
