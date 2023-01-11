package tbs.api_server.utility.XML.StrParser.Default;

import tbs.api_server.utility.XML.StrParser.IStrParser;

public class StringParser implements IStrParser {
    @Override
    public Object parse(String str) {
        return str;
    }
}
