package my_function;

import base.BaseFunction;
import utils.MyUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileSearchCheckHelper extends BaseFunction {
    private HashMap<String, String> dirNamePathMap;

    public FileSearchCheckHelper() {
        super("查找目录下一级文件和文件夹", "查找目录下的一级文件和文件夹是否存在。");
    }

    @Override
    public void startFunction() {
        new Thread(this::startInputData).start();
    }

    private void startInputData() {
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
        //获取文件夹名列表数据
        dirNamePathMap = new HashMap<>();
        for (String path : scanPaths) {
            File scanFile = new File(path);
            File[] listFiles = scanFile.listFiles();
            if (listFiles != null) {
                for (File fileItem : listFiles) {
                    String fileName = fileItem.getName().toUpperCase();
                    if (!fileItem.isDirectory()) {
                        //去除后缀
                        fileName = fileName.substring(0, fileName.length() - MyUtils.getSuffix(fileItem).length());
                    }
                    String lastPath = dirNamePathMap.get(fileName);
                    if (lastPath == null) {
                        lastPath = fileItem.getPath();
                    } else {
                        lastPath = lastPath + "\n" + fileItem.getPath();
                    }
                    dirNamePathMap.put(fileName, lastPath);
                }
            }
        }
        MyUtils.systemLog("读取目录文件夹成功");
        startSearch();
    }

    private void startSearch() {
        boolean exitSearch = false;
        while (!exitSearch) {
            String input = MyUtils.getInputPath("请输入搜索文件名(留空为结束搜索):").toUpperCase();
            if (MyUtils.isEmpty(input)) {
                exitSearch = true;
            } else {
                String path = dirNamePathMap.get(input);
                if (!MyUtils.isEmpty(path)) {
                    MyUtils.systemLog("查找结果:\n" + path);
                } else {
                    MyUtils.systemLog("无匹配结果。");
                }
            }
        }
    }
}
