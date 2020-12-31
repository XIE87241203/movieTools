package my_function;

import base.BaseFunction;
import utils.MyUtils;

import java.io.File;

public class VideoRenameHelper extends BaseFunction {
    public VideoRenameHelper() {
        super("根据目录重命名视频文件", "根据目录下子文件夹重命名子文件夹内的视频文件");
    }

    @Override
    public void startFunction() {
        StringBuilder scanLog = new StringBuilder();
        String scanPath = MyUtils.getInputPath("请输入目录路径(留空为当前文件夹)：");
        File scanFile = new File(scanPath);
        File[] childList = scanFile.listFiles();
        if (childList != null) {
            for (File child : childList) {
                if (child.isDirectory()) {
                    //获取视频文件数量
                    int videoNum = getDirVideoNum(child);
                    if (videoNum > 0) {
                        if (videoNum < 2) {
                            //只有一个视频文件
                            renameVideoFile(child);
                        } else {
                            //有多个视频文件，输出log
                            scanLog.append("存在多个视频文件:\"").append(child.getPath()).append("\"");
                        }
                    }
                }
            }
        }

        if (!MyUtils.isStrEmpty(scanLog.toString())) {
            //输出log
            String logPath = MyUtils.getInputPath("请输入日志输出路径(留空为当前目录)：");
            MyUtils.writeLog("Video_Rename_Log", logPath, scanLog.toString());
        }
    }

    private int getDirVideoNum(File dir) {
        File[] files = dir.listFiles();
        int videoNum = 0;
        if (files != null) {
            for (File child : files) {
                String suffix = MyUtils.getSuffix(child).toUpperCase();
                if (MyUtils.VIDEO_SUFFIX.contains(suffix)) {
                    videoNum++;
                }
            }
        }
        return videoNum;
    }

    /**
     * 重命名目录下所有视频文件
     *
     * @param dir 目录
     */
    private void renameVideoFile(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File child : files) {
                String suffix = MyUtils.getSuffix(child).toUpperCase();
                if (MyUtils.VIDEO_SUFFIX.contains(suffix)) {
                    //是视频文件
                    StringBuilder filePathBuilder = new StringBuilder();
                    filePathBuilder.append(dir.getPath()).append(File.separator);
                    filePathBuilder.append(dir.getName());
                    filePathBuilder.append(suffix);
                    if (!child.renameTo(new File(filePathBuilder.toString()))) {
                        MyUtils.systemLogError("文件 \"" + child.getPath() + "\" 重命名失败");
                    }
                }
            }
        }
    }
}
