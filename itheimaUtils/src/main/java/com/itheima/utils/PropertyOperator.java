package com.itheima.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 属性操作工具
 *
 * @author tengchao
 * @create 2018-01-05-16:14.
 */
public class PropertyOperator {

    public <T> T replacePropertyFromEmptyToNull(T t) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass(), Object.class);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Method readMethod = propertyDescriptor.getReadMethod();
                Object invoke = readMethod.invoke(t);
                if("".equals(invoke)){
                    List list = new ArrayList();
                    list.add(null);
                    propertyDescriptor.getWriteMethod().invoke(t,list.toArray());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
