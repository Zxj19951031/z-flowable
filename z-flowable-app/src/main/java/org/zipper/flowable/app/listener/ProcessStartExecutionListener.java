package org.zipper.flowable.app.listener;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zipper.flowable.app.constant.enums.InstanceStage;
import org.zipper.flowable.app.entity.MyProcessInstance;
import org.zipper.flowable.app.mapper.ProcessInstanceMapper;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 流程开始事件监听
 *
 * @author zhuxj
 * @since 2020/10/14
 */
@Component
public class ProcessStartExecutionListener implements ExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessStartExecutionListener.class);

    @Resource
    private RuntimeService runtimeService;
    @Resource
    private RepositoryService repositoryService;

    @Resource
    private ProcessInstanceMapper processInstanceMapper;

    /**
     * 真正监听到start事件后才进行
     * flow_process_instance 表数据新增
     */
    @Override
    public void notify(DelegateExecution delegateExecution) {
        String initiator = runtimeService.createProcessInstanceQuery()
                .processInstanceId(delegateExecution.getProcessInstanceId())
                .singleResult().getStartUserId();

        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(delegateExecution.getProcessDefinitionId())
                .singleResult();

        String processKey = definition.getKey();

        Map<String, Object> variables = delegateExecution.getVariables();

        MyProcessInstance myInstance = new MyProcessInstance(initiator, processKey, variables);
        myInstance.setInstanceId(delegateExecution.getProcessInstanceId());
        myInstance.setStage(InstanceStage.START);
        myInstance.setStartTime(LocalDateTime.now());
        this.processInstanceMapper.insert(myInstance);
        LOGGER.info("用户 [{}] 发起流程 [{}] 成功", initiator, definition.getName());
    }
}
