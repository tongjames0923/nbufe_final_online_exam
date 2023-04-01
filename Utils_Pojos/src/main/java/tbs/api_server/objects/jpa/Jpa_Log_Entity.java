//package tbs.api_server.objects.jpa;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.sql.Timestamp;
//
//@Entity
//@Table(name = "log", schema = "exam_db", catalog = "")
//public class Jpa_Log_Entity implements Serializable {
//    private int logId;
//    private String logType;
//    private String logFunction;
//    private String logInvoker;
//    private Timestamp logBegin;
//    private int cost;
//    private String logReturn;
//    private String logParams;
//    private String logError;
//
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Id
//    @Column(name = "log_id", nullable = false)
//    public int getLogId() {
//        return logId;
//    }
//
//    public void setLogId(int logId) {
//        this.logId = logId;
//    }
//
//    @Basic
//    @Column(name = "log_type", nullable = false, length = 255)
//    public String getLogType() {
//        return logType;
//    }
//
//    public void setLogType(String logType) {
//        this.logType = logType;
//    }
//
//    @Basic
//    @Column(name = "log_function", nullable = false, length = 255)
//    public String getLogFunction() {
//        return logFunction;
//    }
//
//    public void setLogFunction(String logFunction) {
//        this.logFunction = logFunction;
//    }
//
//    @Basic
//    @Column(name = "log_invoker", nullable = false, length = 512)
//    public String getLogInvoker() {
//        return logInvoker;
//    }
//
//    public void setLogInvoker(String logInvoker) {
//        this.logInvoker = logInvoker;
//    }
//
//    @Basic
//    @Column(name = "log_begin", nullable = false)
//    @DateTimeFormat(pattern="yyyy年MM月dd日 hh:mm:ss")
//    @JsonFormat(pattern="yyyy年MM月dd日 hh:mm:ss",timezone="GMT+8")
//    public Timestamp getLogBegin() {
//        return logBegin;
//    }
//
//    public void setLogBegin(Timestamp logBegin) {
//        this.logBegin = logBegin;
//    }
//
//    @Basic
//    @Column(name = "cost", nullable = false)
//    public int getCost() {
//        return cost;
//    }
//
//    public void setCost(int cost) {
//        this.cost = cost;
//    }
//
//    @Basic
//    @Column(name = "log_return", nullable = true, length = -1)
//    public String getLogReturn() {
//        return logReturn;
//    }
//
//    public void setLogReturn(String logReturn) {
//        this.logReturn = logReturn;
//    }
//
//    @Basic
//    @Column(name = "log_params", nullable = true, length = -1)
//    public String getLogParams() {
//        return logParams;
//    }
//
//    public void setLogParams(String logParams) {
//        this.logParams = logParams;
//    }
//
//    @Basic
//    @Column(name = "log_error", nullable = true, length = -1)
//    public String getLogError() {
//        return logError;
//    }
//
//    public void setLogError(String logError) {
//        this.logError = logError;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Jpa_Log_Entity that = (Jpa_Log_Entity) o;
//
//        if (logId != that.logId) return false;
//        if (cost != that.cost) return false;
//        if (logType != null ? !logType.equals(that.logType) : that.logType != null) return false;
//        if (logFunction != null ? !logFunction.equals(that.logFunction) : that.logFunction != null) return false;
//        if (logInvoker != null ? !logInvoker.equals(that.logInvoker) : that.logInvoker != null) return false;
//        if (logBegin != null ? !logBegin.equals(that.logBegin) : that.logBegin != null) return false;
//        if (logReturn != null ? !logReturn.equals(that.logReturn) : that.logReturn != null) return false;
//        if (logParams != null ? !logParams.equals(that.logParams) : that.logParams != null) return false;
//        if (logError != null ? !logError.equals(that.logError) : that.logError != null) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = logId;
//        result = 31 * result + (logType != null ? logType.hashCode() : 0);
//        result = 31 * result + (logFunction != null ? logFunction.hashCode() : 0);
//        result = 31 * result + (logInvoker != null ? logInvoker.hashCode() : 0);
//        result = 31 * result + (logBegin != null ? logBegin.hashCode() : 0);
//        result = 31 * result + cost;
//        result = 31 * result + (logReturn != null ? logReturn.hashCode() : 0);
//        result = 31 * result + (logParams != null ? logParams.hashCode() : 0);
//        result = 31 * result + (logError != null ? logError.hashCode() : 0);
//        return result;
//    }
//}
