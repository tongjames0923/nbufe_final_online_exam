package tbs.api_server.objects.simple;

import java.util.Objects;

public class UserDetailInfo {
    private int id;
    private String address;
    private String phone;
    private String name;
    private String email;
    private String note;
    private int level;
    private final static long serialVersionUID = 1L;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getLevel()
    {
        return level;
    }
    public void setLevel(int level)
    {
        this.level = level;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString()
    {
        return "UserDetailInfo{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailInfo that = (UserDetailInfo) o;
        return id == that.id && level == that.level && Objects.equals(address, that.address) && Objects.equals(phone, that.phone) && Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, phone, name, email, note, level);
    }
}
