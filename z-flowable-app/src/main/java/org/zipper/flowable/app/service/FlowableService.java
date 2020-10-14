package org.zipper.flowable.app.service;


import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
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
     * @param xml  流程Bpmn2.0定义字符串
     * @return Deployment
     */
    public Deployment deploy(String name, String xml);

    /**
     * 创建流程实例
     *
     * @param initiator  流程发起方
     * @param processKey 流程关键字，即*.bpmn20.xml中process标签的id字段
     * @param variables  流程变量
     * @return ProcessInstance
     */
    public ProcessInstance startProcess(String initiator, String processKey, Map<String, Object> variables);


    /**
     * 查询某人发起的任务实例编号
     *
     * @param pageNum   页码
     * @param pageSize  单页大小
     * @param initiator 发起人
     * @return list of {@link HistoricProcessInstance}
     */
    public List<HistoricProcessInstance> queryMine(int pageNum, int pageSize, String initiator);

    /**
     * 查询某人的代办任务实例编号
     *
     * @param pageNum  页码
     * @param pageSize 单页大小
     * @param user     任务受理人或候选人
     * @return list of task
     */
    public List<Task> queryTodo(int pageNum, int pageSize, String user);


    /**
     * 查询某人的经办任务实例编号
     *
     * @param pageNum  页码
     * @param pageSize 单页
     * @param user     任务受理人
     * @return ids of process instance
     */
    public List<String> queryDone(int pageNum, int pageSize, String user);


    /**
     * 某人完成代办任务
     *
     * @param user      任务受理人
     * @param taskId    任务编号
     * @param variables 任务变量
     * @return true or false
     */
    public boolean finishTask(String user, String taskId, Map<String, Object> variables);

}
