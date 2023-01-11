package tbs.api_server.utility.XML.StrParser.Default;

import tbs.api_server.utility.XML.StrParser.IStrParser;

public class IntParser implements IStrParser {
    @Override
    public Object parse(String str) {
        return Integer.valueOf(str);
    }
}
