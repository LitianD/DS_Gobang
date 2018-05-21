package Gobang.Util;

import com.google.gson.Gson;

public class JsonChangeStrUtil {
    public static <T>T converJsonToJavaBean(String json,Class<T> tClass){
        if(json.length()==0) {
            return null;
        }
        Gson gson=new Gson();
        return gson.fromJson(json,tClass);
    }

    public static String convertJavaBeanToJson(Object object){
        if (object==null){
            return "";
        }
        Gson gson=new Gson();
        String beanStr=gson.toJson(object);
        if(beanStr!=null&&beanStr.length()!=0){
            return beanStr;
        }
        return "";
    }
}
