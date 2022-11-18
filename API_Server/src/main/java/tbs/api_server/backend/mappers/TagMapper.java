package tbs.api_server.backend.mappers;

import org.apache.ibatis.annotations.*;
import tbs.api_server.objects.simple.Tag;

import java.util.List;

@Mapper
public interface TagMapper
{
    @Select("select * from tag FOR UPDATE")
    List<Tag> getAllTags();

    @Select("select * from `tag` where `tag_name`=#{name} FOR UPDATE")
    Tag getTagByName(String name);

    @Select("select * from `tag` where `id`=#{id} FOR UPDATE")
    Tag getTagById(int id);

    @Delete("delete from `tag` where `tag_name`=#{name}")
    int deleteTagByName(String name);

    @Update("UPDATE `tag` SET `tag_used` = #{cnt} WHERE `tag_id` = #{id}")
    int setUsed(int id, int cnt);

    @Insert("INSERT INTO `tag` ( `tag_name`)\n" +
            "VALUES\n" +
            "\t(#{tag});\n")
    int insertTag(String tag);


    @Select("SELECT * FROM `tag` WHERE `ques_id`=#{ques_id} FOR UPDATE")
    List<Tag> findTagsByQuestion(int ques);


    @Insert("INSERT INTO `tag_link`(`tag_id`,`ques_id`) VALUES\n" +
    "(#{tagid},#{ques_id})\n" )
    int linkTag(int ques_id,int tagid);

    @Delete("delete from `tag_link` where `tag_id`=#{tagid} and `ques_id`=#{ques_id}")
    int unLinkTag(int ques_id,int tagid);

}
