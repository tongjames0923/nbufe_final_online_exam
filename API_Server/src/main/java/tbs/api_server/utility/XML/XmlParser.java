package tbs.api_server.utility.XML;

import org.dom4j.Attribute;
import org.dom4j.Element;
import tbs.api_server.utility.XML.ObjectParser.IObjectParser;
import tbs.api_server.utility.XML.StrParser.IStrParser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class XmlParser<T> {
    Map<xml_getter, Method> getter = null;
    Map<xml_setter, Method> setter = null;
    String tag;

    public XmlParser(Class<? extends T> cla, String tag) {
        this.tag = tag;
        getter = new HashMap<xml_getter, Method>();
        setter = new HashMap<xml_setter, Method>();
        for (Method method : cla.getMethods()) {
            xml_getter value = method.getAnnotation(xml_getter.class);
            if (value != null) {
                getter.put(value, method);
            }
            xml_setter setterValue = method.getAnnotation(xml_setter.class);
            if (setterValue != null) {
                setter.put(setterValue, method);
            }
        }
    }

    public T parse(Element element, Class<? extends T> cla) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T obj = cla.getConstructor(null).newInstance(null);
        for (Map.Entry<xml_setter, Method> entry : setter.entrySet()) {
            xml_setter set = entry.getKey();
            Attribute attribute = element.attribute(set.name());
            if (attribute == null)
                continue;
            IStrParser parser = set.parser().getConstructor(null).newInstance(null);
            String atstr = attribute.getValue();
            Object val = null;
            if (!atstr.equals("null")) {
                val = parser.parse(atstr);
            }
            Method m = entry.getValue();
            m.invoke(obj, val);
        }
        return obj;
    }
    static <T> T makeInstance(Class<? extends T> cla,Object... args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
       return cla.getConstructor(null).newInstance(null);
    }
    public Element put(Element parent, T value) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Element element = parent.addElement(tag);
        for (Map.Entry<xml_getter, Method> entry : getter.entrySet()) {
            String key = entry.getKey().name();
            Method method = entry.getValue();
            if (method != null) {
                Object obj = method.invoke(value);
                if (obj != null) {
                   IObjectParser parser= makeInstance(entry.getKey().parser(),null);
                    element.addAttribute(key,parser.parseObject(obj));
                } else {
                    element.addAttribute(key, "null");
                }

            }
        }
        return element;
    }
}
