package tbs.logserver.backend.mappers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tbs.api_server.objects.simple.LogPojo;
import tbs.api_server.objects.simple.LogVo;

import java.util.List;

@Repository
public interface LogMapper extends CrudRepository<LogPojo,Integer>, JpaRepository<LogPojo,Integer>, JpaSpecificationExecutor<LogPojo> {


    @Query(nativeQuery = true,value = "SELECT cast(AVG(ll.cost) as double)as avg_cost,lg.log_function as `function` FROM log lg JOIN log ll ON ll.log_function=lg.log_function GROUP BY lg.log_function ORDER BY avg_cost DESC limit 0,:num")
    List<LogVo> listTopCostFunction(int num);

}
