package tbs.api_server.publicAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tbs.api_server.config.constant.const_Question;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.services.QuestionService;
import tbs.api_server.utility.Error;

import static tbs.api_server.utility.Error.*;

@RestController
@RequestMapping("/question/*")
@Scope("prototype")
public class QuestionController {
    @Autowired
    QuestionService service;
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @Transactional
    public NetResult uploadQuestion(@RequestParam int type, @RequestParam int creator, @RequestParam String title, @RequestParam MultipartFile file, @RequestParam(required = false) Integer isopen, @RequestParam(required = false) Integer tag) {
        try {
            ServiceResult result = service.uploadQuestion(type, title, creator, file.getBytes(), isopen, tag);
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

        try {
            ServiceResult result = service.deleteQuestion(ques, user);
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
    @RequestMapping(value = "find", method = RequestMethod.POST)
    public NetResult find(int type, int[] codes, int from, int num) {
        ServiceResult result = null;
        try {
            final int TAG = 1, TYPE = 2;
            if (type == TAG) {
                result = service.findQuestionsByTags(codes, from, num);
            } else if (TYPE == type) {
                result = service.findQuestionsByType(codes, from, num);
            } else {
                return NetResult.makeResult(EC_InvalidParameter, "不存在此类型");
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
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public NetResult serach(String title, int from, int num) {
        try {
            ServiceResult serviceResult = service.findQuestionsByTitle(title, from, num);
            return NetResult.makeResult(serviceResult, null);

        } catch (Error.BackendError e) {
            _ERROR.rollback();
            return NetResult.makeResult(e.getCode(), e.getMessage());
        } catch (Exception ex) {
            _ERROR.rollback();
            return NetResult.makeResult(EC_UNKNOWN, ex.getMessage());
        }
    }

    @Transactional
    @RequestMapping("list")
    public NetResult list(int from, int num) {
        try {
            return NetResult.makeResult(service.listQuestions(from, num), null);
        } catch (Error.BackendError e) {
            _ERROR.rollback();
            return NetResult.makeResult(e.getCode(), e.getMessage());
        } catch (Exception ex) {
            _ERROR.rollback();
            return NetResult.makeResult(EC_UNKNOWN, ex.getMessage());
        }
    }

    @Transactional
    @RequestMapping(value = "updateFile", method = RequestMethod.POST)
    public NetResult updateFile(int id, MultipartFile file) {
        try {
            ServiceResult result = service.updateQuestionValue(id, const_Question.col_file, file.getBytes());
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
    @RequestMapping("updatePublic")
    public NetResult updatePublic(int ques, int publicable) {
        try {
            ServiceResult result = service.updateQuestionValue(ques, const_Question.col_publicable, publicable);
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
    @RequestMapping(value = "updateTitle", method = RequestMethod.POST)
    public NetResult updateTitle(int ques, String title) {
        try {
            ServiceResult result = service.updateQuestionValue(ques, const_Question.col_title, title);
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
    @RequestMapping("updateType")
    public NetResult updateType(int ques, int type) {
        try {
            ServiceResult result = service.updateQuestionValue(ques, const_Question.col_type, type);
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
    @RequestMapping("questionCount")
    public NetResult getCount() {
        try {
            ServiceResult result = service.questionsLength();
            return NetResult.makeResult(result, null);

        } catch (Error.BackendError e) {
            _ERROR.rollback();
            return NetResult.makeResult(e.getCode(), e.getMessage());
        } catch (Exception ex) {
            _ERROR.rollback();
            return NetResult.makeResult(EC_UNKNOWN, ex.getMessage());
        }
    }

}
