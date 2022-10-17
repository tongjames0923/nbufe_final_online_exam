package tbs.api_server.backend.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tbs.api_server.backend.mappers.TagMapper;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.Tag;
import tbs.api_server.services.TagService;

import java.util.List;
import java.util.Optional;

@Service
public class TagImp implements TagService
{
    @Autowired
    TagMapper tg;

    @Override
    public ServiceResult changeUsed(String tagname, int altervalue)
    {
        final int[] res = {0, 0};
        Optional.of(tg.getTagByName(tagname)).ifPresent(tag ->
                                                        {
                                                            res[0] = tg.setUsed(tag.getTag_id(),
                                                                                tag.getTag_used() + altervalue);
                                                            if (res[0] == 1)
                                                                res[1] = tag.getTag_used() + altervalue;
                                                        });
        return ServiceResult.makeResult(res[0], res[1]);
    }

    @Override
    public ServiceResult newTag(String tagname)
    {
        int t= tg.insertTag(tagname);
        return ServiceResult.makeResult(t,null);
    }

    @Override
    public ServiceResult deleteTag(String tagname)
    {
        int tx=  tg.deleteTagByName(tagname);
        return ServiceResult.makeResult(tx);
    }

    @Override
    public ServiceResult listTags()
    {
        List<Tag> tgs= tg.getAllTags();
        return ServiceResult.makeResult(tgs.size(),tgs);
    }

    @Override
    public ServiceResult linkTag(int ques_id, String tagname)
    {
        final int[] res={0};
        Optional.of(tg.getTagByName(tagname)).ifPresent(tag -> {
            res[0]= tg.linkTag(ques_id,tag.getTag_id());
        });
        return ServiceResult.makeResult(res[0]);
    }

    @Override
    public ServiceResult unLinkTag(int ques_id, String tagname)
    {
        final int[] res={0};
        Optional.of(tg.getTagByName(tagname)).ifPresent(tag -> {
            res[0]= tg.unLinkTag(ques_id,tag.getTag_id());
        });
        return ServiceResult.makeResult(res[0]);
    }

    @Override
    public ServiceResult findTagsByQuestion(int ques_id)
    {
        List<Tag> tags= tg.findTagsByQuestion(ques_id);
        return ServiceResult.makeResult(tags.size(),tags);
    }
}
