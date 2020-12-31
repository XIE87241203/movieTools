package my_function;

import base.BaseFunction;
import utils.MyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DownloadFinishCheckHelper extends BaseFunction {
    public DownloadFinishCheckHelper() {
        super("文件下载完成检测", "整理已下载好的视频文件夹至“下载完成”文件夹内。");
    }

    @Override
    public void startFunction() {
        startMoveDownloadFinishDir();
    }
    /**
     * 开始移动所有下载好的视频文件夹到"下载完成"文件夹内(目录下有视频文件 且 目录及其子目录无缓存文件)
     */
    private void startMoveDownloadFinishDir() {
        String scanPath = MyUtils.getInputPath("请输入目录路径(留空为当前文件夹)：");
        Thread newThread = new Thread(() -> moveDownloadFinishDir(scanPath));
        newThread.start();
    }

    /**
     * 移动所有下载好的视频文件夹到"finish"文件夹内(目录下有视频文件 且 目录及其子目录无缓存文件)
     */
    private void moveDownloadFinishDir(String scanPath) {
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
                        MyUtils.systemLogError("移动文件夹 \"" + item.getName() + "\" 失败");
                    }
                } else {
                    MyUtils.systemLog("(" + (i + 1) + "/" + fileList.size() + ") " + "文件 \"" + item.getName() + "\" 未下载完成.");
                }
            }
        }
    }

    /**
     * 目录内是否有视频文件且没下载缓存
     *
     * @param dirFile 目录文件
     * @return 是否有视频文件和下载缓存
     */
    private Boolean[] isDirHaveVideoAndCache(File dirFile) {
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
                if (!result[0] && MyUtils.VIDEO_SUFFIX.contains(MyUtils.getSuffix(item).toUpperCase())) {
                    //存在视频文件
                    result[0] = true;
                } else if (!result[1] && MyUtils.DOWNLOADING_SUFFIX.contains(MyUtils.getSuffix(item).toUpperCase())) {
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

}
