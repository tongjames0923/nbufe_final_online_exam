package tbs.api_server.backend.mappers;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tbs.api_server.objects.simple.Tag;

import java.util.List;

@Mapper
public interface TagMapper {
    @Select("select * from tag")
    List<Tag> getAllTags();

    @Select("select * from tag where tag_name=#{name}")
    Tag getTagByName(String name);

    @Select("select * from tag where id=#{id}")
    Tag getTagById(int id);

    @Delete("delete from tag where tag_name=#{name}")
    int deleteTagByName(String name);

    @Update("UPDATE `tag` SET `tag_used` = #{cnt} WHERE `tag_id` = #{id}")
    int setUsed(int id,int cnt);
}
