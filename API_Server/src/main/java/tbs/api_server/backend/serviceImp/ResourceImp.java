package tbs.api_server.backend.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tbs.api_server.backend.mappers.ResourceMapper;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.QuestionResource;
import tbs.api_server.services.ResourceService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceImp implements ResourceService
{
    @Autowired
    ResourceMapper mp;

    @Override
    public ServiceResult getResourceById(int id)
    {
        QuestionResource resource = mp.getResourceById(id);
        return ServiceResult.makeResult(resource!=null?1:0,resource);
    }

    @Override
    public ServiceResult getResourceByNote(String note,int from,int n)
    {
        List<QuestionResource> ls=mp.findResourcesByNote(note,from,n);
        return ServiceResult.makeResult(ls.size(),ls);
    }

    @Override
    public ServiceResult getResourceByQuestion(int ques)
    {
        List<Integer> lnk=mp.getQuestionLink(ques);
        ArrayList<QuestionResource> resources =new ArrayList<>();

        for(int i:lnk)
        {
            resources.add(mp.getResourceById(i));
        }
        return ServiceResult.makeResult(resources.size(),resources);
    }

    @Override
    public ServiceResult getResourcesByType(int type, int from, int num)
    {
        List<QuestionResource> ls=mp.getResourcesByType(type,from,num);
        return ServiceResult.makeResult(ls.size(),ls);
    }

    @Override
    public ServiceResult UploadResource(int type, String path, String note)
    {
        int up= mp.uploadResource(type,path,note);
        return ServiceResult.makeResult(up);
    }

    @Override
    public ServiceResult DeleteResource(int id)
    {
        int dp=mp.deleteResourceById(id);
        return ServiceResult.makeResult(dp);
    }

    @Override
    public ServiceResult linkResource(int res_id, int ques_id)
    {
        int lr=mp.linkResource(ques_id,res_id);
        return ServiceResult.makeResult(lr);
    }

    @Override
    public ServiceResult unlinkResource(int res_id, int ques_id)
    {
        int ulr=mp.unlinkResource(ques_id,res_id);
        return ServiceResult.makeResult(ulr);
    }
}
