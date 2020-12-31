package file;


import Utils.MyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DuplicateCheckTools {
    private final static List<String> VIDEO_SUFFIX = Arrays.asList(".ASF", ".AVI", ".WM", ".WMP", ".WMV"
            , ".RAM", ".RM", ".RMVB", ".RP", ".RPM", ".RT", ".SMIL", ".SCM"
            , ".DAT", ".M1V", ".M2V", ".M2P", ".M2TS", ".MP2V", ".MPE", ".MPEG", ".MPEG1", ".MPEG2", ".MPG", ".MPV2", ".PSS", ".TP", ".TPR", ".TS"
            , ".M4B", ".M4R", ".M4P", ".M4V", ".MP4", ".MPEG4"
            , ".3G2", ".3GP", ".3GP2", ".3GPP"
            , ".MOV", ".QT"
            , ".FLV", ".F4V", ".SWF", ".HLV"
            , ".IFO", ".VOB"
            , ".AMV", ".CSF", ".DIVX", ".EVO", ".MKV", ".MOD", ".PMP", ".VP6", ".BIK", ".MTS", ".XLMV", ".OGM", ".OGV", ".OGX");

    private final static List<String> DOWNLOADING_SUFFIX = Arrays.asList(".BC!", ".XLTD");

    /**
     * 开始移动所有下载好的视频文件夹到"下载完成"文件夹内(目录下有视频文件 且 目录及其子目录无缓存文件)
     */
    public static void startMoveDownloadFinishDir(String scanPath) {
        Thread newThread = new Thread(() -> moveDownloadFinishDir(scanPath));
        newThread.start();
    }

    /**
     * 移动所有下载好的视频文件夹到"finish"文件夹内(目录下有视频文件 且 目录及其子目录无缓存文件)
     */
    private static void moveDownloadFinishDir(String scanPath) {
        //获取当前目录路径
        File file = new File(scanPath);
        File targetDir = new File(scanPath + File.separator + "下载完成");
        //如果没有目标文件夹就创建文件夹
        if (!targetDir.exists() && !targetDir.mkdir()) {
            //目标文件夹不存在且创建失败
            MyUtils.systemLogLn("创建文件夹失败");
            return;
        }
        if (file.exists() && file.isDirectory()) {
            //获取目录的文件
            File[] files = file.listFiles();
            if (files == null) return;
            List<File> fileList = new ArrayList<>(Arrays.asList(files));
            //过滤目标文件夹
            fileList.remove(targetDir);
            for (int i = 0; i < fileList.size(); i++) {
                File item = fileList.get(i);
                Boolean[] itemResult = isDirHaveVideoAndCache(item);
                if (itemResult[0] && !itemResult[1]) {
                    MyUtils.systemLog("(" + (i + 1) + "/" + fileList.size() + ") " + "文件 " + item.getName() + "下载完成.");
                    //有视频文件且没有下载缓存
                    //移动到目标文件夹
                    try {
                        MyUtils.systemLog("开始移动文件夹 \"" + item.getName() + "\"");
                        Files.move(Paths.get(item.getPath()),
                                Paths.get(targetDir.getPath() + File.separator + item.getName()), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                        MyUtils.systemLog("移动文件夹 \"" + item.getName() + "\" 失败");
                    }
                } else {
                    MyUtils.systemLog("(" + (i + 1) + "/" + fileList.size() + ") " + "文件 \"" + item.getName() + "\" 未下载完成.");
                }
            }
        }
        MyUtils.systemLog("任务完成！");
    }

    /**
     * 扫描出有超过一个视频文件的文件夹
     */
    public static void logDirNameDuplicateCheck(String scanPath) {
        //获取当前目录路径
        List<String> result = DuplicateCheckTools.dirNameDuplicateCheck(scanPath);
        if(result.isEmpty()){
            MyUtils.systemLog("扫描结果为空。");
        }else{
            StringBuilder scanLog = new StringBuilder();
            for (String item : result) {
                scanLog.append(item).append("\n");
            }
            MyUtils.writeLog("ScanLog", scanPath, scanLog.toString());
        }
    }

    /**
     * 目录内是否有视频文件且没下载缓存
     *
     * @param dirFile 目录文件
     * @return 是否有视频文件和下载缓存
     */
    private static Boolean[] isDirHaveVideoAndCache(File dirFile) {
        Boolean[] result = new Boolean[]{false, false};
        if (!dirFile.isDirectory()) return result;
        File[] fileList = dirFile.listFiles();
        if (fileList == null) return result;
        for (File item : fileList) {
            if (item.isDirectory()) {
                Boolean[] childResult = isDirHaveVideoAndCache(item);
                result[0] = result[0] || childResult[0];
                result[1] = result[1] || childResult[1];
            } else {
                if (!result[0] && VIDEO_SUFFIX.contains(getSuffix(item).toUpperCase())) {
                    //存在视频文件
                    result[0] = true;
                } else if (!result[1] && DOWNLOADING_SUFFIX.contains(getSuffix(item).toUpperCase())) {
                    //存在下载缓存
                    result[1] = true;
                }
            }
            //如果都存在直接返回结果
            if (result[0] && result[1]) {
                break;
            }
        }
        return result;
    }

    /**
     * 查出有超过一个视频文件的文件夹
     *
     * @param path 需要扫描文件夹的路径
     * @return 符合条件的文件目录和对应的文件
     */
    private static List<String> dirNameDuplicateCheck(String path) {
        List<String> result = new ArrayList<>();
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            //获取目录的文件
            File[] fileList = file.listFiles();
            if (fileList != null) {
                for (File child : fileList) {
                    if (!child.isDirectory()) continue;
                    //获取目录下文件夹的子文件
                    File[] childFileList = child.listFiles();
                    if (childFileList == null) continue;
                    //遍历文件夹下面的文件
                    List<String> videoFile = new ArrayList<>();
                    for (File item : childFileList) {
                        if (item.isDirectory()) continue;
                        String suffix = getSuffix(item);
                        if (VIDEO_SUFFIX.contains(suffix.toUpperCase())) {
                            videoFile.add(item.getPath());
                        }
                    }
                    if (videoFile.size() > 1) {
                        //有超过一个文件夹
                        result.add(child.getPath());
                        result.addAll(videoFile);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取文件后缀
     *
     * @param file 文件
     * @return 文件后缀
     */
    private static String getSuffix(File file) {
        String suffix = "";
        int lastIndexOf = file.getName().lastIndexOf(".");
        if (lastIndexOf != -1) {
            suffix = file.getName().substring(lastIndexOf);
        }
        return suffix;
    }
}
