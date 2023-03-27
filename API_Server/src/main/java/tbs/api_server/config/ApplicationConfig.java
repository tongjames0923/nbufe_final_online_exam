package tbs.api_server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import tbs.api_server.ApiServerApplication;

@Component
public class ApplicationConfig
{

    @Value("${tbs.app_save}")
    public String saveLocation;


    @Autowired
    void setDir()
    {
        resourceDir=saveLocation+resourceDir;
        ReplyDir=saveLocation+ReplyDir;
        CheckFileDir=saveLocation+CheckFileDir;
        ApiServerApplication.Starter();
    }
    public static final String VERSION = "0.93";
    public static final String PROJECT_NAME = "nbufeFinalOnlineExam";
    public static final String PROJECT_SPEC="tbs is a very good person!!!";
    public static final String Default_Text = ApplicationConfig.PROJECT_SPEC+" " +
            ApplicationConfig.PROJECT_NAME+" " + ApplicationConfig.VERSION;

    public static String resourceDir="/resources",ReplyDir="/ReplyDir",CheckFileDir="/CheckFileDir";

}
