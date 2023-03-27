package tbs.api_server.objects.compound.exam;

import tbs.api_server.objects.simple.ExamPermission;
import tbs.api_server.objects.simple.UserDetailInfo;

public class ExamPermitionVo {
    ExamPermission permission;
    UserDetailInfo user;

    public ExamPermitionVo(ExamPermission permission, UserDetailInfo user) {
        this.permission = permission;
        this.user = user;
    }

    public ExamPermission getPermission() {
        return permission;
    }

    public void setPermission(ExamPermission permission) {
        this.permission = permission;
    }

    public UserDetailInfo getUser() {
        return user;
    }

    public void setUser(UserDetailInfo user) {
        this.user = user;
    }
}
