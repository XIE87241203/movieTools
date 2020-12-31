package Utils;

import com.sun.istack.internal.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyUtils {

    /**
     * 字符串判空
     *
     * @param str 字符串
     * @return 是否为null或空字符串
     */
    public static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }

    /**
     * 生成log日志的时间
     *
     * @return log日志的时间
     */
    public static String logTime() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sf.format(new Date());
    }

    public static void systemLog(String content){
        System.out.println(content);
    }
    public static void systemLogLn(String content){
        System.out.println(content);
        System.out.println("");
    }

    public static void writeLog(@Nullable String prefix, String dirPath, String content) {
        if (prefix == null) prefix = "";
        FileWriter fileWriter = null;
        try {
            String logFileName = dirPath + File.separator + prefix +
                    "_" + logTime() + "_" + System.currentTimeMillis() + ".txt";
            fileWriter = new FileWriter(logFileName, true);
            fileWriter.write(content);
            systemLog("日志已输出到：\"" + logFileName + "\"");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
