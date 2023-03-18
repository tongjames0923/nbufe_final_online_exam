package tbs.logserver.backend.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import tbs.api_server.objects.simple.LogPojo;

@Mapper
public interface LogMapper {

    @Insert("INSERT INTO `log` (`log_type`, `log_function`, `log_invoker`, `log_begin`, `log_end`, `log_return`, `log_params`, `log_error`) VALUES " +
            "(#{log_type}, #{log_function}, #{log_invoker}, #{log_begin}, #{log_end}, #{log_return},#{log_params}, #{log_error})")
    public int writeLog(LogPojo pojo);
}
