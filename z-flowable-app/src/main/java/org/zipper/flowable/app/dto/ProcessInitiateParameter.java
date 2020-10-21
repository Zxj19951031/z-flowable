package org.zipper.flowable.app.dto;

import java.util.Map;

/**
 * @author zhuxj
 * @since 2020/10/13
 */
public class ProcessInitiateParameter {

    /**
     * 流程标识符
     * 即xml中process标签的id属性
     */
    private String processKey;
    /**
     * 流程发起变量
     */
    private Map<String, Object> variables;

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
}
