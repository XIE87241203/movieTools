package base;


public abstract class BaseFunction {
    private String functionName = "";//功能名
    private String functionDesc = "";//功能描述

    public BaseFunction(String functionName, String functionDesc) {
        this.functionName = functionName;
        this.functionDesc = functionDesc;
    }

    public abstract void startFunction();

    public String getFunctionName() {
        return functionName;
    }

    public String getFunctionDesc() {
        return functionDesc;
    }
}
