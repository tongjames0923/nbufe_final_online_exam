package tbs.api_server.publicAPI;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.services.UserService;
import tbs.api_server.utility.Error;
import tbs.api_server.utility.UserUtility;

import static tbs.api_server.utility.Error.*;

@RestController
@RequestMapping("user/*")
public class UserController {

    @Autowired
    UserService service;


    @RequestMapping("updatelevel")
    /***
     * 更新用户权限
     * @param id 修改人 需要EXAM_STAFF
     * @param target 修改目标人的id
     * @param lv 权限等级 0-2
     * @return 成功为修改数量int，否则为空
     */
    @Transactional
    public NetResult updateLevel(int id, int target, int lv) {
        try {
            ServiceResult t = service.updateUserLevel(id, target, lv);
            return NetResult.makeResult(t.getCode(), null, null);
        } catch (Error.BackendError e) {
            _ERROR.rollback();
            return NetResult.makeResult(e.getCode(), e.getMessage());
        } catch (Exception ex) {
            _ERROR.rollback();
            return NetResult.makeResult(EC_UNKNOWN, ex.getMessage());
        }

    }

    @RequestMapping("login")
    /***
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return 成功为用户信息，否则为空或错误消息
     */
    @Transactional
    public NetResult login(String username, String password) {
        try {
            UserDetailInfo info = null;
            if (UserUtility.isValidForUsername(username) && UserUtility.isValidForPassword(password)) {
                password = UserUtility.passwordEncode(password);
                ServiceResult<UserSecurityInfo> sc = service.loginUser(username, password);
                info = (UserDetailInfo) service.getUserInfo(sc.getObj().getId()).getObj();
                sc.getObj().setSec_ans(null);
                return NetResult.makeResult(sc.getCode(), null, info);
            } else {
                return NetResult.makeResult(EC_InvalidParameter, "用户名和密码的强度不足", null);
            }
        } catch (Error.BackendError e) {
            _ERROR.rollback();
            return NetResult.makeResult(e.getCode(), e.getMessage());
        } catch (Exception ex) {
            _ERROR.rollback();
            return NetResult.makeResult(EC_UNKNOWN, ex.getMessage());
        }
    }

    @RequestMapping("updateSecQues")
    /**
     * 更新密保问题
     * @param id 所需更新的用户id
     * @param ques 密保问题
     * @param ans 密保答案
     * @return 成功为个人安全信息，密保答案隐藏，否则为错误信息
     */
    @Transactional
    public NetResult updateSecQues(int id, String ques, String ans) {
        try {
            ServiceResult sc = service.UpdateUserSecQuestion(id, ques, ans);
            ((UserSecurityInfo) sc.getObj()).setSec_ans(null);
            return NetResult.makeResult(sc.getCode(), null, sc.getObj());
        } catch (Error.BackendError e) {
            _ERROR.rollback();
            return NetResult.makeResult(e.getCode(), e.getMessage());
        } catch (Exception ex) {
            _ERROR.rollback();
            return NetResult.makeResult(EC_UNKNOWN, ex.getMessage());
        }

    }


    @RequestMapping("updatedetails")
    /***
     * 更新用户常规信息,长度为0将不进行修改
     * @param id 用户id
     * @param email 电子邮箱
     * @param phone 手机号
     * @param address 地址
     * @param note 简介
     * @return 成功为用户详细信息，否则为空
     */
    @Transactional
    public NetResult updatedetails(int id, String email, String phone, String address, String note) {
        try {
            ServiceResult<UserDetailInfo> dt = service.UpdateUserDetails(id, address.length() > 0 ? address : null,
                    phone.length() > 0 ? phone : null,
                    email.length() > 0 ? email : null,
                    note.length() > 0 ? note : null);
            return NetResult.makeResult(dt.getCode(), null, dt.getObj());
        } catch (Error.BackendError e) {
            _ERROR.rollback();
            return NetResult.makeResult(e.getCode(), e.getMessage());
        } catch (Exception ex) {
            _ERROR.rollback();
            return NetResult.makeResult(EC_UNKNOWN, ex.getMessage());
        }

    }


