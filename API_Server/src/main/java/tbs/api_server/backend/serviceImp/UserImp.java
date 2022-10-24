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
    public ServiceResult updateUserPasswordByQuestion(int userid, String password, String ans)
    {
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
                    return new ServiceResult(userpdchange_WrongAnswer, null);
                }
            }
            else
            {
                return new ServiceResult(userpdchange_noneQues,null);
            }

        } else
        {
            return new ServiceResult(userpdchange_NotFound, null);
        }
    }

    @Override
    public ServiceResult UpdateUserPassword(int userid, String password, String old)
    {
        UserSecurityInfo securityInfo = mp.getUserSecurityInfo(userid);
        if (securityInfo != null)
        {
            if (securityInfo.getPassword().equals(old))
            {
                return new ServiceResult(mp.setValueForUserSecurity(userid, usec_password, password), null);
            } else
            {
                return new ServiceResult(userpdchange_WrongPassword, null);
            }
        } else
        {
            return new ServiceResult(userpdchange_NotFound, null);
        }
    }

    @Override
    public ServiceResult UpdateUserSecQuestion(int userid, String question, String answer)
    {
        if (question == null || answer == null)
        {
            _ERROR.throwError(EC_InvalidParameter, "question and answer must not be null");
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
    public ServiceResult pullUserInfo(int id, int from, int num)
    {
        int lv= mp.getUserDetailInfoByID(id).getLevel();
        if(lv== LEVEL_EXAM_STAFF)
        {
           return ServiceResult.makeResult(plUser_SUCCESS, mp.getUserDetailInfos(from, num));
        }
        else
        {
         return ServiceResult.makeResult(plUser_NO_RIGHTS,null);
        }
    }

    @Autowired
    UserMapper mp;

    @Override
    public ServiceResult registerUser(String username, String password, String question,
                                      String answer)
    {
        try
        {
            int c = mp.insertSecurityInfo(username, password, question, answer);
            if (c == 1)
                return new ServiceResult<>(userregister_Success, mp.getUserSecurityInfoByName(username));
//                            return new ServiceResult<>(userregister_Success, null);
        } catch (Exception e)
        {
            if (e.getClass().equals(DuplicateKeyException.class))
            {
                return new ServiceResult<>(userregister_Duplicate, null);
            } else
                _ERROR.throwError(EC_UNKNOWN, e.getMessage());
        }
        return new ServiceResult<>(userregister_UnexceptError, null);
    }

    @Override
    public ServiceResult UpdateUserDetails(int userid, String address, String phone, String email,
                                           String note)
    {
        int a = 0;
        if (mp.getUserDetailInfoByID(userid) == null)
        {
            a += mp.insertUserDetails(userid, address, phone, email, note);
        } else
        {
            try
            {
                if (address != null)
                    a = mp.setValueForUserDetails(userid, uinfo_address, address) * 10;
                if (phone != null)
                    a += mp.setValueForUserDetails(userid, uinfo_phone, phone) * 10;
                if (email != null)
                    a += mp.setValueForUserDetails(userid, uinfo_email, email) * 10;
                if (note != null)
                    a += mp.setValueForUserDetails(userid, uinfo_note, note) * 10;
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        return new ServiceResult<>(a, mp.getUserDetailInfoByID(userid));
    }

    @Override
    public ServiceResult updateUserLevel(int userid,int target, int level)
    {
        int lv= mp.getUserDetailInfoByID(userid).getLevel();
        if(lv== LEVEL_EXAM_STAFF)
        {
            int k = mp.setValueForUserDetails(target, uinfo_level, level);
            return ServiceResult.makeResult(plUser_SUCCESS,k);
        }
        else
        {
            return ServiceResult.makeResult(plUser_NO_RIGHTS,null);
        }
    }

    @Override
    public ServiceResult loginUser(String username, String password)
    {
        UserSecurityInfo des = null;
        try
        {
            des = mp.getUserSecurityInfoByName(username);
            if (des == null)
                return new ServiceResult<>(userLogin_NotFound, des);
            else
            {
                if (des.getPassword().equals(password))
                {
                    return new ServiceResult<>(userLogin_Success, des);
                } else
                    return new ServiceResult<>(userLogin_WrongPassword, des);
            }
        } catch (Exception e)
        {
            _ERROR.throwError(EC_UNKNOWN, e.getMessage());
        }
        return null;
    }
}
