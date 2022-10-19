package tbs.api_server.utility;

import org.springframework.beans.factory.annotation.Autowired;
import tbs.api_server.backend.mappers.ExamPermissionMapper;
import tbs.api_server.backend.mappers.QuestionMapper;
import tbs.api_server.backend.mappers.TagMapper;
import tbs.api_server.backend.mappers.UserMapper;

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
}
