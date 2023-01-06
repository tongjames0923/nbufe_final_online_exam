package tbs.api_server.backend.mappers;

import org.apache.ibatis.annotations.*;
import org.springframework.lang.NonNull;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.objects.simple.UserSecurityInfo;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from `user_sec` where `id`=#{id} FOR UPDATE")
    UserSecurityInfo getUserSecurityInfo(int id);
    @Select("select * from `user_sec` where `name`=#{name} FOR UPDATE")
    UserSecurityInfo getUserSecurityInfoByName( @NonNull String name);

    @Select("select a.*,b.`name` from `user_info` a INNER JOIN user_sec b ON b.id=a.id limit #{from},#{num} FOR UPDATE")
    List<UserDetailInfo> getUserDetailInfos(int from,int num);


    @Select("select a.*,b.`name` from `user_info` a INNER JOIN user_sec b ON b.id=a.id where a.`id`=#{id} FOR UPDATE")
    UserDetailInfo getUserDetailInfoByID(int id);

    @Insert("INSERT INTO `user_sec` (`name`, `password`, `sec_ques`, `sec_ans`)" +
            "VALUES\n" +
            "\t(#{name},#{password},#{securityques}, #{securityans});")
    int insertSecurityInfo(@NonNull String name, @NonNull String password, String securityques, String securityans);

    @Insert("INSERT INTO `user_info` (`id`, `address`, `phone`, `email`, `note`)" +
            "VALUES\n" +
            "\t(#{id}, #{address}, #{phone}, #{email},#{note});\n")
    int insertUserDetails(int id, String address, String phone, String email, String note);

    @Update("UPDATE `user_sec` SET `${property}` = #{value} WHERE `id` =#{id}")
    int setValueForUserSecurity(int id, String property, Object value);

    @Update("UPDATE `user_info` SET `${property}` = #{value} WHERE `id` = #{id}")
    int setValueForUserDetails(int id, String property, Object value);
    @Delete("DELETE FROM `user_sec` WHERE `id` = (#{id})")
    int deleteUser(int id);
    @Select("SELECT COUNT(*) FROM user_sec;")
    int userCount();

    @Select("SELECT sec_ques FROM `user_sec` WHERE `name` = #{name}")
    String getQues(String name);



    @Select("SELECT COUNT(*) FROM user_sec WHERE sec_ans=#{ans} AND `name`=#{name};")
    int answerSecQues(String name,String ans);
}
