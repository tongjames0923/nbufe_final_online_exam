package tbs.api_server.backend.mappers;

import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import tbs.api_server.objects.simple.ExamInfo;

import java.util.Date;
import java.util.List;

@Mapper
public interface ExamMapper {
    @Select("Select `exam_id`,`exam_name`,`exam_begin`,`exam_len`,`exam_note`,`exam_status` from " +
            "`exam` where `status`=#{status} limit #{from},#{num} FOR UPDATE")
    List<ExamInfo> getExamsByStatus(int status, int from, int num);

    @Select("Select `exam_id`,`exam_name`,`exam_begin`,`exam_len`,`exam_note`,`exam_status` from " +
            "`exam` where `exam_id`=#{id} FOR UPDATE")
    @Cacheable(value = "examinfo",key = "#id")
    ExamInfo getExamByid(int id);

    @Select("Select `exam_id`,`exam_name`,`exam_begin`,`exam_len`,`exam_note`,`exam_status` from " +
            "`exam` WHERE `exam_note` LIKE %${note}% limit #{from},#{num} FOR UPDATE")
    List<ExamInfo> findByNote(String note,int from,int num);


    @Select("Select `exam_id`,`exam_name`,`exam_begin`,`exam_len`,`exam_note`,`exam_status` from " +
            "`exam` WHERE `exam_begin`<#{before} limit #{from},#{num} FOR UPDATE")
    List<ExamInfo> findByTime(Date before,int from,int num);



    @Select("Select `exam_id`,`exam_name`,`exam_begin`,`exam_len`,`exam_note`,`exam_status` from " +
            "`exam` limit #{from},#{num} FOR UPDATE")
    List<ExamInfo> list(int from, int num);

    @Select("SELECT `exam_file` FROM `exam` where `exam_id`=#{exam_id} FOR UPDATE")
    @Cacheable(value = "examFile",key = "#exam_id")
    String getExamFile(int exam_id);

    @Select("Select `exam_id`,`exam_name`,`exam_begin`,`exam_len`,`exam_note`,`exam_status` from" +
            " `exam` where `exam_name`=#{name} FOR UPDATE")
    ExamInfo getExamIDByExamName(String name);

    @Insert("INSERT INTO `exam`(`exam_name`,`exam_begin`,`exam_len`,`exam_note`,`exam_file`) VALUES (" +
            "#{name},#{beg},#{len},#{note},#{file})")
    int uploadExam(String name, Date beg, String note, byte[] file, Integer len);

    @Delete("DELETE FROM `exam` WHERE `exam_id`=#{exam_id}")
    @Caching(evict = {
            @CacheEvict(value = "examFile",key = "#exam_id"),
            @CacheEvict(value = "examinfo",key = "#exam_id")
    })
    int deleteExam(int exam_id);

    @Caching(evict = {
            @CacheEvict(value = "examFile",key = "#exam_id"),
            @CacheEvict(value = "examinfo",key = "#exam_id")
    })
    @Update("UPDATE `exam` SET `${field}`=#{value} WHERE `exam_id`=#{exam_id}")
    int updateExam(int exam_id, String field, Object value);


    @Select("SELECT COUNT(*) FROM exam;")
    int countStaff();
}
