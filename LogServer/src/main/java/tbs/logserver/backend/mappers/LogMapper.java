package tbs.logserver.backend.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import tbs.api_server.objects.simple.LogPojo;

import java.util.List;

@Mapper
public interface LogMapper {

    @Insert("INSERT INTO `log` (`log_type`, `log_function`, `log_invoker`, `log_begin`, `log_end`, `log_return`, `log_params`, `log_error`) VALUES " +
            "(#{log_type}, #{log_function}, #{log_invoker}, #{log_begin}, #{log_end}, #{log_return},#{log_params}, #{log_error})")
    public int writeLog(LogPojo pojo);

    @Select("select  * from log where `${fied}` like '%${val}%' order by log_begin desc limit #{from},#{num}")
    List<LogPojo> select(int from,int num,String fied,String val);
}
