package tbs.api_server.config.constant;

public class const_User {
    public static final String usec_id="id",usec_name="name",usec_password="password",usec_ques="sec_ques",
            usec_ans="sec_ans", uinfo_level ="level";
    public static final String uinfo_id="id",uinfo_address="address",uinfo_phone="phone",uinfo_email="email",uinfo_note="note";

    //标准只读访问题库,资源员工继承标准，可写资源库,考试员工全功能
    public static final int LEVEL_STANDARD =0,LEVEL_RESOURCES_STAFF=1,LEVEL_EXAM_STAFF=2;


    public static int min_username_length=6;
    public static int min_password_length=8;
}
