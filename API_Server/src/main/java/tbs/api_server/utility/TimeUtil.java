package tbs.api_server.utility;

import tbs.api_server.objects.simple.ExamInfo;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static Date appendMin(Date d,int min)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.MINUTE,min);
        Date ed = calendar.getTime();
        return ed;
    }
    public static boolean isStart(ExamInfo info)
    {
        Date date=new Date();
        return info.getExam_begin().after(date);
    }
    public static boolean isClosed(ExamInfo info)
    {
        Date now=new Date();
      Date d=  appendMin(info.getExam_begin(),info.getExam_len());
      return now.after(d);
    }


}