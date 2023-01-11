package tbs.api_server.utility.XML;

import tbs.api_server.utility.XML.ObjectParser.Default.DefaultParser;
import tbs.api_server.utility.XML.ObjectParser.IObjectParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface xml_getter {
    public String name() default "";
    public Class<? extends IObjectParser> parser() default DefaultParser.class;
}
