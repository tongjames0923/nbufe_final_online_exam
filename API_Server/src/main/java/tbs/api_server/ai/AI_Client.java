package tbs.api_server.ai;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tbs.api_server.ai.model.Ai_RequestModel;
import tbs.api_server.ai.model.Ai_Resp;

import javax.annotation.Resource;

@Component
public class AI_Client {
    @Value("${tbs.ai_token}")
    String token;


    @Value("${tbs.ai_address}")
    String address;
    @Resource
    RestTemplate restTemplate;

    HttpHeaders makeHttpHeaders()
    {
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization","Bearer "+token);
        headers.add("Content-Type","application/json");
        return headers;
    }
    public String request(String msg)
    {
        try {
         Ai_Resp asp=   restTemplate.exchange(address, HttpMethod.POST,new HttpEntity<>(Ai_RequestModel.request(msg),makeHttpHeaders()), Ai_Resp.class).getBody();
         if(!asp.getChoices().isEmpty())
         {
             return asp.getChoices().get(0).getMessage().getContent();
         }
         else
            return "没有回答";
        }catch (Exception e)
        {
            e.printStackTrace();
            return e.getMessage();
        }

    }

}
