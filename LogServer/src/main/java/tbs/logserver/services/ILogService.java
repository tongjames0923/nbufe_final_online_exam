package tbs.logserver.services;

import tbs.api_server.objects.simple.LogPojo;

import java.util.List;

public interface ILogService {
    public List<LogPojo> listLogInPage(int from, int num, int fied, String val) ;
}
