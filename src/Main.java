import base.BaseFunction;
import my_function.*;
import utils.MyUtils;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        new Thread(() -> {
            BaseFunction[] functionMenu = new BaseFunction[]{new VideoMoreCheckHelper(), new DownloadFinishCheckHelper()
                    , new ChildFileSearchHelper(), new DirChildComparatorHelper(), new VideoRenameHelper(), new ExtractVideoFilesHelper()};
            MyUtils.systemLog("请选择功能：");
            for (int i = 0; i < functionMenu.length; i++) {
                MyUtils.systemLog((i + 1) + "、" + functionMenu[i].getFunctionDesc());
            }
            MyUtils.systemLog("更多功能敬请期待。。。");
            int functionIndex = -1;
            while (functionIndex < 1 || functionIndex > functionMenu.length) {
                MyUtils.systemLog("请输入所需功能序号(1~" + functionMenu.length + ")：");
                Scanner sc = new Scanner(System.in);
                functionIndex = sc.nextInt();
            }
            functionMenu[functionIndex - 1].startFunction();
            MyUtils.systemLog("任务完成！");
        }).start();
    }


}
