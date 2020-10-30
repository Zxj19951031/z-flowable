package org.zipper.flowable.app.entity;

import com.alibaba.fastjson.JSONObject;
import org.zipper.flowable.app.constant.enums.InstanceStage;
import org.zipper.flowable.app.constant.enums.Status;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author zhuxj
 * @since 2020/10/28
 */
public class MyProcessInstance {

    private Integer id;
    private String processKey;
    private String instanceId;
    private String initiator;
    private InstanceStage stage;
    private String variables;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Status status;

    public MyProcessInstance() {
    }

    public MyProcessInstance(String initiator, String processKey, Map<String, Object> variables) {
        this.initiator = initiator;
        this.processKey = processKey;
        this.variables = JSONObject.toJSONString(variables);
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.status = Status.VALID;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public InstanceStage getStage() {
        return stage;
    }

    public void setStage(InstanceStage stage) {
        this.stage = stage;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
