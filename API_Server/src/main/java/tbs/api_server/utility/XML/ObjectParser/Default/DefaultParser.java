package tbs.api_server.utility.XML.ObjectParser.Default;

import tbs.api_server.utility.XML.ObjectParser.IObjectParser;

public class DefaultParser implements IObjectParser {

    @Override
    public String parseObject(Object obj) {
        return obj.toString();
    }
}
