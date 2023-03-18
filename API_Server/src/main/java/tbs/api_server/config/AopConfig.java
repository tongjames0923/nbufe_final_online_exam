package tbs.api_server.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CookieValue;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.simple.UserSecurityInfo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Enumeration;

import static tbs.api_server.utility.Error.*;

@Aspect
@Component
public class AopConfig {

    @Pointcut("within(tbs.api_server.publicAPI.*)")
    public void accessPointCut()
    {
    }


    @Resource
    HttpServletRequest request;

    @Resource
    AccessManager manager;
    @Around("accessPointCut()")
    public Object accessControl(ProceedingJoinPoint proceedingJoinPoint)
    {

        try {
            MethodSignature signature= (MethodSignature) proceedingJoinPoint.getSignature();
            Annotation annotation= signature.getMethod().getAnnotation(NoNeedAccess.class);
            if(annotation==null)
            {
               Enumeration<String> em= request.getHeaders("X-TOKEN");
               String token=null;
               while (em.hasMoreElements())
               {
                   token=em.nextElement();
               }

               UserSecurityInfo sec= manager.getLogined(token);
               if(sec==null)
               {
                   return NetResult.makeResult(FC_UNAVALIABLE,"需要登录");
               }
                System.out.println("request by "+sec.getId()+" "+sec.getName());
            }
            Object result=proceedingJoinPoint.proceed();
            return result;
        } catch (Throwable throwable) {
            return NetResult.makeResult(EC_UNKNOWN,throwable.getMessage());
        }
    }

}
