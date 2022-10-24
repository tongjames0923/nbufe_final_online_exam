package tbs.api_server.publicAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tbs.api_server.config.ApplicationConfig;
import tbs.api_server.config.constant.const_Resource_Type;
import tbs.api_server.config.constant.const_Text;
import tbs.api_server.config.constant.const_User;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.QuestionResource;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.services.ResourceService;
import tbs.api_server.utility.Error;
import tbs.api_server.utility.FileUtility;
import tbs.api_server.utility.MapperStore;
import tbs.api_server.utility.SecurityTools;

import java.io.File;
import java.util.Date;
import java.util.Optional;
import java.util.function.Consumer;

@RestController
@RequestMapping(value = "/resource/*")
public class ResourceController
{
    @Autowired
    ResourceService service;





    @RequestMapping("/getByType")
    public NetResult getResourcesByType(int type, int from, int num)
    {
        try
        {

            ServiceResult result = service.getResourcesByType(type, from, num);
            return NetResult.makeResult(result.getCode() > 0, const_Text.NET_success, result.getObj());
        } catch (Exception e)
        {
            return NetResult.makeResult(false, const_Text.NET_FAILURE, e.getMessage().toString());
        }
    }

    String makePath(int type, String note) throws Exception
    {
        Date date = new Date();
        String d = Integer.toHexString((int) (date.getTime() % Integer.MAX_VALUE)) + note;

        d = SecurityTools.Encrypt_str(d);
        d = d.substring(32, 64);
        switch (type)
        {
            case const_Resource_Type
                    .Audio:
                d += ".mp3";
                break;
            case const_Resource_Type.Image:
                d += ".jpeg";
                break;
            case const_Resource_Type.Video:
                d += ".mp4";
                break;
            case const_Resource_Type.Text:
                d += ".md";
                break;
        }
        return d;
    }

    String genPath(String filename)
    {
        return ApplicationConfig.resourceDir + "/" + filename;
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)

    public NetResult upload(@RequestParam MultipartFile file, @RequestParam int type, @RequestParam String note)
    {
        try
        {
            MultipartFile bytes = file;
            int tp = type;

            String filepath = makePath(tp, note);
            File file1 = new File(genPath(filepath));
            int total = 0;
            ServiceResult result = service.UploadResource(tp, filepath, note);
            if (!bytes.isEmpty())
            {
                FileUtility.BaseThen then = new FileUtility.FileWriteThen(bytes.getInputStream(), true);
                FileUtility.existFile(file1.getAbsolutePath(), then);
                total = (int) then.result();
            }

            return NetResult.makeResult(result.getCode() > 0, const_Text.NET_success, total);
        } catch (Throwable e)
        {
            Error._ERROR.rollback();
            return NetResult.makeResult(false, const_Text.NET_FAILURE, e.getMessage());
        }
    }

    @RequestMapping("/delete")
    public NetResult delete(int userid, int resource_id)
    {
        final NetResult result = new NetResult(false, null, const_Text.NET_FAILURE);
        try
        {
            QuestionResource resource = (QuestionResource) service.getResourceById(resource_id).getObj();
            Optional.ofNullable(resource).ifPresent(new Consumer<QuestionResource>()
            {
                @Override
                public void accept(QuestionResource questionResource)
                {
                    UserDetailInfo info = MapperStore.userMapper.getUserDetailInfoByID(userid);
                    if (info.getLevel() == const_User.LEVEL_EXAM_STAFF)
                    {

                        service.DeleteResource(userid);
                        FileUtility.BaseThen then = new FileUtility.FileDeleteThen();
                        FileUtility.existFile(genPath(questionResource.getResource()), then);
                        try
                        {
                            result.setData(then.result());
                            result.setSuccess(true);
                        } catch (Throwable e)
                        {
                            result.setMessage(e.getMessage());
                        }


                    } else
                    {
                        result.setMessage(const_Text.ERRROR_CODE_TEXT(const_User.plUser_NO_RIGHTS));
                    }
                }
            });

        } catch (Throwable e)
        {
            Error._ERROR.rollback();
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping("/link")
    public NetResult linkResource(int ques_id, int resource_id)
    {

        try
        {
           return NetResult.makeResult(service.linkResource(resource_id,ques_id).getCode()>1,const_Text.NET_success);
        }catch (Throwable e)
        {
            Error._ERROR.rollback();
            return NetResult.makeResult(false,const_Text.NET_FAILURE,e.getMessage());
        }
    }

    @RequestMapping("/unlink")
    public NetResult unLinkResource(int ques_id, int resource_id)
    {
        try
        {
            return NetResult.makeResult(service.unlinkResource(resource_id,ques_id).getCode()>1,const_Text.NET_success);
        }catch (Throwable e)
        {
            Error._ERROR.rollback();
            return NetResult.makeResult(false,const_Text.NET_FAILURE,e.getMessage());
        }
    }

    @RequestMapping("/getByNote")
    public NetResult getResourcesByNote(String note,int from,int to)
    {
        try
        {
         ServiceResult rs= service.getResourceByNote(note, from, to);
         return NetResult.makeResult(rs.getCode()>0,const_Text.NET_success,rs.getObj());
        }
        catch (Throwable e)
        {
            return NetResult.makeResult(false,const_Text.NET_FAILURE,e.getMessage());
        }
    }
    @RequestMapping("/getByQues")
    public NetResult getResourcesByQuestion(int ques)
    {
        try
        {
            ServiceResult rs=service.getResourceByQuestion(ques);
            return NetResult.makeResult(rs.getCode()>0,const_Text.NET_success,rs.getObj());
        }catch (Throwable e)
        {
            return NetResult.makeResult(false,const_Text.NET_FAILURE,e.getMessage());
        }
    }


}
