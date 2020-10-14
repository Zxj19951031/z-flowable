package org.zipper.flowable.app.listener;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 流程结束事件监听
 *
 * @author zhuxj
 * @since 2020/10/14
 */
@Component
public class ProcessEndExecutionListener implements ExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessEndExecutionListener.class);

    @Resource
    private RepositoryService repositoryService;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(delegateExecution.getProcessDefinitionId())
                .singleResult();
        LOGGER.debug("process:[{}] end success", definition.getName());
    }
}
