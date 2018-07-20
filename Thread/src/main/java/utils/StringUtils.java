package utils;

/**
 * @description: 字符串工具类 ，日后逐步完善
 * @author: it.wf
 * @create: 2018-07-20 16:57
 **/
public class StringUtils {

    public static boolean isBlank(String s){
        if (s == null || s.trim().isEmpty() || s.equalsIgnoreCase("null")){
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String s){
        return !isBlank(s);
    }
}
