package org.zipper.flowable.app.service;


import org.flowable.engine.repository.Deployment;
import org.flowable.task.api.Task;

import java.util.List;
import java.util.Map;

/**
 * 定义流程相关服务
 *
 * @author zhuxj
 * @since 2020/09/28
 */
public interface FlowableService {

    /**
     * 部署流程定义
     *
     * @param name 流程名称
     * @param xml 流程Bpmn2.0定义字符串
     * @return Deployment
     */
    public Deployment deploy(String name, String xml);

    /**
     * 创建流程实例
     *
     * @param initiator  流程发起方
     * @param processKey 流程关键字，即*.bpmn20.xml中process标签的id字段
     * @param variables  流程变量
     */
    public void startProcess(String initiator, String processKey, Map<String, Object> variables);

    /**
     * 获取某个候选人的待完成任务
     *
     * @param assignee 候选人
     * @return 任务列表
     */
    public List<Task> getTasks(String assignee);

}
