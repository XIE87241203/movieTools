import base.BaseFunction;
import my_function.ChildFileSearchHelper;
import my_function.DirChildComparatorHelper;
import my_function.DownloadFinishCheckHelper;
import my_function.VideoMoreCheckHelper;
import utils.MyUtils;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        BaseFunction[] functionMenu = new BaseFunction[]{new VideoMoreCheckHelper(), new DownloadFinishCheckHelper()
                , new ChildFileSearchHelper(), new DirChildComparatorHelper()};
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
        // TODO: 2020/12/31 根据目录名重命名视频文件
    }


}
