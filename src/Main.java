import Utils.MyUtils;
import file.DuplicateCheckTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {
    private static final String FUNCTION_DUPLICATE_CHECK = "logDirNameDuplicateCheck";
    private static final String MOVE_DOWNLOAD_FINISH_CHECK = "startMoveDownloadFinishDir";

    private static final String[] FUNCTION_MENU = {FUNCTION_DUPLICATE_CHECK, MOVE_DOWNLOAD_FINISH_CHECK};

    public static void main(String[] args) {
        MyUtils.systemLog("请选择功能：");
        MyUtils.systemLog("1、查找含有一个以上视频文件的文件夹，并输出LOG。");
        MyUtils.systemLog("2、整理已下载好的视频文件夹至“下载完成”文件夹内。");
        MyUtils.systemLog("更多功能敬请期待。。。");
        int functionIndex = -1;
        while (functionIndex < 1 || functionIndex > FUNCTION_MENU.length) {
            MyUtils.systemLog("请输入所需功能序号（1~" + FUNCTION_MENU.length + "）：");
            Scanner sc = new Scanner(System.in);
            functionIndex = sc.nextInt();
        }

        String scanPath = "";
        //读取路径
        MyUtils.systemLog("请输入目录路径：");
        Scanner sc = new Scanner(System.in);
        scanPath = sc.nextLine();

        if (MyUtils.isEmpty(scanPath)) {
            scanPath = new File("").getPath();
        }

        String functionName = FUNCTION_MENU[functionIndex - 1];
        switch (functionName) {
            case FUNCTION_DUPLICATE_CHECK:
                DuplicateCheckTools.logDirNameDuplicateCheck(scanPath);
                break;
            case MOVE_DOWNLOAD_FINISH_CHECK:
                DuplicateCheckTools.startMoveDownloadFinishDir(scanPath);
                break;
        }
        // TODO: 2020/12/30 根据输入字符串查重
        // TODO: 2020/12/30 当前文件夹的目录与库文件夹的目录查重
        // TODO: 2020/12/31 根据目录名重命名视频文件
    }
}
