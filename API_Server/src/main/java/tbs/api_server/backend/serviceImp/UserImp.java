package tbs.api_server.backend.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import tbs.api_server.backend.mappers.UserMapper;
import tbs.api_server.config.constant.const_User;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.services.UserService;
import tbs.api_server.utility.MapperStore;

import static tbs.api_server.config.constant.const_User.*;
import static tbs.api_server.utility.Error.*;

@Service
public class UserImp implements UserService
{
    @Override
    public ServiceResult updateUserPasswordByQuestion(int userid, String password, String ans) throws BackendError {
        UserSecurityInfo securityInfo = mp.getUserSecurityInfo(userid);
        if (securityInfo != null)
        {
            if(securityInfo.getSec_ques()!=null&&securityInfo.getSec_ans()!=null)
            {
                if (securityInfo.getSec_ans().equals(ans))
                {
                    return new ServiceResult(mp.setValueForUserSecurity(userid, usec_password, password), null);
                } else
                {
                  throw  _ERROR.throwError(FC_WRONG_PASSTEXT,"验证答案错误");
                }
            }
            else
            {
                throw  _ERROR.throwError(FC_UNAVALIABLE,"不存在密保问题");

            }

        } else
        {
            throw _ERROR.throwError(FC_NOTFOUND,"用户不存在");
            //return new ServiceResult(userpdchange_NotFound, null);
        }
    }

    @Override
    public ServiceResult UpdateUserPassword(int userid, String password, String old) throws BackendError {
        UserSecurityInfo securityInfo = mp.getUserSecurityInfo(userid);
        if (securityInfo != null)
        {
            if (securityInfo.getPassword().equals(old))
            {
                return new ServiceResult(mp.setValueForUserSecurity(userid, usec_password, password), null);
            } else
            {
                throw _ERROR.throwError(FC_WRONG_PASSTEXT,"原密码错误");
                //return new ServiceResult(userpdchange_WrongPassword, null);
            }
        } else
        {
            throw _ERROR.throwError(FC_NOTFOUND,"用户不存在");
        }
    }

    @Override
    public ServiceResult UpdateUserSecQuestion(int userid, String question, String answer) throws BackendError {
        if (question == null || answer == null)
        {
           throw   _ERROR.throwError(EC_InvalidParameter, "必须包含密保问题和答案");
        }
        int a = mp.setValueForUserSecurity(userid, usec_ques, question) + mp.setValueForUserSecurity(userid,
                                                                                                     usec_ans, answer);
        return new ServiceResult<>(a, mp.getUserSecurityInfo(userid));
    }

    @Override
    public ServiceResult getUserInfo(int userid)
    {
        return new ServiceResult<>(1, mp.getUserDetailInfoByID(userid));
    }

    @Override
    public ServiceResult pullUserInfo(int id, int from, int num) throws BackendError {
        int lv= mp.getUserDetailInfoByID(id).getLevel();
        if(lv== LEVEL_EXAM_STAFF)
        {
           return ServiceResult.makeResult(SUCCESS, mp.getUserDetailInfos(from, num));
        }
        else
        {
            throw  _ERROR.throwError(EC_LOW_PERMISSIONS,String.format("LEVEL {%d} 权限无法拉取用户信息",lv));

        }
    }

    @Autowired
    UserMapper mp;

    @Override
    public ServiceResult registerUser(String username, String password, String question,
                                      String answer) throws BackendError {
        try
        {
            int c = mp.insertSecurityInfo(username, password, question, answer);
            if (c == 1)
                return new ServiceResult<>(SUCCESS, mp.getUserSecurityInfoByName(username));
            else
            {
                throw _ERROR.throwError(EC_DB_INSERT_FAIL,"插入数据失败");
            }
        } catch (Exception e)
        {
            if (e.getClass().equals(DuplicateKeyException.class))
            {
                throw _ERROR.throwError(FC_DUPLICATE,"用户名重复");
            } else
              throw   _ERROR.throwError(EC_UNKNOWN, e.getMessage());
        }
    }

    @Override
    public ServiceResult UpdateUserDetails(int userid, String address, String phone, String email,
                                           String note) throws BackendError {
        try {
            int a = 0;
            if (mp.getUserDetailInfoByID(userid) == null)
            {
                a += mp.insertUserDetails(userid, address, phone, email, note);
            } else
            {
                if (address != null)
                    a = mp.setValueForUserDetails(userid, uinfo_address, address) * 10;
                if (phone != null)
                    a += mp.setValueForUserDetails(userid, uinfo_phone, phone) * 10;
                if (email != null)
                    a += mp.setValueForUserDetails(userid, uinfo_email, email) * 10;
                if (note != null)
                    a += mp.setValueForUserDetails(userid, uinfo_note, note) * 10;
            }
            return new ServiceResult<>(a, mp.getUserDetailInfoByID(userid));
        }catch (Exception ex)
        {
            throw _ERROR.throwError(EC_UNKNOWN,ex.getMessage(),ex);
        }

    }

    @Override
    public ServiceResult updateUserLevel(int userid,int target, int level) throws BackendError {

        if(level<0||level>2)
            throw _ERROR.throwError(EC_InvalidParameter,"权限有效值0-2");
        int lv= mp.getUserDetailInfoByID(userid).getLevel();

        if(lv== LEVEL_EXAM_STAFF)
        {
            int k = mp.setValueForUserDetails(target, uinfo_level, level);
            return ServiceResult.makeResult(SUCCESS,k);
        }
        else
        {
            throw  _ERROR.throwError(EC_LOW_PERMISSIONS,String.format("LEVEL {%d} 权限无法更新用户权限",lv));
        }
    }

    @Override
    public ServiceResult loginUser(String username, String password) throws BackendError {
        UserSecurityInfo des = null;
        try
        {
            des = mp.getUserSecurityInfoByName(username);
            if (des == null)
                throw _ERROR.throwError(FC_NOTFOUND,"用户名不存在");
            else
            {
                if (des.getPassword().equals(password))
                {
                    return new ServiceResult<>(SUCCESS, des);
                } else
                    throw _ERROR.throwError(FC_WRONG_PASSTEXT,"密码错误");
            }
        } catch (Exception e)
        {
            _ERROR.throwError(EC_UNKNOWN,e.getMessage(),e);
        }
        return null;
    }
}
