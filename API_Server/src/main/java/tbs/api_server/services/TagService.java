package tbs.api_server.services;

import tbs.api_server.objects.ServiceResult;
import tbs.api_server.utility.Error;

public interface TagService
{

    ServiceResult changeUsed(String tagname, int altervalue) throws Error.BackendError;


    ServiceResult newTag(String tagname) throws Error.BackendError;


    ServiceResult deleteTag(String tagname) throws Error.BackendError;


    ServiceResult listTags() throws Error.BackendError;


    ServiceResult linkTag(int ques_id,String tagname) throws Error.BackendError;


    ServiceResult unLinkTag(int ques_id,String tagname) throws Error.BackendError;

    ServiceResult unLinkTag(int ques_id) throws Error.BackendError;
    ServiceResult findTagsByQuestion(int ques_id) throws Error.BackendError;

}
