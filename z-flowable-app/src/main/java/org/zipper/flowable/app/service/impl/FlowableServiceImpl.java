package org.zipper.flowable.app.service.impl;

import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.common.engine.impl.util.io.StringStreamSource;
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
import org.zipper.flowable.app.constant.SystemException;
import org.zipper.flowable.app.constant.errors.FlowableError;
import org.zipper.flowable.app.service.FlowableService;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    @Override
    public Deployment deploy(String name, String xml) {
        //资源名称以bpmn20.xml结尾Flowable才会校验xml内容否则部署失败
        Deployment deployment = this.repositoryService.createDeployment()
                .addString(String.format("%s.bpmn20.xml", name), xml)
                .deploy();
        LOGGER.debug("deploy process name:[{}] deployment-id:[{}] success ", name, deployment.getId());
        return deployment;
    }


    @Override
    public ProcessInstance startProcess(String initiator, String processKey, Map<String, Object> variables) {

        ProcessDefinition definition = this.repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .latestVersion()
                .singleResult();
        if (definition == null) {
            throw SystemException.newException(FlowableError.PROCESS_DEFINITION_NOT_EXIST, "流程尚未部署");
        }

        variables.put(FlowableVariableKey.INITIATOR, initiator);
        this.identityService.setAuthenticatedUserId(initiator);
        ProcessInstance instance = this.runtimeService.startProcessInstanceByKey(processKey, variables);
        this.identityService.setAuthenticatedUserId(null);

        LOGGER.debug("start process instance by key [{}] success", processKey);
        return instance;
    }

    @Override
    public List<HistoricProcessInstance> queryMine(int pageNum, int pageSize, String initiator) {

        HistoricProcessInstanceQuery query = this.historyService.createHistoricProcessInstanceQuery();

        return query.startedBy(initiator)
                .orderByProcessInstanceStartTime()
                .desc()
                .listPage(pageSize * pageNum, pageSize);
    }

    /**
     * @param user 任务受理人或候选人
     * @return list of task
     */
    @Override
    public List<Task> queryTodo(String user) {
        TaskQuery query = this.taskService.createTaskQuery();

        return query.taskCandidateOrAssigned(user).list();
    }

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


    @Override
    public boolean finishTask(String user, String taskId, Map<String, Object> variables) {

        this.taskService.setAssignee(taskId, user);
        this.taskService.complete(taskId, variables, true);
        return true;
    }

    @Override
    public String getMainProcessKey(String xml) {
        try {
            StringStreamSource streamSource = new StringStreamSource(xml);
            BpmnXMLConverter converter = new BpmnXMLConverter();
            BpmnModel model = converter.convertToBpmnModel(streamSource, true, true);
            return model.getMainProcess().getId();
        } catch (Throwable throwable) {
            LOGGER.error("解析xml至BpmnModel异常", throwable);
            throw SystemException.newException(FlowableError.XML_FORMAT_ERROR, "流程配置有误，请检查");
        }
    }


}
