package org.zipper.flowable.app.service;


import org.springframework.transaction.annotation.Transactional;
import org.zipper.flowable.app.constant.enums.ProcessStatus;
import org.zipper.flowable.app.entity.Process;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程定义服务
 *
 * @author zhuxj
 * @since 2020/09/29
 */
public interface ProcessService {


    /**
     * 保存流程定义
     *
     * @param id   流程定义ID，可为空，非空即更新
     * @param name 流程名称，非空
     * @param xml  流程bpmn2.0标准内容
     * @return id
     */
    long save(Long id, String name, String xml);

    /**
     * 保存并发布流程
     *
     * @param id   流程定义ID，可为空，非空即更新
     * @param name 流程名称，非空
     * @param xml  流程bpmn2.0标准内容
     * @return id
     */
    long saveAndDeploy(Long id, String name, String xml);

    /**
     * 查看流程列表
     *
     * @param name          流程名称
     * @param processStatus 流程状态
     * @return 流程列表
     * @see ProcessStatus
     */
    List<Process> page(String name, ProcessStatus processStatus);

    /**
     * 批量删除
     *
     * @param ids 等删除ID列表
     * @return true or false
     */
    boolean delete(ArrayList<Integer> ids);
}
