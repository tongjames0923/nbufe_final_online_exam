package tbs.api_server.backend.mappers;

import org.apache.ibatis.annotations.*;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.objects.simple.UserSecurityInfo;

@Mapper
@Validated
public interface UserMapper {

    @Select("select * from user_sec where name=#{name} and password=#{password}")
    UserSecurityInfo getUserSecurityInfo(@NonNull String name, @NonNull String password);

    @Select("select * from user_sec where id=#{id} and password=#{password}")
    UserSecurityInfo getUserSecurityInfoByID(int id, @NonNull String password);

    @Select("select * from user_info where id=#{id}")
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
    @Delete("DELETE FROM `user_sec` WHERE `id` IN (#{id})")
    int deleteUser(int id);
}
