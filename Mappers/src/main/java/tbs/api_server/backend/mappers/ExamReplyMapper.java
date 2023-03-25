package tbs.api_server.backend.mappers;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.lang.NonNull;
import tbs.api_server.objects.simple.ExamReply;

import java.util.List;

@Mapper
public interface ExamReplyMapper
{
    @Insert("INSERT INTO `exam_reply` (`exam_id`,`exam_number`,`person_id`,`reply_file`) VALUES(" +
            "#{param1},#{param2},#{param3},#{param4})")
    int insertReply(int exam_id, @NonNull String examnumber,@NonNull String person_id,@NonNull String reply_file);

    @Select("SELECT * FROM `exam_reply` WHERE `exam_id`=#{param1} FOR UPDATE")
    List<ExamReply> getReplys(int examid);

    @Update("UPDATE `exam_reply` SET `status`=#{status} where `exam_number`=#{examnumber}")
    int updateReplysStatus(String examnumber,int status);

    @Select("SELECT * FROM `exam_reply` WHERE `exam_number`=#{number} FOR UPDATE")
    ExamReply getExamReply(String number);

    @Update("UPDATE `exam_reply` SET `status`=#{status},`check_file`=#{check_file} where `exam_number`=#{examnumber}")
    int updateReplyCheck(String examnumber,String check_file,int status);
}