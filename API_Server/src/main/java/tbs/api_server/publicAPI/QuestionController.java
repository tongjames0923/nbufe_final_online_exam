package tbs.api_server.publicAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tbs.api_server.config.AccessLimit;
import tbs.api_server.config.constant.const_Question;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.Tag;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.services.QuestionService;
import tbs.api_server.services.TagService;
import tbs.api_server.utility.ApiMethod;
import tbs.api_server.utility.Error;

import java.util.List;

import static tbs.api_server.utility.Error.*;

@RestController
@RequestMapping("/question/*")
public class QuestionController {
    @Autowired
    QuestionService service;
    @Autowired
    TagService tagService;



    @RequestMapping(value = "updateTags",method = RequestMethod.POST)
    public NetResult updateTags(
            int ques,@RequestBody List<Tag> tags)
    {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                if(tags==null)
                    throw _ERROR.throwError(EC_InvalidParameter,"参数空错误");
                int[] arr=new int[tags.size()];
                int i=0;
                for(Tag t:tags)
                {
                    arr[i++]=t.getTag_id();
                }
                return NetResult.makeResult(service.updateTags(arr,ques),null);
            }
        });
    }




    @RequestMapping(value = "create", method = RequestMethod.POST)
    @Transactional
    public NetResult uploadQuestion(@RequestParam int type,@RequestParam int creator,@RequestParam String title,
                                    @RequestParam MultipartFile md,@RequestParam Integer isopen
            , @RequestParam List<String>  tags,@RequestParam String answer) {
        try {
            ServiceResult result = service.uploadQuestion(type, title, creator, md.getBytes(), isopen,answer);
            int id= (int) result.getObj();
            for (String ch:tags)
            {
                try {
                    tagService.linkTag(id,ch);
                }catch (Exception ex)
                {

                }
            }
            return NetResult.makeResult(result, null);
        } catch (Error.BackendError e) {
            _ERROR.rollback();
            return NetResult.makeResult(e.getCode(), e.getMessage());
        } catch (Exception ex) {
            _ERROR.rollback();
            return NetResult.makeResult(EC_UNKNOWN, ex.getMessage());
        }
    }

    @Transactional
    @RequestMapping("delete")
    public NetResult delete(int ques, int user) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                ServiceResult result = service.deleteQuestion(ques, user);
                return NetResult.makeResult(result, null);
            }
        }).methodWithLogined();
    }


    @Transactional
    @RequestMapping(value = "find", method = RequestMethod.POST)
    public NetResult find(int type, int[] codes, int from, int num) {

        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                ServiceResult result = null;
                final int TAG = 1, TYPE = 2;
                if (type == TAG) {
                    result = service.findQuestionsByTags(codes, from, num);
                } else if (TYPE == type) {
                    result = service.findQuestionsByType(codes, from, num);
                } else {
                    return NetResult.makeResult(EC_InvalidParameter, "不存在此类型");
                }
                return NetResult.makeResult(result, null);
            }
        }).methodWithLogined();
    }

    @Transactional
    @RequestMapping(value = "search")
    public NetResult serach(String title, int from, int num) {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                ServiceResult serviceResult = service.findQuestionsByTitle(title, from, num);
                return NetResult.makeResult(serviceResult, null);
            }
        });
    }
    @Transactional
    @RequestMapping("list")
    public NetResult list(int from, int num) {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                return NetResult.makeResult(service.listQuestions(from, num), null);
            }
        });
    }


    @RequestMapping("findByid")
    @Transactional
    public  NetResult findById(int id)
    {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                return NetResult.makeResult(service.findQuestionsByID(id),null);
            }
        }).methodWithLogined();
    }
    @RequestMapping("findByTag")
    @Transactional
    public NetResult findByTag(String tag)  {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                return NetResult.makeResult(service.findQuestionsByTag(tag),null);
            }
        }).methodWithLogined();
    }


//    @Transactional
//    @RequestMapping(value = "updateFile", method = RequestMethod.POST)
//    public NetResult updateFile(int id, MultipartFile file) {
//        try {
//            ServiceResult result = service.updateQuestionValue(id, const_Question.col_file, file.getBytes());
//            return NetResult.makeResult(result, null);
//        } catch (Error.BackendError e) {
//            _ERROR.rollback();
//            return NetResult.makeResult(e.getCode(), e.getMessage());
//        } catch (Exception ex) {
//            _ERROR.rollback();
//            return NetResult.makeResult(EC_UNKNOWN, ex.getMessage());
//        }
//    }

    @Transactional
    @RequestMapping("updatePublic")
    public NetResult updatePublic(int ques, int publicable) {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                ServiceResult result = service.updateQuestionValue(ques, const_Question.col_publicable, publicable);
                return NetResult.makeResult(result, null);
            }
        });
    }

    @Transactional
    @RequestMapping(value = "updateTitle")
    public NetResult updateTitle(int ques, String title) {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                ServiceResult result = service.updateQuestionValue(ques, const_Question.col_title, title);
                return NetResult.makeResult(result, null);
            }
        });
    }

    @Transactional
    @RequestMapping("updateType")
    public NetResult updateType(int ques, int type) {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                ServiceResult result = service.updateQuestionValue(ques, const_Question.col_type, type);
                return NetResult.makeResult(result, null);
            }
        });
    }
    @RequestMapping("questionCount")
    public NetResult getCount() {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                ServiceResult result = service.questionsLength(applyUser);
                return NetResult.makeResult(result, null);
            }
        }).methodWithLogined();
    }

}
