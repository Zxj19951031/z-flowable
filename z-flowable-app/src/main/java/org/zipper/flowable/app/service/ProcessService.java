package org.zipper.flowable.app.service;


import org.zipper.flowable.app.dto.parameter.ProcessQueryParameter;
import org.zipper.flowable.app.dto.parameter.ProcessSaveParameter;
import org.zipper.flowable.app.entity.Process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * @param processSaveParameter {@link ProcessSaveParameter} 参数
     * @return 流程定义记录ID
     * @see ProcessSaveParameter
     */
    int save(ProcessSaveParameter processSaveParameter);

    /**
     * 保存流程定义
     *
     * @param id   流程定义记录ID
     * @param name 流程名称，非空
     * @param xml  流程bpmn2.0标准内容
     * @return id
     */
    int save(Integer id, String name, String xml, Integer formId);


    /**
     * 保存并发布流程
     *
     * @param parameter {@link ProcessSaveParameter} 参数
     * @return id
     */
    int saveAndDeploy(ProcessSaveParameter parameter);

    /**
     * 保存并发布流程
     *
     * @param id   流程定义记录ID
     * @param name 流程名称，非空
     * @param xml  流程bpmn2.0标准内容
     * @return id
     */
    int saveAndDeploy(Integer id, String name, String xml, Integer formId);

    /**
     * 查看流程列表
     *
     * @param parameter 查询参数{@link ProcessQueryParameter}
     * @return 流程列表
     * @see ProcessQueryParameter
     */
    List<Process> list(ProcessQueryParameter parameter);

    /**
     * 批量删除
     *
     * @param ids 等删除ID列表
     * @return 成功删除记录数
     */
    int delete(ArrayList<Integer> ids);


    /**
     * 发起流程
     *
     * @param initiator  发起人
     * @param processKey 流程定义key即xml中process标签的id属性
     * @param variables  流程变量
     * @return true or false
     */
    boolean initiate(String initiator, String processKey, Map<String, Object> variables);


    /**
     * 发布流程
     *
     * @param id processId 流程编号
     * @return
     */
    boolean deploy(int id);
}
