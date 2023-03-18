package tbs.api_server.backend.mappers;

import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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





    @Insert("INSERT INTO `tag_link`(`tag_id`,`ques_id`) VALUES\n" +
    "(#{tagid},#{ques_id})\n" )
    @CacheEvict(value = "quesTags",key = "#ques_id")
    int linkTag(int ques_id,int tagid);

    @Delete("delete from `tag_link` where `tag_id`=#{tagid} and `ques_id`=#{ques_id}")
    @CacheEvict(value = "quesTags",key = "#ques_id")
    int unLinkTag(int ques_id,int tagid);


    @Delete("delete from `tag_link` where `ques_id`=#{ques_id}")
    @CacheEvict(value = "quesTags",key = "#ques_id")
    int unLinkTagByQuestion(int ques_id);

    @Select("SELECT `ques_id` from `tag_link` where `tag_id`=#{id}")
    List<Integer> listQuestionIdByTagId(int id);

    @Select("SELECT * FROM tag WHERE tag.tag_id IN (SELECT tag_link.tag_id FROM tag_link WHERE tag_link.ques_id=#{ques_id}) FOR UPDATE;")
    @Cacheable(value = "quesTags",key = "#ques_id")
    List<Tag> findTagsByQuestion(int ques_id);
}
