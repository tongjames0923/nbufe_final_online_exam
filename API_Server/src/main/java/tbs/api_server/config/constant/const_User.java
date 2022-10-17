package tbs.api_server.config.constant;

public class const_User {
    public static final String usec_id="id",usec_name="name",usec_password="password",usec_ques="sec_ques",
            usec_ans="sec_ans", uinfo_level ="level";
    public static final String uinfo_id="id",uinfo_address="address",uinfo_phone="phone",uinfo_email="email",uinfo_note="note";
    public static final int userLogin_Success = 100,userLogin_NotFound=102,userLogin_WrongPassword=103;
    public static final int plUser_NO_RIGHTS=112,plUser_SUCCESS=113;
    public static final int userregister_Success = 104,userregister_Duplicate=105,userregister_UnexceptError=106;
    public static final int userpdchange_Success = 107,userpdchange_WrongPassword=108,userpdchange_NotFound=109,userpdchange_WrongAnswer=110,
    userpdchange_noneQues=111;
    //标准只读访问题库,资源员工继承标准，可写资源库,考试员工全功能
    public static final int LEVEL_STANDARD =0,LEVEL_RESOURCES_STAFF=1,LEVEL_EXAM_STAFF=2;


    public static int min_username_length=6;
    public static int min_password_length=8;
}
