package tbs.api_server.backend.mappers;

import org.apache.ibatis.annotations.*;
import tbs.api_server.objects.simple.ExamPermission;

@Mapper
public interface ExamPermissionMapper
{

    @Select("Select * from `per_exam` where `exam_id` = #{exam_id} and `id`=#{userid}")
    ExamPermission getPermission(int userid,int exam_id);

    @Insert("INSERT INTO `per_exam`(`user`,`exam_id`,`readable`,`writeable`,`checkable`) VALUES(" +
            "#{param1},#{param2},#{param3},#{param4},#{param5}) ")
    int putPermission(int userid,int exam_id, Integer readable,Integer writable,Integer checkable);

    @Update("Update `per_exam` set `readable`=#{readable} where `user`=#{userid} and `exam_id`=#{exam_id}")
    int updatePermissionAtReadable(int userid,int exam_id,int readable);
    @Update("Update `per_exam` set `writeable`=#{writeable} where `user`=#{userid} and `exam_id`=#{exam_id}")
    int updatePermissionAtWriteable(int userid,int exam_id,int writeable);
    @Update("Update `per_exam` set `checkable`=#{checkable} where `user`=#{userid} and `exam_id`=#{exam_id}")
    int updatePermissionAtCheckable(int userid,int exam_id,int checkable);
    @Delete("Delete from `per_exam` where `user`=#{userid} and `exam_id`=#{exam_id}")
    int deletePermission(int userid,int exam_id);

}
