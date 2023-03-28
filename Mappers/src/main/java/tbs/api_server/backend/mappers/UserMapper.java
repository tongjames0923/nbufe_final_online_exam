package tbs.api_server.backend.mappers;


import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.lang.NonNull;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.objects.simple.UserSecurityInfo;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select *,ui.level from `user_sec` uc join `user_info` ui on ui.id=uc.id  where uc.`id`=#{id} FOR UPDATE")
    @Cacheable(value = "userSec",key = "#id")
    UserSecurityInfo getUserSecurityInfo(int id);
    @Select("select *,ui.level from `user_sec` uc join `user_info` ui on ui.id=uc.id  where uc.`name`=#{name} FOR UPDATE")
    @Cacheable(value = "userSec",key = "result.id")
    UserSecurityInfo getUserSecurityInfoByName( @NonNull String name);


    @Select("select a.*,b.`name` from `user_info` a INNER JOIN user_sec b ON b.id=a.id where b.name like '${name}%' order by b.name limit #{from},#{num} FOR UPDATE")
    List<UserDetailInfo> findByNameLike(String name,int from,int num);

    @Select("select a.*,b.`name` from `user_info` a INNER JOIN user_sec b ON b.id=a.id limit #{from},#{num}")
    List<UserDetailInfo> getUserDetailInfos(int from,int num);


    @Select("select a.*,b.`name` from `user_info` a INNER JOIN user_sec b ON b.id=a.id where a.`id`=#{id}")
    @Cacheable(value = "userDetail",key = "#id")
    UserDetailInfo getUserDetailInfoByID(int id);

    @Insert("INSERT INTO `user_sec` (`id`,`name`, `password`, `sec_ques`, `sec_ans`)" +
            "VALUES\n" +
            "\t(#{id},#{name},#{password},#{securityques}, #{securityans});")
    int insertSecurityInfo(int id,@NonNull String name, @NonNull String password, String securityques, String securityans);

    @Insert("INSERT INTO `user_info` (`id`, `address`, `phone`, `email`, `note`)" +
            "VALUES\n" +
            "\t(#{id}, #{address}, #{phone}, #{email},#{note});\n")
    int insertUserDetails(int id, String address, String phone, String email, String note);

    @Update("UPDATE `user_sec` SET `${property}` = #{value} WHERE `id` =#{id}")
    @CacheEvict(value = "userSec",key = "#id")
    int setValueForUserSecurity(int id, String property, Object value);

    @Update("UPDATE `user_info` SET `${property}` = #{value} WHERE `id` = #{id}")
    @CacheEvict(value = "userDetail",key = "#id")
    int setValueForUserDetails(int id, String property, Object value);
    @Delete("DELETE FROM `user_sec` WHERE `id` = (#{id})")
    @Caching(evict = {
            @CacheEvict(value = "userDetail",key = "#id"),
            @CacheEvict(value = "userSec",key = "#id")
    })
    int deleteUser(int id);
    @Select("SELECT COUNT(*) FROM user_sec;")
    int userCount();

    @Select("SELECT sec_ques FROM `user_sec` WHERE `name` = #{name}")
    String getQues(String name);



    @Select("SELECT COUNT(*) FROM user_sec WHERE sec_ans=#{ans} AND `name`=#{name};")
    int answerSecQues(String name,String ans);
}
