package tbs.api_server.config;


import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import tbs.api_server.backend.mappers.UserMapper;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.utility.Error;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static tbs.api_server.utility.Error.FC_UNAVALIABLE;
import static tbs.api_server.utility.Error._ERROR;

@Component
public class AccessManager {


    @Value("${tbs.login.timeout}")
    int login_timeout;
    @Resource
    RedisTemplate<String, UserSecurityInfo> redis;
    @Resource
    RedisTemplate<String, String> str_redis;
    public static AccessManager ACCESS_MANAGER;
    ValueOperations<String, UserSecurityInfo> ops;
    ValueOperations<String, String> str_ops;



    @Autowired
    void init()
    {
        ops = redis.opsForValue();
        str_ops = str_redis.opsForValue();
    }
    @Autowired
    public AccessManager() {
        ACCESS_MANAGER = this;
    }

    public void renew() {
        String m_token = token();
        if (m_token != null) {
            redis.expire("loginKey-" + m_token, login_timeout, TimeUnit.MINUTES);
            str_redis.expire("single_login-" + getLogined(m_token).getId(), login_timeout, TimeUnit.MINUTES);
        }
    }

    public String putLogined(UserSecurityInfo info) throws Error.BackendError {
        if (str_ops.get("single_login-" + info.getId()) != null)
            throw _ERROR.throwError(FC_UNAVALIABLE, "禁止多地登录,请先登出账户");
        String uid = UUID.randomUUID().toString()+"-mix-"+info.getId();
        str_ops.set("single_login-" + info.getId(), uid, login_timeout, TimeUnit.MINUTES);
        ops.set("loginKey-" + uid, info, login_timeout, TimeUnit.MINUTES);
        return uid;
    }

    public UserSecurityInfo getLogined(String uid) {
        ValueOperations<String, UserSecurityInfo> ops = redis.opsForValue();
        return (UserSecurityInfo) ops.get("loginKey-" + uid);
    }

    public void logOut(String access) {
        UserSecurityInfo u = getLogined(access);
        str_redis.delete("single_login-" + u.getId());
        redis.delete("loginKey-" + access);
    }

    @Resource
    HttpServletRequest request;


    public String token() {
        Enumeration<String> em = request.getHeaders("X-TOKEN");
        String token = null;
        while (em.hasMoreElements()) {
            String tmp=em.nextElement();
            if(!StringUtil.isNullOrEmpty(tmp))
            token = tmp;
        }
        if(token!=null)
        {
            if(token.trim().equals("null"))
            {
                token=null;
            }
        }
        return token;
    }

    public UserSecurityInfo getLoginedFromHttp() {
        UserSecurityInfo sec = getLogined(token());
        return sec;
    }

    @Resource
    UserMapper userMapper;
}
