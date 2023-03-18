package tbs.api_server.objects.simple;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class LogPojo implements Serializable {
    private int log_id;
    private String log_type,log_function,log_invoker;
    @DateTimeFormat(pattern="yyyy年MM月dd日 hh:mm:ss.SSSS")
    @JsonFormat(pattern="yyyy年MM月dd日 hh:mm:ss.SSSSSS",timezone="GMT+8")
    private Date log_begin;
    @DateTimeFormat(pattern="yyyy年MM月dd日 hh:mm:ss.SSSS")
    @JsonFormat(pattern="yyyy年MM月dd日 hh:mm:ss.SSSSSS",timezone="GMT+8")
    private Date log_end;

    public String getLog_error() {
        return log_error;
    }

    public void setLog_error(String log_error) {
        this.log_error = log_error;
    }

    private String log_return,log_params,log_error;

    public LogPojo(int log_id, int log_cost, String log_type, String log_function, String log_invoker, Date log_begin, Date log_end, String log_return, String log_params) {
        this.log_id = log_id;
        this.log_type = log_type;
        this.log_function = log_function;
        this.log_invoker = log_invoker;
        this.log_begin = log_begin;
        this.log_end = log_end;
        this.log_return = log_return;
        this.log_params = log_params;
    }

    public LogPojo() {
    }

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }


    public String getLog_type() {
        return log_type;
    }

    public void setLog_type(String log_type) {
        this.log_type = log_type;
    }

    public String getLog_function() {
        return log_function;
    }

    public void setLog_function(String log_function) {
        this.log_function = log_function;
    }

    public String getLog_invoker() {
        return log_invoker;
    }

    public void setLog_invoker(String log_invoker) {
        this.log_invoker = log_invoker;
    }

    public Date getLog_begin() {
        return log_begin;
    }

    public void setLog_begin(Date log_begin) {
        this.log_begin = log_begin;
    }

    public Date getLog_end() {
        return log_end;
    }

    public void setLog_end(Date log_end) {
        this.log_end = log_end;
    }

    public String getLog_return() {
        return log_return;
    }

    public void setLog_return(String log_return) {
        this.log_return = log_return;
    }

    public String getLog_params() {
        return log_params;
    }

    public void setLog_params(String log_params) {
        this.log_params = log_params;
    }

    @Override
    public String toString() {
        return "LogPojo{" +
                "log_id=" + log_id +
                ", log_type='" + log_type + '\'' +
                ", log_function='" + log_function + '\'' +
                ", log_invoker='" + log_invoker + '\'' +
                ", log_begin=" + log_begin +
                ", log_end=" + log_end +
                ", log_return='" + log_return + '\'' +
                ", log_params='" + log_params + '\'' +
                ", log_error='" + log_error + '\'' +
                '}';
    }
}
