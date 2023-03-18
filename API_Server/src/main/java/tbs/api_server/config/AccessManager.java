package tbs.api_server.config;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import tbs.api_server.objects.simple.UserSecurityInfo;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class AccessManager {

    @Resource
    RedisTemplate<String,UserSecurityInfo> redis;
    public String putLogined(UserSecurityInfo info)
    {
        String uid= UUID.randomUUID().toString();
        ValueOperations<String,UserSecurityInfo> ops= redis.opsForValue();
        while (ops.get(uid)!=null)
        {
        }
        ops.set("loginKey-"+uid,info,30, TimeUnit.MINUTES);
        return uid;
    }

    public UserSecurityInfo getLogined(String uid)
    {
        ValueOperations<String,UserSecurityInfo> ops= redis.opsForValue();
        return ops.get("loginKey-"+uid);
    }
}
