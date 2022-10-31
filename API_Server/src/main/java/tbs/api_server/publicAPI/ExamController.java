package tbs.api_server.publicAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tbs.api_server.config.constant.const_Exam;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.simple.ExamInfo;
import tbs.api_server.services.ExamService;
import tbs.api_server.utility.ApiMethod;
import tbs.api_server.utility.Error;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static tbs.api_server.utility.Error.SUCCESS;

@RestController
@RequestMapping("exam/*")
public class ExamController
{
    @Autowired
    ExamService service;


    public NetResult list(int from, int num)
    {
        return ApiMethod.make(new ApiMethod.IAction()
        {
            @Override
            public NetResult action() throws Error.BackendError, Exception
            {
                Date now = new Date();
                List<ExamInfo> list = (List<ExamInfo>) service.list(from, num).getObj();
                for (ExamInfo info : list)
                {
                    try
                    {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(info.getExam_begin());
                        calendar.add(Calendar.MINUTE, info.getExam_len());
                        Date ed = calendar.getTime();
                        if (now.after(
                                ed) && (info.getExam_status() == const_Exam.EXAM_STATUS_WAIT || info.getExam_status() == const_Exam.EXAM_STATUS_START))
                        {
                            service.updateStatus(const_Exam.EXAM_STATUS_CLOSED, info.getExam_id());
                            info.setExam_status(const_Exam.EXAM_STATUS_CLOSED);
                        } else if (now.after(
                                info.getExam_begin()) && info.getExam_status() == const_Exam.EXAM_STATUS_WAIT)
                        {
                            service.updateStatus(const_Exam.EXAM_STATUS_START, info.getExam_id());
                            info.setExam_status(const_Exam.EXAM_STATUS_START);
                        }
                    } catch (Exception e)
                    {
                    }
                }
                return NetResult.makeResult(SUCCESS,null,list);
            }
        }).method();
    }

}
