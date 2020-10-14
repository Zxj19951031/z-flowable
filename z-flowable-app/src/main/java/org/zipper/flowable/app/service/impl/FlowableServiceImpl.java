package org.zipper.flowable.app.service.impl;

import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zipper.flowable.app.constant.FlowableVariableKey;
import org.zipper.flowable.app.constant.errors.FlowableError;
import org.zipper.flowable.app.service.FlowableService;
import org.zipper.helper.exception.HelperException;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FlowableServiceImpl implements FlowableService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowableService.class);

    @Resource
    RepositoryService repositoryService;
    @Resource
    RuntimeService runtimeService;
    @Resource
    HistoryService historyService;
    @Resource
    IdentityService identityService;
    @Resource
    TaskService taskService;

    /**
     * @param name 流程名称
     * @param xml  流程Bpmn2.0定义字符串
     * @return Deployment
     */
    @Override
    public Deployment deploy(String name, String xml) {
        //资源名称以bpmn20.xml结尾Flowable才会校验xml内容否则部署失败
        Deployment deployment = this.repositoryService.createDeployment()
                .addString(String.format("%s.bpmn20.xml", name), xml)
                .deploy();
        LOGGER.debug("deploy process name:[{}] deployment-id:[{}] success ", name, deployment.getId());
        return deployment;
    }


    /**
     * @param initiator  流程发起方
     * @param processKey 流程关键字，即*.bpmn20.xml中process标签的id字段
     * @param variables  流程变量
     * @return ProcessInstance
     */
    @Override
    public ProcessInstance startProcess(String initiator, String processKey, Map<String, Object> variables) {

        ProcessDefinition definition = this.repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .latestVersion()
                .singleResult();
        if (definition == null) {
            throw HelperException.newException(FlowableError.PROCESS_DEFINITION_NOT_EXIST, "流程尚未部署");
        }

        variables.put(FlowableVariableKey.INITIATOR, initiator);
        this.identityService.setAuthenticatedUserId(initiator);
        ProcessInstance instance = this.runtimeService.startProcessInstanceByKey(processKey, variables);
        this.identityService.setAuthenticatedUserId(null);

        LOGGER.debug("start process instance by key [{}] success", processKey);
        return instance;
    }

    /**
     * @param pageNum   页码
     * @param pageSize  单页大小
     * @param initiator 发起人
     * @return list of {@link HistoricProcessInstance}
     */
    @Override
    public List<HistoricProcessInstance> queryMine(int pageNum, int pageSize, String initiator) {

        HistoricProcessInstanceQuery query = this.historyService.createHistoricProcessInstanceQuery();

        return query.startedBy(initiator)
                .orderByProcessInstanceStartTime()
                .desc()
                .listPage(pageSize * pageNum, pageSize);
    }


    /**
     * @param pageNum  页码
     * @param pageSize 单页大小
     * @param user     任务受理人或候选人
     * @return list of task
     */
    @Override
    public List<Task> queryTodo(int pageNum, int pageSize, String user) {
        TaskQuery query = this.taskService.createTaskQuery();

        return query.taskCandidateOrAssigned(user)
                .orderByTaskCreateTime()
                .desc()
                .listPage(pageSize * pageNum, pageSize);
    }

    /**
     * @param pageNum  页码
     * @param pageSize 单页
     * @param user     任务受理人
     * @return ids of process instance
     */
    @Override
    public List<String> queryDone(int pageNum, int pageSize, String user) {

        HistoricTaskInstanceQuery query = this.historyService.createHistoricTaskInstanceQuery();

        return query.taskAssignee(user)
                .finished()
                .orderByTaskCreateTime()
                .desc()
                .listPage(pageSize * pageNum, pageSize)
                .stream()
                .map(HistoricTaskInstance::getProcessInstanceId)
                .collect(Collectors.toList());
    }

    /**
     * @param user      任务受理人
     * @param taskId    任务编号
     * @param variables 任务变量
     * @return true or false
     */
    @Override
    public boolean finishTask(String user, String taskId, Map<String, Object> variables) {

        this.taskService.setAssignee(taskId, user);
        this.taskService.complete(taskId, variables, true);
        return true;
    }

}
