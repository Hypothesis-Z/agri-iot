package com.zzq.app.util;

import java.lang.reflect.ParameterizedType;

public class ReflectUtil {
    public static <T> T getT(Object o, int i){
        try{
            return ((Class<T>) ((ParameterizedType)
                (o.getClass().getGenericSuperclass())).getActualTypeArguments()[i]).newInstance();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
