package tbs.api_server.utility.XML.ObjectParser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser implements IObjectParser {
    String pattern="yyyy-MM-dd HH:mm";
    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
    @Override
    public String parseObject(Object obj) {
        if(obj instanceof Date)
        {
            return dateFormat.format(obj);
        }
        return "<<!!!!!bad instance!!!!!!>>";
    }
}
