package tbs.api_server.objects.simple;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class QuestionResource {
    private int id;
    private String resource;
    private String note;
    private int resource_type;


    @DateTimeFormat(pattern="yyyy年MM月dd日 hh:mm:ss")
    @JsonFormat(pattern="yyyy年MM月dd日 hh:mm:ss",timezone="GMT+8")
    private Date altertime;

    public Date getAltertime()
    {
        return altertime;
    }

    public void setAltertime(Date altertime)
    {
        this.altertime = altertime;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResource()
    {
        return resource;
    }

    public void setResource(String resource)
    {
        this.resource = resource;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getResource_type()
    {
        return resource_type;
    }

    public void setResource_type(int resource_type) {
        this.resource_type = resource_type;
    }
}
