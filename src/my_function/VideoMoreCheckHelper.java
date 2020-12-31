package my_function;

import base.BaseFunction;
import utils.MyUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoMoreCheckHelper extends BaseFunction {
    public VideoMoreCheckHelper() {
        super("视频文件查重", "查找含有一个以上视频文件的文件夹，并输出LOG。");
    }

    @Override
    public void startFunction() {
        logDirNameDuplicateCheck();
    }

    /**
     * 扫描出有超过一个视频文件的文件夹
     */
    private void logDirNameDuplicateCheck() {
        String scanPath  = MyUtils.getInputPath("请输入目录路径(留空为当前文件夹)：");
        //获取当前目录路径
        List<String> result = dirNameDuplicateCheck(scanPath);
        if(result.isEmpty()){
            MyUtils.systemLog("扫描结果为空。");
        }else{
            StringBuilder scanLog = new StringBuilder();
            for (String item : result) {
                scanLog.append(item).append("\n");
            }
            MyUtils.writeLog("Video_More_Check", scanPath, scanLog.toString());
        }
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
                        String suffix = MyUtils.getSuffix(item);
                        if (MyUtils.VIDEO_SUFFIX.contains(suffix.toUpperCase())) {
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

}
