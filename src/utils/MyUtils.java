package utils;


import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyUtils {
    public final static List<String> VIDEO_SUFFIX = Arrays.asList(".ASF", ".AVI", ".WM", ".WMP", ".WMV"
            , ".RAM", ".RM", ".RMVB", ".RP", ".RPM", ".RT", ".SMIL", ".SCM"
            , ".DAT", ".M1V", ".M2V", ".M2P", ".M2TS", ".MP2V", ".MPE", ".MPEG", ".MPEG1", ".MPEG2", ".MPG", ".MPV2", ".PSS", ".TP", ".TPR", ".TS"
            , ".M4B", ".M4R", ".M4P", ".M4V", ".MP4", ".MPEG4"
            , ".3G2", ".3GP", ".3GP2", ".3GPP"
            , ".MOV", ".QT"
            , ".FLV", ".F4V", ".SWF", ".HLV"
            , ".IFO", ".VOB"
            , ".AMV", ".CSF", ".DIVX", ".EVO", ".MKV", ".MOD", ".PMP", ".VP6", ".BIK", ".MTS", ".XLMV", ".OGM", ".OGV", ".OGX");

    public final static List<String> DOWNLOADING_SUFFIX = Arrays.asList(".BC!", ".XLTD");

    /**
     * 字符串判空
     *
     * @param str 字符串
     * @return 是否为null或空字符串
     */
    public static boolean isStrEmpty(String str) {
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

    public static void systemLogError(String content){
        System.out.println("错误" + content);
    }
    /**
     * 输出日志到txt
     * @param prefix 日志名前缀
     * @param dirPath 日志路径
     * @param content 日志内容
     */
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

    public static String getInputPath(String tips){
        String scanPath = "";
        //读取路径
        MyUtils.systemLog(tips);
        Scanner sc = new Scanner(System.in);
        scanPath = sc.nextLine();

        if (MyUtils.isStrEmpty(scanPath)) {
            scanPath = new File("").getPath();
        }
        return scanPath;
    }


    /**
     * 获取文件后缀
     *
     * @param file 文件
     * @return 文件后缀
     */
    public static String getSuffix(File file) {
        String suffix = "";
        if(!file.isDirectory()){
           suffix = getSuffix(file.getName());
        }
        return suffix;
    }

    public static String getSuffix(String file) {
        String suffix = "";
        int lastIndexOf = file.lastIndexOf(".");
        if (lastIndexOf != -1) {
            suffix = file.substring(lastIndexOf);
        }
        return suffix;
    }

    public static boolean isVideo(File file){
        //获取文件后缀
        String suffix = MyUtils.getSuffix(file).toUpperCase();
        return MyUtils.VIDEO_SUFFIX.contains(suffix);
    }
}
