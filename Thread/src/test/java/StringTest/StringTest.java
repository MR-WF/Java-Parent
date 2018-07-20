package StringTest;


import utils.PrintUtils;
import utils.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @description:
 * @author: it.wf
 * @create: 2018-07-20 16:38
 **/
public class StringTest {
    public static void main(String[] args) {
        String phones = "15510897284,18630802620,13955556666,,13955556666,";
        String[] split = phones.split(",");
        PrintUtils.printInfoLine("length----"+split.length);
        for (int i = 0;i<split.length;i++){
            System.out.println(split[i]);
        }
        PrintUtils.printInfoLine("");
        Set<String> set = new HashSet<>();
        for (int i = 0;i<split.length;i++){
            if (StringUtils.isNotBlank(split[i])){
                set.add(split[i]);
            }
        }

        String[] arrayResult = set.toArray(new String[set.size()]);
        System.out.println(Arrays.toString(arrayResult));

        String s = Arrays.toString(arrayResult).replace("[", "").replace("]", "").replace(" ", "");

        PrintUtils.printInfoLine(s);

    }
}
