package tbs.api_server.services;

import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tbs.api_server.objects.ServiceResult;
public interface UserService
{
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult registerUser(@NonNull String username, @NonNull String password, String question, String answer);

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult UpdateUserDetails(int userid,String address,
                              String phone,
                              String email,
                              String note);
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult UpdateUserPassword(int userid,String password,String old);

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult updateUserPasswordByQuestion(int userid,String password,String ans);


    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult UpdateUserSecQuestion(int userid,String question,String answer);

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult updateUserLevel(int userid,int target,int level);

   ServiceResult loginUser(@NonNull String username, @NonNull String password);


   ServiceResult getUserInfo(int userid);

   ServiceResult pullUserInfo(int id,int from,int num);

}
