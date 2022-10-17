package tbs.api_server.publicAPI;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tbs.api_server.config.constant.const_Text;
import tbs.api_server.config.constant.const_User;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.services.UserService;
import tbs.api_server.utility.UserUtility;

import static tbs.api_server.config.constant.const_User.*;
import static tbs.api_server.utility.Error._ERROR;

@RestController
public class UserController
{

    @Autowired
    UserService service;


    @RequestMapping("user/updatelevel")
    /***
     * 更新用户权限
     * @param id 修改人 需要EXAM_STAFF
     * @param target 修改目标人的id
     * @param lv 权限等级 0-2
     * @return 成功为修改数量int，否则为空
     */
    public NetResult updateLevel(int id, int target, int lv)
    {
        try
        {
            if (lv >= 0 && lv <= 2)
            {
                ServiceResult t = service.updateUserLevel(id, target, lv);
                if (t.getCode() == plUser_SUCCESS)
                    return new NetResult(true, t.getObj(), const_Text.NET_success);
                else
                    return new NetResult(false, null, const_Text.ERRROR_CODE_TEXT(t.getCode()));
            } else
            {
                return new NetResult(false, null, const_Text.ERROR_LEVEL_LIMIT());
            }
        } catch (Exception e)
        {
            _ERROR.rollback();
            return new NetResult(false, e.getMessage(), const_Text.NET_UNKNOWN);
        }

    }

    @RequestMapping("user/login")
    /***
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return 成功为用户信息，否则为空或错误消息
     */
    public NetResult login(String username, String password)
    {
        try
        {
            UserDetailInfo info = null;
            if (UserUtility.isValidForUsername(username) && UserUtility.isValidForPassword(password))
            {
                password = UserUtility.passwordEncode(password);
                ServiceResult<UserSecurityInfo> sc = service.loginUser(username, password);
                if (sc.getCode() == userLogin_Success)
                {
                    info = (UserDetailInfo) service.getUserInfo(sc.getObj().getId()).getObj();
                    sc.getObj().setSec_ans(null);
                    return new NetResult<>(true
                            ,  info, const_Text.NET_success);
                }
                else
                {
                    return new NetResult<>(false, null, const_Text.ERRROR_CODE_TEXT(sc.getCode()));
                }
            }
            else
            {
                return new NetResult<>(false, null, const_Text.ERROR_BAD_USERNAME_OR_PASSWORD(username, password));
            }
        } catch (Exception e)
        {
            return new NetResult<>(false, e.getMessage(), const_Text.NET_UNKNOWN);
        }
    }

    @RequestMapping("/user/updateSecQues")
    /**
     * 更新密保问题
     * @param id 所需更新的用户id
     * @param ques 密保问题
     * @param ans 密保答案
     * @return 成功为个人安全信息，密保答案隐藏，否则为错误信息
     */

    public NetResult updateSecQues(int id, String ques, String ans)
    {
        try
        {
            UserSecurityInfo sc = (UserSecurityInfo) service.UpdateUserSecQuestion(id, ques, ans).getObj();
            sc.setSec_ans(null);
            return new NetResult(true, sc, const_Text.NET_success);
        } catch (Exception e)
        {
            _ERROR.rollback();
            return new NetResult(false, e.getMessage(),const_Text.NET_UNKNOWN);
        }

    }


    @RequestMapping("user/updatedetails")
    /***
     * 更新用户常规信息,长度为0将不进行修改
     * @param id 用户id
     * @param email 电子邮箱
     * @param phone 手机号
     * @param address 地址
     * @param note 简介
     * @return 成功为用户详细信息，否则为空
     */
    public NetResult updatedetails(int id, String email, String phone, String address, String note)
    {
        try
        {
            ServiceResult<UserDetailInfo> dt = service.UpdateUserDetails(id, address.length() > 0 ? address : null,
                                                                         phone.length() > 0 ? phone : null,
                                                                         email.length() > 0 ? email : null,
                                                                         note.length() > 0 ? note : null);
            if (dt.getCode() > 0)
                return new NetResult(true, service.getUserInfo(id).getObj(), const_Text.NET_success);
            else
                return new NetResult(false, null, const_Text.NET_FAILURE);
        }catch (Exception e)
        {
            _ERROR.rollback();
            return NetResult.makeResult(false, e.getMessage(), const_Text.NET_UNKNOWN);
        }

    }


