package tbs.api_server.publicAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tbs.api_server.config.constant.const_Text;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.services.TagService;

@RestController
@RequestMapping("tag/*")
public class TagController
{

    @Autowired
    TagService service;


    private NetResult resultMake(ServiceResult result,String badMSG)
    {
        String msg=result.getCode()>0?const_Text.NET_success:badMSG;
        return NetResult.makeResult(result.getCode()>0,msg);
    }


    @RequestMapping("list")
    public NetResult list()
    {
        try
        {
            ServiceResult result= service.listTags();
            return new NetResult(true, result.getObj(), const_Text.NET_success);
        }catch (Exception e)
        {
            return NetResult.makeResult(false, e.getMessage());
        }

    }
    @RequestMapping
    public NetResult getTagsByQuestion(int ques)
    {
        try
        {
            return NetResult.makeResult(true,const_Text.NET_success,service.findTagsByQuestion(ques).getObj());
        }
        catch (Exception e)
        {
            return NetResult.makeResult(false,e.getMessage());
        }
    }
    @RequestMapping
    public NetResult addTag(String tag)
    {
        try
        {
            ServiceResult result= service.newTag(tag);
            return resultMake(result,"can't add tag");
        }catch (Exception e)
        {
            return NetResult.makeResult(false,e.getMessage());
        }
    }
    @RequestMapping
    public NetResult removeTag(String tag)
    {
        try
        {
            ServiceResult result = service.deleteTag(tag);
            return resultMake(result,"can't remove tag");
        }catch (Exception e)
        {
            return NetResult.makeResult(false,e.getMessage());
        }
    }
    @RequestMapping
    public NetResult linkTag(String tag,int ques)
    {
        try
        {
            ServiceResult result = service.linkTag(ques,tag);
            if(result.getCode()>0)
            {
                service.changeUsed(tag,1);
            }
            return resultMake(result,String.format("can not link tag %s to %d",tag,ques));
        }catch (Exception e)
        {
            return NetResult.makeResult(false,e.getMessage());
        }
    }
    @RequestMapping
    public NetResult unLinkTag(String tag,int ques)
    {
        try
        {
            ServiceResult result = service.unLinkTag(ques,tag);
            if(result.getCode()>0)
            {
                service.changeUsed(tag,-1);
            }
            return resultMake(result,"no link found");
        }catch (Exception e)
        {
            return NetResult.makeResult(false,e.getMessage());
        }
    }


}
