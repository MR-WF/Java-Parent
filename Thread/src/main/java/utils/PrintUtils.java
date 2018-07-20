package utils;

/**
 * @description:
 * @author: it.wf
 * @create: 2018-07-20 17:02
 **/
public class PrintUtils {

    public static void printInfoLine(String info){
        System.out.println("**************************************************");
        System.out.println(info);
        System.out.println("**************************************************");
    }

    public static void printLine(String line){
        if (StringUtils.isBlank(line)){
            line = "**************************************************";
        }
        System.out.println(line);
    }
}
