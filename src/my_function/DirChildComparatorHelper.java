package my_function;

import base.BaseFunction;
import utils.MyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirChildComparatorHelper extends BaseFunction {

    public DirChildComparatorHelper() {
        super("目录查重", "查找多个目录中重复的文件");
    }

    @Override
    public void startFunction() {
        new Thread(this::startCompare).start();
    }

    private void startCompare() {
        List<String> scanPaths = new ArrayList<>();
        boolean exitInput = false;
        while (!exitInput) {
            String tips;
            if (scanPaths.isEmpty()) {
                tips = "请输入目录路径" + (scanPaths.size() + 1) + "(留空为当前文件夹)：";
            } else {
                tips = "请输入目录路径" + (scanPaths.size() + 1) + "(留空为当前文件夹，输\"n\"结束输入)：";
            }
            String input = MyUtils.getInputPath(tips);
            if (!scanPaths.isEmpty() && (input.equals("n") || input.equals("N"))) {
                //结束输入
                exitInput = true;
            } else {
                scanPaths.add(input);
            }
        }
        MyUtils.systemLog("开始对比");
        List<String> result = new ArrayList<>();
        //获取文件夹名列表数据
        HashMap<String, String> dirNamePathMap = ChildFileSearchHelper.getChildNamePathMap(scanPaths);
        for (Map.Entry<String, String> entry : dirNamePathMap.entrySet()) {
            if (entry.getValue().contains("\n")) {
                result.add("重复文件: " + entry.getKey() + "\n" + entry.getValue());
            }
        }
        //输出结果
        if (result.isEmpty()) {
            MyUtils.systemLog("无重复文件。");
        } else {
            StringBuilder log = new StringBuilder();
            for (String str : result) {
                log.append(str).append("\n\n");
            }
            String logPath = MyUtils.getInputPath("请输入日志输出路径(留空为当前目录)：");
            MyUtils.writeLog("Dir_Compare", logPath, log.toString());
        }
    }
}
