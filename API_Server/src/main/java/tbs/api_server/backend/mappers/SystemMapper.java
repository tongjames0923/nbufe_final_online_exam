package tbs.api_server.backend.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SystemMapper {
    @Select("SELECT LAST_INSERT_ID()")
    int getLastId();
}
