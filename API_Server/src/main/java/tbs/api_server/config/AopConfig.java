package tbs.api_server.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.simple.LogPojo;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.utils.LogQueueSender;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.Enumeration;

import static tbs.api_server.config.constant.const_Log.*;
import static tbs.api_server.utility.Error.*;

@Aspect
@Component
public class AopConfig {

    @Value("${tbs.queue.log}")
    String log_q;

    @Pointcut("within(tbs.api_server.publicAPI.*)")
    public void accessPointCut()
    {
    }


    @Pointcut("within(tbs.api_server.backend.serviceImp.*)")
    public void logWritePointCut()
    {

    }
    public String getRemoteHost(javax.servlet.http.HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    }
    private LogPojo beginALog(ProceedingJoinPoint proceedingJoinPoint)
    {
        MethodSignature signature= (MethodSignature) proceedingJoinPoint.getSignature();
        LogPojo logPojo=new LogPojo();
        logPojo.setLog_function(proceedingJoinPoint.getTarget().getClass().getName()+signature.getName());
        Enumeration<String> em= request.getHeaders("X-TOKEN");
        String token=null;
        while (em.hasMoreElements())
        {
            token=em.nextElement();
        }

        UserSecurityInfo sec= manager.getLogined(token);
        if(sec==null)
        {
            logPojo.setLog_invoker(String.format("invoke ip:%s",getRemoteHost(request)));
        }
        else
        logPojo.setLog_invoker(String.format("user id:%d,username:%s from:%s",sec.getId(),sec.getName(),getRemoteHost(request)));

        String paramstext="";
        Object[] args= proceedingJoinPoint.getArgs();
        int index=0;
        for (String i:signature.getParameterNames())
        {
            paramstext+=String.format("[%s]=[%s] ",i,args[index++].toString());
        }
        logPojo.setLog_params(paramstext);
        logPojo.setLog_begin(new Date());
        return logPojo;
    }

    @Resource
    LogQueueSender sender;
    void WriteLog(LogPojo pojo)
    {

        sender.send(log_q,pojo);
    }


    @Resource
    HttpServletRequest request;

    @Resource
    AccessManager manager;
    @Around("accessPointCut()")
    public Object accessControl(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        LogPojo log=beginALog(proceedingJoinPoint);
        log.setLog_type(TYPE_API_ACCESS);
        Object result;
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
                   log.setLog_end(new Date());
                   log.setLog_type(TYPE_ERROR);
                   WriteLog(log);
                   return NetResult.makeResult(FC_UNAVALIABLE,"需要登录");
               }
                System.out.println("request by "+sec.getId()+" "+sec.getName());
            }
           result =proceedingJoinPoint.proceed();
            log.setLog_end(new Date());
            if(result!=null)
                log.setLog_return(result.toString());
            WriteLog(log);
            return result;
        } catch (BackendError error)
        {
            log.setLog_end(new Date());
            log.setLog_type(TYPE_ERROR);
            log.setLog_error(error.getDetail());
            WriteLog(log);
            throw error;
        }

        catch (Throwable throwable) {

            log.setLog_end(new Date());
            log.setLog_type(TYPE_ERROR);
            log.setLog_error(throwable.getMessage());
            WriteLog(log);
            throw  throwable;
        }
    }
    @Around("logWritePointCut()")
    public Object MethodInvokeLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        LogPojo log=beginALog(proceedingJoinPoint);
        log.setLog_type(TYPE_IMPL);
        try {
           Object result= proceedingJoinPoint.proceed();
           log.setLog_end(new Date());
           if(result!=null)
               log.setLog_return(result.toString());
            WriteLog(log);
           return result;
        }
        catch (BackendError error)
        {
            log.setLog_end(new Date());
            log.setLog_type(TYPE_ERROR);
            log.setLog_error(error.getDetail());
            WriteLog(log);
            throw  error;
        }

        catch (Throwable throwable) {
            log.setLog_end(new Date());
            log.setLog_type(TYPE_ERROR);
            log.setLog_error(throwable.getMessage());
            WriteLog(log);
            throw throwable;
        }
    }




}
