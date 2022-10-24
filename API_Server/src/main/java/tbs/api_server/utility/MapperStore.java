package tbs.api_server.utility;

import org.springframework.beans.factory.annotation.Autowired;
import tbs.api_server.backend.mappers.*;

public final class MapperStore
{
    @Autowired
    public static UserMapper userMapper;
    @Autowired
    public static TagMapper tagMapper;
    @Autowired
    public static QuestionMapper questionMapper;

    @Autowired
    public static ExamPermissionMapper examPermissionMapper;
    @Autowired
    public static ResourceMapper resourceMapper;
    @Autowired
    public static ExamMapper examMapper;
    @Autowired
    public static AnswerMapper answerMapper;
    @Autowired
    public static ExamReplyMapper examReplyMapper;
}
