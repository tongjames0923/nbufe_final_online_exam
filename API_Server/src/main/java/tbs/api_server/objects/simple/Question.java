package tbs.api_server.objects.simple;

import java.io.InputStream;
import java.util.Date;

public class Question {
    private int que_id;
    private int que_type;
    private int que_creator;
    private Date que_alter_time;
    private InputStream que_file;
    private int publicable;
    private int tag;
    private int use_time;
    private int answerd;
    private float answerd_right;
}
