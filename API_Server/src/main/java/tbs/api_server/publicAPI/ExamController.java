package tbs.api_server.publicAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sun.nio.ch.Net;
import tbs.api_server.config.constant.const_Exam;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.compound.exam.ExamPost;
import tbs.api_server.objects.simple.ExamInfo;
import tbs.api_server.services.ExamService;
import tbs.api_server.utility.ApiMethod;
import tbs.api_server.utility.Error;
import tbs.api_server.utility.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static tbs.api_server.utility.Error.SUCCESS;

@RestController
@RequestMapping("exam/*")
@Scope("prototype")
public class ExamController {
    @Autowired
    ExamService service;

    private void update(List<ExamInfo> list) throws Error.BackendError {


        for (ExamInfo info : list) {
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
        }
    }



    @RequestMapping("list")
    @Transactional
    public NetResult list(int from, int num) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action() throws Exception {

                List<ExamInfo> list = (List<ExamInfo>) service.list(from, num).getObj();
                update(list);
                return NetResult.makeResult(SUCCESS, null, list);
            }
        }).method();
    }


    @RequestMapping("count")
    public NetResult count(int user)
    {
        return  ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action() throws Error.BackendError, Exception {
                return NetResult.makeResult(service.countExams(user),null);
            }
        })
                .method();
    }

    @RequestMapping("byStatus")
    @Transactional
    public NetResult listStatus(int status, int from, int num) {

        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action() throws Exception {

                List<ExamInfo> list = (List<ExamInfo>) service.list(from, num).getObj();
                update(list);

                ServiceResult result = service.getExamByStatus(status, from, num);
                return NetResult.makeResult(result, null);
            }
        }).method();
    }
    @RequestMapping("updateLen")
    @Transactional
    public NetResult updateLen(int len, int user, int examid) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action() throws Exception {

                return NetResult.makeResult(service.updateLen(len, user, examid), null);
            }
        }).method();
    }
    @RequestMapping("updateName")
    @Transactional
    public NetResult updateName(String name, int user, int examid) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action() throws Exception {

                return NetResult.makeResult(service.updateName(name, user, examid), null);
            }
        }).method();
    }
    @RequestMapping("updateNote")
    @Transactional
    public NetResult updateNote(String note, int user, int examid) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action() throws Exception {

                return NetResult.makeResult(service.updateNote(note, user, examid), null);
            }
        }).method();
    }
    @RequestMapping("updateBegin")
    @Transactional
    public NetResult updateBegin(String time, int user, int examid) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action() throws Exception {
                Date d=new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(time).getTime());
                return NetResult.makeResult(service.updateBegin(d, user, examid), null);
            }
        }).method();
    }
    @RequestMapping("get")
    @Transactional
    public NetResult getExamByName(String name) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action() throws Exception {

                return NetResult.makeResult(service.getExamByName(name), null);
            }
        }).method();
    }
    @RequestMapping(value = "upload",method = RequestMethod.POST)
    @Transactional
    public NetResult upload(int user,@RequestBody ExamPost exam) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action() throws Exception {
                return NetResult.makeResult(service.uploadExam(user, exam),null);
            }
        }).method();
    }


    @RequestMapping("getFull")
    public NetResult getfull(int id)
    {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action() throws Error.BackendError, Exception {
                return  NetResult.makeResult(service.getFullExamInfoById(id),null);
            }
        }).method();
    }

    @RequestMapping("studentLogin")
    public NetResult studentLogin(String name,String number,String id,int examID)
    {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action() throws Error.BackendError, Exception {
                return NetResult.makeResult(service.StudentLogin(name,id,number,examID),null);
            }
        }).method();
    }


    @RequestMapping("delete")
    @Transactional
    public NetResult delete(int id, int user) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action() throws Exception {
                return NetResult.makeResult(service.deleteExam(id, user), null);
            }
        }).method();
    }
    @RequestMapping("bytime")
    @Transactional
    public NetResult findByTime(Date date,int from,int num)
    {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action() throws Exception {
                List<ExamInfo> list = (List<ExamInfo>) service.getExamBeforeTime(date,from,num).getObj();
                update(list);
                return NetResult.makeResult(SUCCESS, null, list);
            }
        }).method();
    }
    @RequestMapping("bynote")
    @Transactional
    public NetResult findByNote(String note,int from,int num)
    {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action() throws Exception {
                List<ExamInfo> list = (List<ExamInfo>) service.getExamByNote(note,from,num).getObj();
                update(list);
                return NetResult.makeResult(SUCCESS, null, list);
            }
        }).method();
    }
}
