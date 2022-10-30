package tbs.api_server.services;

import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.utility.Error;

public interface UserService
{

    ServiceResult registerUser(@NonNull String username, @NonNull String password, String question, String answer) throws Error.BackendError;


    ServiceResult UpdateUserDetails(int userid,String address,
                              String phone,
                              String email,
                              String note) throws Error.BackendError;

    ServiceResult UpdateUserPassword(int userid,String password,String old) throws Error.BackendError;


    ServiceResult updateUserPasswordByQuestion(int userid,String password,String ans) throws Error.BackendError;



    ServiceResult UpdateUserSecQuestion(int userid,String question,String answer) throws Error.BackendError;


    ServiceResult updateUserLevel(int userid,int target,int level) throws Error.BackendError;

   ServiceResult loginUser(@NonNull String username, @NonNull String password) throws Error.BackendError;


   ServiceResult getUserInfo(int userid);

   ServiceResult pullUserInfo(int id,int from,int num) throws Error.BackendError;

}
