package tbs.api_server.publicAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.services.TagService;
import tbs.api_server.utility.ApiMethod;
import tbs.api_server.utility.Error;

@RestController
@RequestMapping("tag/*")
public class TagController {

    @Autowired
    TagService service;


    @Transactional
    @RequestMapping("list")
    public NetResult list() {


        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                ServiceResult result = service.listTags();
                return NetResult.makeResult(result, null);
            }
        }).methodWithLogined();
    }

    @Transactional
    @RequestMapping("getQuesTag")
    public NetResult getTagsByQuestion(int ques) {

        return  ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                ServiceResult result = service.findTagsByQuestion(ques);
                return NetResult.makeResult(result, null);
            }
        }).methodWithLogined();
    }

    @Transactional
    @RequestMapping("add")
    public NetResult addTag(String tag) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                ServiceResult result = service.newTag(tag);
                return NetResult.makeResult(result, null);
            }
        }).methodWithLogined();
    }

    @Transactional
    @RequestMapping("remove")
    public NetResult removeTag(String tag) {

        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                ServiceResult result = service.deleteTag(tag);
                return NetResult.makeResult(result, null);
            }
        }).methodWithLogined();
    }

    @Transactional
    @RequestMapping("link")
    public NetResult linkTag(String tag, int ques) {

        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                ServiceResult result = service.linkTag(ques, tag);
                return NetResult.makeResult(result, null);
            }
        }).methodWithLogined();

    }

    @Transactional
    @RequestMapping("unlink")
    public NetResult unLinkTag(String tag, int ques) {

        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                ServiceResult result = service.unLinkTag(ques, tag);
                return NetResult.makeResult(result, null);
            }
        }).methodWithLogined();
    }

}
