package tbs.api_server.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import tbs.api_server.backend.mappers.UserMapper;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.objects.simple.UserSecurityInfo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class AccessManager {


    @Value("${tbs.login.timeout}")
    int login_timeout;
    @Resource
    RedisTemplate<String,UserSecurityInfo> redis;

    public static AccessManager ACCESS_MANAGER;

    @Autowired
    public AccessManager()
    {
        ACCESS_MANAGER=this;
    }

    public void renew()
    {
        String m_token=token();
        if(m_token!=null)
        {
            redis.expire("loginKey-"+m_token,login_timeout,TimeUnit.MINUTES);
        }
    }

    public String putLogined(UserSecurityInfo info)
    {
        String uid= UUID.randomUUID().toString();
        ValueOperations<String,UserSecurityInfo> ops= redis.opsForValue();
        while (ops.get(uid)!=null)
        {
        }
        ops.set("loginKey-"+uid,info,login_timeout, TimeUnit.MINUTES);
        return uid;
    }

    public UserSecurityInfo getLogined(String uid)
    {
        ValueOperations<String,UserSecurityInfo> ops= redis.opsForValue();
        return ops.get("loginKey-"+uid);
    }
    public void logOut(String access)
    {
        redis.delete("loginKey-"+access);
    }

    @Resource
    HttpServletRequest request;


    public String token()
    {
        Enumeration<String> em= request.getHeaders("X-TOKEN");
        String token=null;
        while (em.hasMoreElements())
        {
            token=em.nextElement();
        }
        return token;
    }
    public UserSecurityInfo getLoginedFromHttp()
    {
        UserSecurityInfo sec= getLogined(token());
        return sec;
    }
    @Resource
    UserMapper userMapper;
    public int getAccessLevel(int id)
    {
        UserDetailInfo detailInfo=
        userMapper.getUserDetailInfoByID(id);
        if(detailInfo!=null)
            return detailInfo.getLevel();
        else
            return -1;
    }
}