    @RequestMapping("register")
    /***
     * 注册 密保问题和答案一个为空即视为不设置密保。
     * @param username 用户名
     * @param password 用户密码
     * @param question 密保问题
     * @param answer 密保答案
     * @return 仅在出NET_UNKNOWN错误时候为错误消息，否则为空
     */
    @Transactional
    public NetResult register(String username, String password, @RequestParam(required = false) String question, @RequestParam(required = false) String answer,
                              @RequestParam(required = false) String address, @RequestParam(required = false) String phone, @RequestParam(required = false) String email,
                              @RequestParam(required = false) String note) {
        try {
            if (UserUtility.isValidForUsername(username) && UserUtility.isValidForPassword(password)) {

                if (question != null && answer != null) {
                    if (question.length() == 0)
                        question = null;
                    if (answer.length() == 0)
                        answer = null;
                }
                password = UserUtility.passwordEncode(password);
                ServiceResult<UserSecurityInfo> sc = service.registerUser(username, password, question, answer);
                ServiceResult result = service.UpdateUserDetails(sc.getObj().getId(), address,
                        phone, email, note);

                return NetResult.makeResult(result, null);
            } else {
                return NetResult.makeResult(EC_InvalidParameter, "用户名或密码强度不足");
            }
        } catch (Error.BackendError e) {
            _ERROR.rollback();
            return NetResult.makeResult(e.getCode(), e.getMessage());
        } catch (Exception ex) {
            _ERROR.rollback();
            return NetResult.makeResult(EC_UNKNOWN, ex.getMessage());
        }
    }

    @RequestMapping("updatePassword")
    /***
     * 通过旧密码更新密码
     * @param id 用户id
     * @param password 新密码
     * @param oldpassword ji
     * @return
     */
    @Transactional
    public NetResult updatePassword(int id, String password, String oldpassword) {
        ServiceResult result = null;
        try {
            result = service.UpdateUserPassword(id, UserUtility.passwordEncode(password),
                    UserUtility.passwordEncode(oldpassword));
            return NetResult.makeResult(result, null);
        } catch (Error.BackendError e) {
            _ERROR.rollback();
            return NetResult.makeResult(e.getCode(), e.getMessage());
        } catch (Exception ex) {
            _ERROR.rollback();
            return NetResult.makeResult(EC_UNKNOWN, ex.getMessage());
        }

    }


    @RequestMapping("findpdbyques")
    /***
     * 通过密保问题重置密码
     * @param id 用户id
     * @param password 新密码
     * @param answer 密保问题答案
     * @return 是否成功, data都为空，msg为服务结果代码
     */
    @Transactional
    public NetResult updatePasswordByQues(int id, String password, String answer) {
        ServiceResult result = null;
        try {
            result = service.updateUserPasswordByQuestion(id, UserUtility.passwordEncode(password)
                    , answer);
            return NetResult.makeResult(result, null);
        } catch (Error.BackendError e) {
            _ERROR.rollback();
            return NetResult.makeResult(e.getCode(), e.getMessage());
        } catch (Exception ex) {
            _ERROR.rollback();
            return NetResult.makeResult(EC_UNKNOWN, ex.getMessage());
        }
    }

    @Transactional
    @RequestMapping("pullusers")
    /***
     * 为EXAM_STAFF 用户获取用户列表
     * @param id 拉取人
     * @param from 起始条数
     * @param num 数量
     * @return 成功为用户信息列表，否则为错误信息或空，msg为NET_FAILURE时候data为错误信息
     */
    public NetResult getUserinfoPage(int id, int from, int num) {
        try {
            ServiceResult result = service.pullUserInfo(id, from, num);
            return NetResult.makeResult(result, null);
        } catch (Error.BackendError e) {
            _ERROR.rollback();
            return NetResult.makeResult(e.getCode(), e.getMessage());
        } catch (Exception ex) {
            _ERROR.rollback();
            return NetResult.makeResult(EC_UNKNOWN, ex.getMessage());
        }

    }


}
