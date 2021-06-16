package it.unimore.dipi.iot.dt.utils;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project conduits-zones-manager
 * @created 09/06/2021 - 09:27
 */
public class CommandLineResult {

    private String executedComamnd;

    private String outputString;

    private String errorString;

    private int exitValue;

    private long executionTime;

    public CommandLineResult() {
    }

    public CommandLineResult(String executedComamnd, String outputString, String errorString, int exitValue, long executionTime) {
        this.executedComamnd = executedComamnd;
        this.outputString = outputString;
        this.errorString = errorString;
        this.exitValue = exitValue;
        this.executionTime = executionTime;
    }

    public String getOutputString() {
        return outputString;
    }

    public void setOutputString(String outputString) {
        this.outputString = outputString;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    public int getExitValue() {
        return exitValue;
    }

    public void setExitValue(int exitValue) {
        this.exitValue = exitValue;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public String getExecutedComamnd() {
        return executedComamnd;
    }

    public void setExecutedComamnd(String executedComamnd) {
        this.executedComamnd = executedComamnd;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CommandLineResult{");
        sb.append("executedComamnd='").append(executedComamnd).append('\'');
        sb.append(", outputString='").append(outputString).append('\'');
        sb.append(", errorString='").append(errorString).append('\'');
        sb.append(", exitValue=").append(exitValue);
        sb.append(", executionTime=").append(executionTime);
        sb.append('}');
        return sb.toString();
    }
}
