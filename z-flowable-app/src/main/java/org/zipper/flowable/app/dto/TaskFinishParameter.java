package org.zipper.flowable.app.dto;

import java.util.Map;

/**
 * 用户任务完成参数
 *
 * @author zhuxj
 * @since 2020/10/15
 */
public class TaskFinishParameter {

    /**
     * 任务编号
     */
    private String taskId;
    /**
     * 递交参数
     */
    private Map<String, Object> localVariables;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Map<String, Object> getLocalVariables() {
        return localVariables;
    }

    public void setLocalVariables(Map<String, Object> localVariables) {
        this.localVariables = localVariables;
    }
}
