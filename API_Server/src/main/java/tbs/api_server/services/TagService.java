package tbs.api_server.services;

import tbs.api_server.objects.ServiceResult;

public interface TagService
{
    ServiceResult changeUsed(String tagname, int altervalue);

    ServiceResult newTag(String tagname);

    ServiceResult deleteTag(String tagname);

    ServiceResult listTags();

    ServiceResult linkTag(int ques_id,String tagname);

    ServiceResult unLinkTag(int ques_id,String tagname);

    ServiceResult findTagsByQuestion(int ques_id);

}
