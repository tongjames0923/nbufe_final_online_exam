package tbs.api_server.objects.compound.exam;

import tbs.api_server.utility.XML.xml_getter;
import tbs.api_server.utility.XML.xml_setter;

public class ExamUser {
    String id;
    String number;
    String name;

    @xml_getter(name = "id")
    public String getId() {
        return id;
    }

    @xml_setter(name = "id")
    public void setId(String id) {
        this.id = id;
    }

    @xml_getter(name = "number")
    public String getNumber() {
        return number;
    }

    @xml_setter(name = "number")
    public void setNumber(String number) {
        this.number = number;
    }

    @xml_getter(name = "name")
    public String getName() {
        return name;
    }

    @xml_setter(name = "name")
    public void setName(String name) {
        this.name = name;
    }
}
