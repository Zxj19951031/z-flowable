package org.zipper.flowable.app.service.impl;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zipper.flowable.app.service.FlowableService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class FlowableServiceImpl implements FlowableService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowableService.class);

    @Resource
    ProcessEngine processEngine;

    @Override
    public Deployment deploy(String name, String xml) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addString(name, xml)
                .deploy();
        LOGGER.debug("deploy process [{}] success ", name);
        return deployment;
    }

    @Override
    public void startProcess(String initiator, String processKey, Map<String, Object> variables) {

    }

    @Override
    public List<Task> getTasks(String assignee) {
        return null;
    }
}
