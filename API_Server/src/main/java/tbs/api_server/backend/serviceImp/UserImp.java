package tbs.api_server.backend.serviceImp;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import tbs.api_server.backend.mappers.UserMapper;
import tbs.api_server.backend.utility.Error;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.services.UserService;

import static tbs.api_server.config.const_User.*;
import static tbs.api_server.backend.utility.Error.*;

@Service
public class UserImp implements UserService
{
    @Override
    public ServiceResult<UserSecurityInfo> UpdateUserPassword(int userid, String password)
    {
        int a= mp.setValueForUserSecurity(userid, usec_password,password);
        return new ServiceResult<>(a,mp.getUserSecurityInfo(userid));
    }

    @Override
    public ServiceResult<UserSecurityInfo> UpdateUserSecQuestion(int userid, String question, String answer)
    {
        if(question == null||answer==null)
        {
            _ERROR.throwError(EC_InvalidParameter,"question and answer must not be null");
        }
        int a=mp.setValueForUserSecurity(userid,usec_ques,question)+mp.setValueForUserSecurity(userid,
                                                                                             usec_ans,answer);
        return new ServiceResult<>(a, mp.getUserSecurityInfo(userid));
    }

    @Override
    public ServiceResult<UserDetailInfo> getUserInfo(int userid)
    {
        return new ServiceResult<>(1,mp.getUserDetailInfoByID(userid));
    }

    @Autowired
    UserMapper mp;

    @Override
    public ServiceResult<UserSecurityInfo> registerUser(String username, String password, String question,
                                                        String answer)
    {
        try
        {
            int c = mp.insertSecurityInfo(username, password, question, answer);
            if (c == 1)
                return new ServiceResult<>(userregister_Success, null);
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
    public ServiceResult<UserDetailInfo> UpdateUserDetails(int userid, String address, String phone, String email,
                                                           String note)
    {
        int a = 0;
        try
        {
            if (address != null)
            {
                a = mp.setValueForUserDetails(userid, uinfo_address, address);
            }
            if (phone != null)
                a += mp.setValueForUserDetails(userid, uinfo_phone, phone) * 10;
            if (email != null)
                a += mp.setValueForUserDetails(userid, uinfo_email, email) * 100;
            if (note != null)
                a += mp.setValueForUserDetails(userid, uinfo_note, note) * 1000;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ServiceResult<>(a, mp.getUserDetailInfoByID(userid));
    }

    @Override
    public ServiceResult<UserSecurityInfo> loginUser(String username, String password)
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
