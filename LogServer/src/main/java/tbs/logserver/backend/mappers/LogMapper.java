package tbs.logserver.backend.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import tbs.api_server.objects.simple.LogPojo;
import tbs.api_server.objects.simple.LogVo;

import java.util.List;

@Mapper
public interface LogMapper {

    @Insert("INSERT INTO `log` (`log_type`, `log_function`, `log_invoker`, `log_begin`, `cost`, `log_return`, `log_params`, `log_error`) VALUES " +
            "(#{log_type}, #{log_function}, #{log_invoker}, #{log_begin}, #{cost}, #{log_return},#{log_params}, #{log_error})")
    public int writeLog(LogPojo pojo);


    @Select("SELECT AVG(lg.cost) as avg_cost,log_function as `function`  FROM log lg GROUP BY lg.log_function ORDER BY avg_cost DESC LIMIT 0,#{num}")
    List<LogVo> listTopCostFunction(int num);

    @Select("select  * from log where `${fied}` like '${val}%' order by log_begin desc limit #{from},#{num}")
    List<LogPojo> select(int from,int num,String fied,String val);
}
