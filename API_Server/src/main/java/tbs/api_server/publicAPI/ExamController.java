package tbs.api_server.publicAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tbs.api_server.config.NoNeedAccess;
import tbs.api_server.config.constant.const_Exam;
import tbs.api_server.config.constant.const_User;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.compound.exam.ExamPost;
import tbs.api_server.objects.simple.ExamInfo;
import tbs.api_server.objects.simple.ExamPermission;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.services.ExamPermissionService;
import tbs.api_server.services.ExamService;
import tbs.api_server.utility.ApiMethod;
import tbs.api_server.utility.Error;
import tbs.api_server.utils.TimeUtil;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static tbs.api_server.utility.Error.SUCCESS;

@RestController
@RequestMapping("exam/*")
public class ExamController {
    @Autowired
    ExamService service;
    @Resource
    ExamPermissionService permissionService;
    private void update(UserSecurityInfo user,List<ExamInfo> list) throws Error.BackendError {

        Iterator<ExamInfo> iter= list.iterator();
        while (iter.hasNext())
        {
            ExamInfo info=iter.next();
            try {
                if(user.getLevel()== const_User.LEVEL_EXAM_STAFF)
                {
                    info.setCheckable(1);
                    info.setReadable(1);
                    info.setWriteable(1);
                }
                else
                {
                    ExamPermission permission= (ExamPermission) permissionService.getPermission(info.getExam_id(),user.getId()).getObj();
                    if(permission!=null)
                    {
                        info.setWriteable(permission.getWriteable());
                        info.setCheckable(permission.getCheckable());
                        info.setReadable(permission.getReadable());
                    }
                }
                if(info.getExam_status()>const_Exam.EXAM_STATUS_CLOSED)
                    continue;
                if(info.getExam_status()<const_Exam.EXAM_STATUS_CLOSED&&TimeUtil.isClosed(info))
                {
                    service.updateStatus(const_Exam.EXAM_STATUS_CLOSED, info.getExam_id());
                    info.setExam_status(const_Exam.EXAM_STATUS_CLOSED);
                    continue;
                }
                if(info.getExam_status()==const_Exam.EXAM_STATUS_WAIT&&TimeUtil.isStart(info))
                {
                    service.updateStatus(const_Exam.EXAM_STATUS_START, info.getExam_id());
                    info.setExam_status(const_Exam.EXAM_STATUS_START);
                }
            }catch (Exception e)
            {

            }

            if(info.getReadable()==0)
                iter.remove();
        }
    }

    @RequestMapping("count")
    public NetResult count()
    {
        return  ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                return NetResult.makeResult(service.countExams(applyUser),null);
            }
        })
                .methodWithLogined();
    }

    @RequestMapping("byStatus")
    @Transactional
    public NetResult listStatus(int status, int from, int num) {

        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Exception {

                List<ExamInfo> list = (List<ExamInfo>) service.list(applyUser,from, num).getObj();
                update(applyUser,list);
                ServiceResult result = service.getExamByStatus(status, from, num);
                return NetResult.makeResult(result, null);
            }
        }).methodWithLogined();
    }
    @RequestMapping("updateLen")
    @Transactional
    public NetResult updateLen(int len,  int examid) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Exception {

                return NetResult.makeResult(service.updateLen(len, applyUser, examid), null);
            }
        }).methodWithLogined();
    }
    @RequestMapping("updateName")
    @Transactional
    public NetResult updateName(String name,  int examid) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Exception {

                return NetResult.makeResult(service.updateName(name, applyUser, examid), null);
            }
        }).methodWithLogined();
    }

    @RequestMapping("list")
    public NetResult listMyExam(int from,int num)
    {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                return NetResult.makeResult(service.list(applyUser,from,num),null);
            }
        });
    }


    @RequestMapping("updateNote")
    @Transactional
    public NetResult updateNote(String note,int examid) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Exception {

                return NetResult.makeResult(service.updateNote(note, applyUser, examid), null);
            }
        }).methodWithLogined();
    }
    @RequestMapping("updateBegin")
    @Transactional
    public NetResult updateBegin(String time, int examid) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Exception {
                Date d=new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(time).getTime());
                return NetResult.makeResult(service.updateBegin(d,applyUser, examid), null);
            }
        }).methodWithLogined();
    }
    @RequestMapping("get")
    @Transactional
    public NetResult getExamByName(String name) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Exception {

                return NetResult.makeResult(service.getExamByName(name), null);
            }
        }).methodWithLogined();
    }
    @RequestMapping(value = "upload",method = RequestMethod.POST)
    @Transactional
    public NetResult upload(int user,@RequestBody ExamPost exam) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Exception {
                return NetResult.makeResult(service.uploadExam(user, exam),null);
            }
        }).methodWithLogined();
    }


    @NoNeedAccess
    @RequestMapping("getFull")
    public NetResult getfull(int id)
    {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                return  NetResult.makeResult(service.getFullExamInfoById(id),null);
            }
        }).methodWithLogined();
    }


    @RequestMapping("listExamForStudent")
    @NoNeedAccess
    public NetResult listExam(String name,String number,String id)
    {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                return NetResult.makeResult(service.listExamsForStudent(number,id,name),null);
            }
        });
    }


    @RequestMapping("studentLogin")
    @NoNeedAccess
    public NetResult studentLogin(String name,String number,String id,int examID)
    {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                return NetResult.makeResult(service.StudentLogin(name,id,number,examID),null);
            }
        }).methodWithLogined();
    }


    @RequestMapping("updateScore")
    public NetResult updateScore(int qid,String exam,int score)
    {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Error.BackendError, Exception {
                return NetResult.makeResult(service.updateScore(exam,qid,score),null);
            }
        });
    }


    @RequestMapping("delete")
    @Transactional
    public NetResult delete(int id) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Exception {
                return NetResult.makeResult(service.deleteExam(id, applyUser), null);
            }
        }).methodWithLogined();
    }
    @RequestMapping("bytime")
    @Transactional
    public NetResult findByTime(Date date,int from,int num)
    {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Exception {
                List<ExamInfo> list = (List<ExamInfo>) service.getExamBeforeTime(date,from,num).getObj();
                update(applyUser,list);
                return NetResult.makeResult(SUCCESS, null, list);
            }
        }).methodWithLogined();
    }
    @RequestMapping("bynote")
    @Transactional
    public NetResult findByNote(String note,int from,int num)
    {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws Exception {
                List<ExamInfo> list = (List<ExamInfo>) service.getExamByNote(note,from,num).getObj();
                update(applyUser,list);
                return NetResult.makeResult(SUCCESS, null, list);
            }
        }).methodWithLogined();
    }
}
