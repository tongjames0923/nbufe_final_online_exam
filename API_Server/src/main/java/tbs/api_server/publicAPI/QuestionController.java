package tbs.api_server.publicAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tbs.api_server.config.constant.const_Question;
import tbs.api_server.config.constant.const_Text;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.services.QuestionService;

import static tbs.api_server.utility.Error._ERROR;

@RestController
@RequestMapping("/question/*")
public class QuestionController {
    @Autowired
    QuestionService service;



    @RequestMapping(value = "create",method = RequestMethod.POST)

    public NetResult uploadQuestion(@RequestParam int type,@RequestParam int creator,@RequestParam String title,@RequestParam MultipartFile file,@RequestParam(required = false) Integer isopen,@RequestParam(required = false) Integer tag)
    {
        try {
           ServiceResult result= service.uploadQuestion(type,title,creator,file.getBytes(),isopen,tag);
           if(result.getCode()>0)
               return NetResult.makeResult(true,null);
           else
               return NetResult.makeResult(false, const_Text.NET_INSERT_FAIL);
        }catch (Throwable throwable)
        {
            _ERROR.rollback();
            return NetResult.makeResult(false,throwable.getLocalizedMessage(),null);
        }
    }
    @RequestMapping("delete")
    public NetResult delete(int ques,int user)
    {

        try {
            ServiceResult result=service.deleteQuestion(ques,user);
            if(result.getCode()>0)
                return NetResult.makeResult(true,const_Text.NET_success);
            else
                return NetResult.makeResult(false,const_Text.NET_FAILURE);
        }catch (Exception e)
        {
            _ERROR.rollback();
            return NetResult.makeResult(false,const_Text.NET_UNKNOWN,e.getMessage());
        }
    }
    @RequestMapping(value = "find",method = RequestMethod.POST)
    public NetResult find(int type,int[] codes,int from,int num)
    {
        ServiceResult result=null;
        try {
            final int TAG=1,TYPE=2;
            if(type==TAG)
            {
                result=service.findQuestionsByTags(codes,from,num);
            }
            else if(TYPE==type)
            {
                result=service.findQuestionsByType(codes,from,num);
            }
            else
            {
                return NetResult.makeResult(false,const_Text.NET_FIND_QUES_ERROR);
            }

                return NetResult.makeResult(true,const_Text.NET_success,result.getObj());


        }catch (Exception e)
        {
            return NetResult.makeResult(false,const_Text.NET_UNKNOWN,e.getMessage());
        }
    }


    @RequestMapping(value = "search",method = RequestMethod.POST)
    public NetResult serach(String title,int from,int num)
    {
        try {
            ServiceResult serviceResult=service.findQuestionsByTitle(title,from,num);
            if(serviceResult.getCode()>0)
                return NetResult.makeResult(true,const_Text.NET_success,serviceResult.getObj());
            else
                return NetResult.makeResult(false,const_Text.NET_NOT_FIND);

        }catch (Exception e)
        {
            return NetResult.makeResult(false,const_Text.NET_UNKNOWN,e.getMessage());
        }
    }

    @RequestMapping("list")
    public NetResult list(int from,int num)
    {
        return NetResult.makeResult(true,const_Text.NET_success,service.listQuestions(from,num).getObj());
    }

    @RequestMapping(value = "updateFile",method = RequestMethod.POST)
    public NetResult updateFile(int id,MultipartFile file)
    {
        try {
           ServiceResult result= service.updateQuestionValue(id, const_Question.col_file,file.getBytes());
           if(result.getCode()>0)
           return NetResult.makeResult(true,const_Text.NET_success,null);
           else
               return NetResult.makeResult(false,const_Text.NET_UPDATE_FIAL);
        }catch (Exception e)
        {
            _ERROR.rollback();
            return NetResult.makeResult(false,const_Text.NET_UNKNOWN,e.getMessage());
        }

    }

    @RequestMapping("updatePublic")
    public NetResult updatePublic(int ques,int publicable)
    {
        try {
            int cd= service.updateQuestionValue(ques,const_Question.col_publicable,publicable).getCode();
            if(cd>0)
                return NetResult.makeResult(true,const_Text.NET_success);
            else
                return NetResult.makeResult(false,const_Text.NET_UPDATE_FIAL);

        }catch (Exception e)
        {
            _ERROR.rollback();
            return NetResult.makeResult(false,const_Text.NET_UNKNOWN,e.getMessage());
        }
    }
    @RequestMapping(value = "updateTitle",method = RequestMethod.POST)
    public NetResult updateTitle(int ques,String title)
    {
        try {
            int cd= service.updateQuestionValue(ques,const_Question.col_title,title).getCode();
            if(cd>0)
                return NetResult.makeResult(true,const_Text.NET_success);
            else
                return NetResult.makeResult(false,const_Text.NET_UPDATE_FIAL);

        }catch (Exception e)
        {
            _ERROR.rollback();
            return NetResult.makeResult(false,const_Text.NET_UNKNOWN,e.getMessage());
        }
    }
    @RequestMapping("updateType")
    public NetResult updateType(int ques,int type)
    {
        try {
            int cd= service.updateQuestionValue(ques,const_Question.col_type,type).getCode();
            if(cd>0)
                return NetResult.makeResult(true,const_Text.NET_success);
            else
                return NetResult.makeResult(false,const_Text.NET_UPDATE_FIAL);

        }catch (Exception e)
        {
            _ERROR.rollback();
            return NetResult.makeResult(false,const_Text.NET_UNKNOWN,e.getMessage());
        }


    }

    @RequestMapping("questionCount")
    public NetResult getCount()
    {
        return NetResult.makeResult(true,const_Text.NET_success,service.questionsLength().getObj());
    }

}
