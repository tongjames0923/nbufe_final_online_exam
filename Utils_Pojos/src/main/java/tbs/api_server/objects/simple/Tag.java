package tbs.api_server.objects.simple;

public class Tag {
    private int tag_id;
    private String tag_name;
    private int tag_used;

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public int getTag_used() {
        return tag_used;
    }

    public void setTag_used(int tag_used) {
        this.tag_used = tag_used;
    }
}
