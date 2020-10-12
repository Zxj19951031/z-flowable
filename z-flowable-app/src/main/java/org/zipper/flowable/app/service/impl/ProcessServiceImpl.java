package org.zipper.flowable.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zipper.flowable.app.constant.enums.ProcessStatus;
import org.zipper.flowable.app.entity.Process;
import org.zipper.flowable.app.service.FlowableService;
import org.zipper.flowable.app.service.ProcessService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuxj
 * @since 2020/10/12
 */
@Service
public class ProcessServiceImpl implements ProcessService {


    @Resource
    private FlowableService flowableService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessService.class);

    /**
     * @param id   流程定义ID，可为空，非空即更新
     * @param name 流程名称，非空
     * @param xml  流程bpmn2.0标准内容
     * @return id
     */
    public long save(Long id, String name, String xml) {

        LOGGER.debug("新增/编辑流程{}\n{}", name, xml);

        return id;
    }

    /**
     * @param id   流程定义ID，可为空，非空即更新
     * @param name 流程名称，非空
     * @param xml  流程bpmn2.0标准内容
     * @return id
     */
    @Transactional
    public long saveAndDeploy(Long id, String name, String xml) {

        LOGGER.debug("新增/编辑并发布流程{}\n{}", name, xml);

        this.flowableService.deploy(name, xml);

        return id;
    }

    /**
     * @param name          流程名称
     * @param processStatus 流程状态
     * @return list of {@link Process}
     */
    @Override
    public List<Process> page(String name, ProcessStatus processStatus) {

        LOGGER.debug("查询流程列表");

        return null;
    }

    /**
     * @param ids 等删除ID列表
     * @return true or false
     */
    @Override
    public boolean delete(ArrayList<Integer> ids) {

        LOGGER.debug("删除记录");

        return false;
    }

}
