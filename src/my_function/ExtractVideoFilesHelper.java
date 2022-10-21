package my_function;

import base.BaseFunction;
import utils.MyUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExtractVideoFilesHelper extends BaseFunction {

    public ExtractVideoFilesHelper() {
        super("提取文件夹内的视频", "把目录内的所有文件夹内的视频文件提取至指定目录");
    }

    @Override
    public void startFunction() {
        StringBuilder scanLog = new StringBuilder();
        String scanPath = MyUtils.getInputPath("请输入目录路径(留空为当前文件夹)：");
        File scanFile = new File(scanPath);
        MyUtils.systemLog("开始扫描");
        List<File> scanResult = getVideoList(scanFile);
        MyUtils.systemLog("共查找到" + scanResult.size() + "个结果");
        int errorCount = 0;
        int successCount = 0;
        if (!scanResult.isEmpty()) {
            //目录不为空，开始移动文件
            String targetPath = MyUtils.getInputPath("请输入目标路径(留空为当前文件夹)：");
            File targetFile = new File(targetPath);
            if (!targetFile.exists()) {
                MyUtils.systemLog("目标目录不存在");
                return;
            }
            if (targetFile.isFile()) {
                MyUtils.systemLog("目标不是一个目录");
                return;
            }
            for (int i = 0; i < scanResult.size(); i++) {
                File item = scanResult.get(i);
                MyUtils.systemLog(String.format("正在移动文件(%d/%d):%s", i, scanResult.size(), item.getName()));
                if (!moveFile(item, targetFile)) {
                    errorCount++;
                } else {
                    successCount++;
                }
            }
        }
        MyUtils.systemLog(String.format("移动完成，%d个成功，%d个失败", successCount, errorCount));
    }

    private boolean moveFile(File file, File targetfile) {
        String destinationFile = targetfile.getPath() + File.separator + replaceC(file.getName());
        if (!file.renameTo(new File(destinationFile))) {
            MyUtils.systemLogError("移动文件 \"" + file.getPath() + "\" 失败");
            return false;
        }
        return true;
    }

    /**
     * 替换结尾的C为-C
     *
     * @param fileName 文件全名包括后缀
     */
    private String replaceC(String fileName) {
        String key = "C";
        String unMatchKey = "-C";
        String suffix = MyUtils.getSuffix(fileName);
        String nameNoSuffix = fileName.substring(0, fileName.length() - suffix.length());
        if (nameNoSuffix.endsWith(key) && !nameNoSuffix.endsWith(unMatchKey)) {
            //把结尾的C改为-C
            nameNoSuffix = nameNoSuffix.substring(0, nameNoSuffix.length() - key.length()) + unMatchKey;
        }else{
            return fileName;
        }
        return nameNoSuffix + suffix;
    }

    /**
     * 获取文件夹中的视频文件
     *
     * @return 文件列表
     */
    private List<File> getVideoList(File file) {
        List<File> result = new ArrayList<>();
        if (file.isDirectory()) {
            //file是目录
            File[] childList = file.listFiles();
            if (childList != null) {
                for (File child : childList) {
                    //递归获取视频文件
                    result.addAll(getVideoList(child));
                }
            }
        } else {
            //file不是目录
            if (MyUtils.isVideo(file)) {
                result.add(file);
            }
        }
        return result;
    }

}
