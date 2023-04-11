package tbs.api_server.services;

import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.compound.exam.ExamPost;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.utility.Error;

import java.util.Date;

public interface ExamService
{
    ServiceResult countExams(UserSecurityInfo user);
    ServiceResult listExamsForStudent(String number,String id,String name);
    ServiceResult getExamByStatus(int status, int from, int num) throws Error.BackendError;
    ServiceResult getExamByName(String name) throws Error.BackendError;
    ServiceResult uploadExam(int user, ExamPost data) throws Error.BackendError;
    ServiceResult deleteExam(int id,UserSecurityInfo useri) throws Error.BackendError;
    ServiceResult list(UserSecurityInfo user, int from, int num) throws Error.BackendError;

    ServiceResult updateStatus(int status, int examid) throws Error.BackendError;
    ServiceResult updateLen(int len, UserSecurityInfo user, int examid) throws Error.BackendError;
    ServiceResult updateNote(String note,UserSecurityInfo user,int examid) throws Error.BackendError;
    ServiceResult updateBegin(Date date,UserSecurityInfo user,int examid) throws Error.BackendError;
    ServiceResult updateName(String name ,UserSecurityInfo user,int examid) throws Error.BackendError;
    ServiceResult getExamByNote(String note,int from,int num)throws Error.BackendError;
    ServiceResult getExamBeforeTime(Date d,int from,int num)throws Error.BackendError;
    ServiceResult getFullExamInfoById(int exam_id,boolean fullUserList) throws Error.BackendError;
    ServiceResult updateScore(String exam,int qid,int score) throws Error.BackendError;
    ServiceResult StudentLogin(String uid,int exam) throws Error.BackendError;

}
