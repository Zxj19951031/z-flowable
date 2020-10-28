package org.zipper.flowable.app.entity;

import org.zipper.flowable.app.constant.enums.ProcessStatus;
import org.zipper.flowable.app.constant.enums.Status;

import java.time.LocalDateTime;

/**
 * @author zhuxj
 * @since 2020/10/12
 */
public class MyProcess {
    private Integer id;
    private String processKey;
    private String name;
    private Integer formId;
    private String xml;
    private ProcessStatus deployStatus;
    private LocalDateTime deployTime;
    private String allowInitiator;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Status status;

    public MyProcess() {
    }

    public MyProcess(String name, String key, String xml, Integer formId) {
        this.processKey = key;
        this.name = name;
        this.formId = formId;
        this.xml = xml;
        this.deployStatus = ProcessStatus.UNPUBLISHED;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.status = Status.VALID;
    }


    public String getAllowInitiator() {
        return allowInitiator;
    }

    public void setAllowInitiator(String allowInitiator) {
        this.allowInitiator = allowInitiator;
    }

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public LocalDateTime getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(LocalDateTime deployTime) {
        this.deployTime = deployTime;
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

    public ProcessStatus getDeployStatus() {
        return deployStatus;
    }

    public void setDeployStatus(ProcessStatus deployStatus) {
        this.deployStatus = deployStatus;
    }
}