    @RequestMapping("user/register")
    /***
     * 注册 密保问题和答案一个为空即视为不设置密保。
     * @param username 用户名
     * @param password 用户密码
     * @param question 密保问题
     * @param answer 密保答案
     * @return 仅在出NET_UNKNOWN错误时候为错误消息，否则为空
     */
    public NetResult register(String username, String password, String question, String answer)
    {
        try
        {
            if (UserUtility.isValidForUsername(username) && UserUtility.isValidForPassword(password))
            {
                password = UserUtility.passwordEncode(password);
                if(!(question.length()>0&&answer.length()>0))
                {
                    question=null;
                    answer=null;
                }
                ServiceResult<UserSecurityInfo> sc = service.registerUser(username, password, question, answer);
                if (sc.getCode() == userregister_Success)
                {
                    service.UpdateUserDetails(sc.getObj().getId(),null,
                                              null,null,null);

                    return new NetResult<>(true
                            , null, const_Text.NET_success);
                } else
                {
                    return new NetResult<>(false, null, const_Text.ERRROR_CODE_TEXT(sc.getCode()));
                }
            } else
            {
                return new NetResult<>(false, null, const_Text.ERROR_BAD_USERNAME_OR_PASSWORD(username, password));
            }
        } catch (Exception e)
        {
            //执行回滚
            _ERROR.rollback();
            return new NetResult<>(false, e.getMessage(), const_Text.NET_UNKNOWN);
        }
    }

    @RequestMapping("user/updatePassword")
    /***
     * 通过旧密码更新密码
     * @param id 用户id
     * @param password 新密码
     * @param oldpassword ji
     * @return
     */
    public NetResult updatePassword(int id, String password, String oldpassword)
    {
        ServiceResult result = null;
        try
        {
            result = service.UpdateUserPassword(id, UserUtility.passwordEncode(password),
                                                UserUtility.passwordEncode(oldpassword));
            if(result.getCode()== userpdchange_Success)
                return NetResult.makeResult(true,const_Text.NET_success);
            else
                return NetResult.makeResult(false,const_Text.ERRROR_CODE_TEXT(result.getCode()));
        } catch (Exception e)
        {
            _ERROR.rollback();
            e.printStackTrace();
            return NetResult.makeResult(false,const_Text.NET_UNKNOWN);
        }

    }


    @RequestMapping("user/findpdbyques")
    /***
     * 通过密保问题重置密码
     * @param id 用户id
     * @param password 新密码
     * @param answer 密保问题答案
     * @return 是否成功,data都为空，msg为服务结果代码
     */
    public NetResult updatePasswordByQues(int id, String password, String answer)
    {
        ServiceResult result = null;
        try
        {
            result = service.updateUserPasswordByQuestion(id, UserUtility.passwordEncode(password)
                    , answer);
            if(result.getCode()== userpdchange_Success)
                return NetResult.makeResult(true,const_Text.NET_success);
            else
                return NetResult.makeResult(false,const_Text.ERRROR_CODE_TEXT(result.getCode()));
        } catch (Exception e)
        {
            _ERROR.rollback();
            e.printStackTrace();
            return NetResult.makeResult(false,const_Text.NET_UNKNOWN);
        }
    }

    @RequestMapping("/user/pullusers")
    /***
     * 为EXAM_STAFF 用户获取用户列表
     * @param id 拉取人
     * @param from 起始条数
     * @param num 数量
     * @return 成功为用户信息列表，否则为错误信息或空，msg为NET_FAILURE时候data为错误信息
     */
    public NetResult getUserinfoPage(int id, int from, int num)
    {
        try
        {
            ServiceResult result = service.pullUserInfo(id, from, num);
            if (result.getCode() == plUser_SUCCESS)
            {
                return new NetResult(true, result.getObj(), const_Text.NET_success);
            } else
            {
                return new NetResult(false, null, const_Text.ERRROR_CODE_TEXT(result.getCode()));
            }
        } catch (Exception e)
        {
            return new NetResult(false, e.getMessage(), const_Text.NET_FAILURE);
        }

    }


}
