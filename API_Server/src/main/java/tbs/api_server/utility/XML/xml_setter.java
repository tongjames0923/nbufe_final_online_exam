package tbs.api_server.utility.XML;


import tbs.api_server.utility.XML.StrParser.Default.StringParser;
import tbs.api_server.utility.XML.StrParser.IStrParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface xml_setter {
    public String name() default "";
    public Class<? extends IStrParser> parser() default StringParser.class;

}
