package tbs.api_server.backend.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import tbs.api_server.backend.mappers.TagMapper;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.Tag;
import tbs.api_server.services.TagService;
import tbs.api_server.utility.Error;

import java.util.List;
import java.util.Optional;

import static tbs.api_server.utility.Error.*;

@Service
@Scope("prototype")
public class TagImp implements TagService
{
    @Autowired
    TagMapper tg;

    @Override
    public ServiceResult changeUsed(String tagname, int altervalue) throws BackendError {
        final int[] res = {0, 0};
        Optional.ofNullable(tg.getTagByName(tagname)).ifPresent(tag ->
                                                        {
                                                            res[0] = tg.setUsed(tag.getTag_id(),
                                                                                tag.getTag_used() + altervalue);
                                                            if (res[0] == 1)
                                                                res[1] = tag.getTag_used() + altervalue;
                                                        });
        if(res[0]>0)
            return ServiceResult.makeResult(SUCCESS);
        else
            throw _ERROR.throwError(EC_DB_UPDATE_FAIL,"更新使用问题次数失败");
    }

    @Override
    public ServiceResult newTag(String tagname) throws Error.BackendError {
        int t= tg.insertTag(tagname);
        if(t>0)
            return ServiceResult.makeResult(SUCCESS);
        else
            throw  _ERROR.throwError(EC_DB_INSERT_FAIL,"新增标签失败");
    }

    @Override
    public ServiceResult deleteTag(String tagname) throws BackendError {
        int tx=  tg.deleteTagByName(tagname);
        if(tx>0)
            return ServiceResult.makeResult(SUCCESS);
        else
            throw _ERROR.throwError(EC_DB_DELETE_FAIL,"删除标签失败");
    }

    @Override
    public ServiceResult listTags() throws BackendError {
        List<Tag> tgs= tg.getAllTags();
        if(tgs.size()>0)
            return ServiceResult.makeResult(SUCCESS,tgs);
        else
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"标签列表为空");
    }

    @Override
    public ServiceResult linkTag(int ques_id, String tagname) throws BackendError {
        final int[] res={0};
        Optional.of(tg.getTagByName(tagname)).ifPresent(tag -> {
            res[0]= tg.linkTag(ques_id,tag.getTag_id());
            if(res[0]>0)
            res[0]+= tg.setUsed(tag.getTag_id(), tag.getTag_used()+1);
        });
        if(res[0]>1)
        return ServiceResult.makeResult(SUCCESS);
        else
            throw  _ERROR.throwError(EC_DB_INSERT_FAIL,"链接标签失败");
    }

    @Override
    public ServiceResult unLinkTag(int ques_id, String tagname) throws BackendError {
        final int[] res={0};
        Optional.ofNullable(tg.getTagByName(tagname)).ifPresent(tag -> {
            res[0]= tg.unLinkTag(ques_id,tag.getTag_id());
            if(res[0]>0)
            res[0]+= tg.setUsed(tag.getTag_id(), tag.getTag_used()-1);
        });
        if(res[0]>1)
            return ServiceResult.makeResult(SUCCESS);
        else
           throw  _ERROR.throwError(EC_DB_DELETE_FAIL,"解除标签失败");
    }

    @Override
    public ServiceResult findTagsByQuestion(int ques_id) throws BackendError {
        List<Tag> tags= tg.findTagsByQuestion(ques_id);
        if(tags.size()>0)
            return ServiceResult.makeResult(SUCCESS);
        else
            throw _ERROR.throwError(EC_DB_SELECT_NOTHING,"这个问题没有资源");
    }
}
