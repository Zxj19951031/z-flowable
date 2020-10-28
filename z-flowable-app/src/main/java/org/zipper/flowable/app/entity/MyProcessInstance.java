package org.zipper.flowable.app.entity;

import org.zipper.flowable.app.constant.enums.InstanceStage;
import org.zipper.flowable.app.constant.enums.Status;

import java.time.LocalDateTime;

/**
 * @author zhuxj
 * @since 2020/10/28
 */
public class MyProcessInstance {

    private Integer id;
    private String instanceId;
    private InstanceStage stage;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Status status;

    public MyProcessInstance() {
    }

    public MyProcessInstance(String processInstanceId) {
        this.instanceId = processInstanceId;
        this.stage = InstanceStage.START;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.status = Status.VALID;
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
