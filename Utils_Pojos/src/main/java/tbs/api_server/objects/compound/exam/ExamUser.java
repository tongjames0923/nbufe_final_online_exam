package tbs.api_server.objects.compound.exam;

import com.alibaba.fastjson.JSON;

public class ExamUser {
    String id;
    String number;
    String name;


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getNumber() {
        return number;
    }


    public void setNumber(String number) {
        this.number = number;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
