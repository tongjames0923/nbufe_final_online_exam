package tbs.api_server.publicAPI;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tbs.api_server.config.AccessLimit;
import tbs.api_server.config.AccessManager;
import tbs.api_server.config.NoLog;
import tbs.api_server.config.NoNeedAccess;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.compound.exam.UserVo;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.services.UserService;
import tbs.api_server.utility.ApiMethod;
import tbs.api_server.utility.Error;
import tbs.api_server.utility.UserUtility;

import static tbs.api_server.utility.Error.*;

@RestController
@RequestMapping("user/*")
public class UserController {

    @Autowired
    UserService service;


    @RequestMapping("renew")
    @NoLog
    @NoNeedAccess
    public NetResult renewToken() {
        return ApiMethod.makeResult(new ApiMethod.IAction() {

            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                AccessManager.ACCESS_MANAGER.renew();
                return NetResult.makeResult(SUCCESS, null);
            }
        });
    }


    @RequestMapping("logout")
    @NoNeedAccess
    @NoLog
    public NetResult logOut(String access) {
        return ApiMethod.makeResultNoLogin(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                return NetResult.makeResult(service.logOut(access), null);
            }
        });
    }


    @RequestMapping("updatelevel")
    /***
     * 更新用户权限
     * @param id 修改人 需要EXAM_STAFF
     * @param target 修改目标人的id
     * @param lv 权限等级 -1~2
     * @return 成功为修改数量int，否则为空
     */
    @Transactional
    @AccessLimit
    public NetResult updateLevel(int id, int target, int lv) {

        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                ServiceResult t = service.updateUserLevel(applyUser, target, lv);
                return NetResult.makeResult(t.getCode(), null, null);
            }
        });
    }

    @RequestMapping("login")
    /***
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return 成功为用户信息，否则为空或错误消息
     */
    @NoNeedAccess
    @NoLog
    public NetResult login(String username, String password) {

        return ApiMethod.makeResultNoLogin(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                UserDetailInfo info = null;
                if (UserUtility.isValidForUsername(username) && UserUtility.isValidForPassword(password)) {
                    String passwords = UserUtility.passwordEncode(password);
                    ServiceResult<UserVo> sc = service.loginUser(username, passwords);
                    info = (UserDetailInfo) service.getUserInfo(sc.getObj().getId()).getObj();
                    return NetResult.makeResult(sc.getCode(), null, new Object[]{sc.getObj().getUid(), info});
                } else {
                    return NetResult.makeResult(EC_InvalidParameter, "用户名和密码的强度不足", null);
                }
            }
        });
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
    @NoLog
    public NetResult updateSecQues(String ques, String ans) {
        final String q = ques, a = ans;
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                String ans = UserUtility.passwordEncode(q);
                ServiceResult sc = service.UpdateUserSecQuestion(applyUser.getId(), ques, ans);
                ((UserSecurityInfo) sc.getObj()).setSec_ans(null);
                return NetResult.makeResult(sc, null);
            }
        });

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
    public NetResult updatedetails(
            @RequestParam(required = false)
            String email,
            @RequestParam(required = false)
            String phone,
            @RequestParam(required = false)
            String address,
            @RequestParam(required = false)
            String note) {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                ServiceResult<UserDetailInfo> dt = service.UpdateUserDetails(applyUser.getId(), address,
                        phone,
                        email,
                        note);
                return NetResult.makeResult(dt.getCode(), null, dt.getObj());
            }
        });

    }


    @RequestMapping(value = "register", method = RequestMethod.POST)
    /***
     * 注册 密保问题和答案一个为空即视为不设置密保。
     * @param username 用户名
     * @param password 用户密码
     * @param question 密保问题
     * @param answer 密保答案
     * @return 仅在出NET_UNKNOWN错误时候为错误消息，否则为空
     */
    @Transactional
    @NoNeedAccess
    @NoLog
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
                if (answer != null)
                    answer = UserUtility.passwordEncode(answer);
                ServiceResult<Integer> sc = service.registerUser(username, password, question, answer);
                ServiceResult result = service.UpdateUserDetails(sc.getObj(), address,
                        phone, email, note);

                return NetResult.makeResult(sc, null);
            } else {
                return NetResult.makeResult(EC_InvalidParameter, "用户名或密码强度不足");
            }
        } catch (Error.BackendError e) {
            _ERROR.rollback();
            return NetResult.makeResult(e.getCode(), e.getDetail());
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
    @NoLog
    public NetResult updatePassword(int id, String password, String oldpassword) {
        ServiceResult result = null;
        try {
            result = service.UpdateUserPassword(id, UserUtility.passwordEncode(password),
                    UserUtility.passwordEncode(oldpassword));
            return NetResult.makeResult(result, null);
        } catch (Error.BackendError e) {
            _ERROR.rollback();
            return NetResult.makeResult(e.getCode(), e.getDetail());
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
    @NoNeedAccess
    @NoLog
    public NetResult updatePasswordByQues(String name, String password, String answer) {
        ServiceResult result = null;
        try {
            result = service.updateUserPasswordByQuestion(name, UserUtility.passwordEncode(password)
                    , UserUtility.passwordEncode(answer));
            return NetResult.makeResult(result, null);
        } catch (Error.BackendError e) {
            _ERROR.rollback();
            return NetResult.makeResult(e.getCode(), e.getDetail());
        } catch (Exception ex) {
            _ERROR.rollback();
            return NetResult.makeResult(EC_UNKNOWN, ex.getMessage());
        }
    }

    @Transactional
    @RequestMapping("pullusers")
    @AccessLimit
    /***
     * 为EXAM_STAFF 用户获取用户列表
     * @param id 拉取人
     * @param from 起始条数
     * @param num 数量
     * @return 成功为用户信息列表，否则为错误信息或空，msg为NET_FAILURE时候data为错误信息
     */
    public NetResult getUserinfoPage(int from, int num) {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                ServiceResult result = service.pullUserInfo(from, num);
                return NetResult.makeResult(result, null);
            }
        });
    }


    @RequestMapping("getUser")
    public NetResult getUserInfo(@RequestParam(required = false) Integer id) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                UserDetailInfo sc = (UserDetailInfo) service.getUserInfo(id == null ? applyUser.getId() : id).getObj();
                return NetResult.makeResult(SUCCESS, null, sc);
            }
        }).methodWithLogined();
    }

    @RequestMapping("count")
    @AccessLimit
    public NetResult count() {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                return NetResult.makeResult(service.total(), null);
            }
        }).methodWithLogined();
    }


    @RequestMapping("secQues")
    @NoNeedAccess
    public NetResult getQues(String name) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                return NetResult.makeResult(service.getUserSecQuestion(name), null);
            }
        }).method();
    }

    @RequestMapping("answerSec")
    @NoNeedAccess
    @NoLog
    public NetResult replySec(String name, String answer) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                return NetResult.makeResult(service.replySecQuestion(name, UserUtility.passwordEncode(answer)), null);
            }
        }).method();
    }

    @RequestMapping("findUser")
    @AccessLimit
    public NetResult findUserByName(String name) {
        return ApiMethod.makeResultNoLogin(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                return NetResult.makeResult(service.findUserByNameLike(name), null);
            }
        });
    }


}
