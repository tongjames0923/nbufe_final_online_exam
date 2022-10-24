package tbs.api_server.services;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tbs.api_server.objects.ServiceResult;

public interface TagService
{
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult changeUsed(String tagname, int altervalue);

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult newTag(String tagname);

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult deleteTag(String tagname);

    ServiceResult listTags();

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult linkTag(int ques_id,String tagname);

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult unLinkTag(int ques_id,String tagname);

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    ServiceResult findTagsByQuestion(int ques_id);

}
