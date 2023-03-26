package tbs.api_server.objects.simple;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Arrays;
import java.util.Date;

public class Question {

    private int que_id;
    private int que_type;
    private String title;


    private String answer_data;
    private int que_creator;
    @DateTimeFormat(pattern="yyyy年MM月dd日 hh:mm:ss")
    @JsonFormat(pattern="yyyy年MM月dd日 hh:mm:ss",timezone="GMT+8")
    private Date que_alter_time;
    private String que_file;
    private int publicable;
    private int use_time;
    private int answerd;
    private float answerd_right;
    public String getAnswer_data() {
        return answer_data;
    }

    public void setAnswer_data(String answer_data) {
        this.answer_data = answer_data;
    }

    public int getQue_id()
    {
        return que_id;
    }

    public void setQue_id(int que_id)
    {
        this.que_id = que_id;
    }

    public int getQue_type()
    {
        return que_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setQue_type(int que_type)
    {
        this.que_type = que_type;
    }

    public int getQue_creator()
    {
        return que_creator;
    }

    public void setQue_creator(int que_creator)
    {
        this.que_creator = que_creator;
    }

    public Date getQue_alter_time()
    {
        return que_alter_time;
    }

    public void setQue_alter_time(Date que_alter_time)
    {
        this.que_alter_time = que_alter_time;
    }

    public String getQue_file()
    {
        return que_file;
    }

    public void setQue_file(String que_file)
    {
        this.que_file = que_file;
    }

    public int getPublicable()
    {
        return publicable;
    }

    public void setPublicable(int publicable)
    {
        this.publicable = publicable;
    }

    public int getUse_time()
    {
        return use_time;
    }

    public void setUse_time(int use_time)
    {
        this.use_time = use_time;
    }

    public int getAnswerd()
    {
        return answerd;
    }

    public void setAnswerd(int answerd)
    {
        this.answerd = answerd;
    }

    public float getAnswerd_right()
    {
        return answerd_right;
    }

    public void setAnswerd_right(float answerd_right)
    {
        this.answerd_right = answerd_right;
    }

    @Override
    public String toString() {
        return "Question{" +
                "que_id=" + que_id +
                ", que_type=" + que_type +
                ", title='" + title + '\'' +
                ", answer_data='" + answer_data + '\'' +
                ", que_creator=" + que_creator +
                ", que_alter_time=" + que_alter_time +
                ", publicable=" + publicable +
                ", use_time=" + use_time +
                ", answerd=" + answerd +
                ", answerd_right=" + answerd_right +
                '}';
    }
}
