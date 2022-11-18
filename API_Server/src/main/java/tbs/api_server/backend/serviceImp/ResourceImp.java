package tbs.api_server.backend.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import tbs.api_server.backend.mappers.ResourceMapper;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.QuestionResource;
import tbs.api_server.services.ResourceService;

import java.util.ArrayList;
import java.util.List;

import static tbs.api_server.utility.Error.*;

@Service
@Scope("prototype")
public class ResourceImp implements ResourceService
{
    @Autowired
    ResourceMapper mp;

    @Override
    public ServiceResult getResourceById(int id) throws BackendError {
        QuestionResource resource = mp.getResourceById(id);
        if(resource!=null)
        return ServiceResult.makeResult(SUCCESS,resource);
        else
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"不存在此id的资源");
    }

    @Override
    public ServiceResult getResourceByNote(String note,int from,int n) throws BackendError {
        List<QuestionResource> ls=mp.findResourcesByNote(note,from,n);
        if(ls.size()>0)
            return ServiceResult.makeResult(SUCCESS,ls);
        else
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"此备注没有资源");
    }

    @Override
    public ServiceResult getResourceByQuestion(int ques) throws BackendError {
        List<Integer> lnk=mp.getQuestionLink(ques);
        ArrayList<QuestionResource> resources =new ArrayList<>();

        for(int i:lnk)
        {
            resources.add(mp.getResourceById(i));
        }
        if(resources.size()>0)
            return ServiceResult.makeResult(SUCCESS,resources);
        else
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"此问题不存在资源");
    }

    @Override
    public ServiceResult getResourcesByType(int type, int from, int num) throws BackendError {
        List<QuestionResource> ls=mp.getResourcesByType(type,from,num);
        if(ls.size()>0)
            return ServiceResult.makeResult(SUCCESS,ls);
        else
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"此类型不存在资源");
    }

    @Override
    public ServiceResult UploadResource(int type, String path, String note) throws BackendError {
        int up= mp.uploadResource(type,path,note);
        if(up>0)
            return ServiceResult.makeResult(SUCCESS);
        throw _ERROR.throwError(EC_DB_INSERT_FAIL,"问题上传失败");
    }

    @Override
    public ServiceResult DeleteResource(int id) throws BackendError {
        int dp=mp.deleteResourceById(id);
        if(dp>0)
            return ServiceResult.makeResult(SUCCESS);
        throw _ERROR.throwError(EC_DB_DELETE_FAIL,"资源删除失败");
    }

    @Override
    public ServiceResult linkResource(int res_id, int ques_id) throws BackendError {
        int lr=mp.linkResource(ques_id,res_id);
        if(lr>0)
            return ServiceResult.makeResult(SUCCESS);
        throw _ERROR.throwError(EC_DB_INSERT_FAIL,"资源链接失败");
    }

    @Override
    public ServiceResult unlinkResource(int res_id, int ques_id) throws BackendError {
        int ulr=mp.unlinkResource(ques_id,res_id);
        if(ulr>0)
        return ServiceResult.makeResult(SUCCESS);
        throw _ERROR.throwError(EC_DB_DELETE_FAIL,"取消资源链接失败");
    }
}
