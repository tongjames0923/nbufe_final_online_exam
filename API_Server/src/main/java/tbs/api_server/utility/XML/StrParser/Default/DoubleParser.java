package tbs.api_server.utility.XML.StrParser.Default;

import tbs.api_server.utility.XML.StrParser.IStrParser;

public class DoubleParser implements IStrParser {
    @Override
    public Object parse(String str) {
        return Double.valueOf(str);
    }
}
