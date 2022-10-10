package tbs.api_server.objects.simple;

import java.io.InputStream;
import java.util.Date;

public class ExamInfo {
    private int exam_id;
    private String exam_name;
    private Date exam_begin;
    private int exam_len;
    private InputStream exam_file;
    private String exam_note;
    private int exam_status;
}
