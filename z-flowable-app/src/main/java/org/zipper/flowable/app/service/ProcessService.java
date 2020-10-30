package org.zipper.flowable.app.service;


import org.flowable.task.api.Task;
import org.zipper.flowable.app.dto.parameter.InstanceQueryParameter;
import org.zipper.flowable.app.dto.parameter.ProcessQueryParameter;
import org.zipper.flowable.app.dto.parameter.ProcessSaveParameter;
import org.zipper.flowable.app.entity.MyProcess;
import org.zipper.flowable.app.entity.MyProcessInstance;

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
     * 保存并发布流程
     *
     * @param parameter {@link ProcessSaveParameter} 参数
     * @return id
     */
    int saveAndDeploy(ProcessSaveParameter parameter);

    /**
     * 查看流程列表
     *
     * @param parameter 查询参数{@link ProcessQueryParameter}
     * @return 流程列表
     * @see ProcessQueryParameter
     */
    List<MyProcess> list(ProcessQueryParameter parameter);

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
     * @return
     */
    String initiate(String initiator, String processKey, Map<String, Object> variables);


    /**
     * 发布流程
     *
     * @param id processId 流程编号
     * @return
     */
    boolean deploy(int id);


    /**
     * 获取可以发起该流程的所有规则
     *
     * @param anybody     是否任何人可发起 ANYBODY
     * @param roles       某些角色可发起流程 ROLE_1,ROLE_2
     * @param members     某些人可发起流程 MEMBER_1,MEMBER_2
     * @param departments 某些部门可发起流程 DEPT_1,DEPT_2
     * @return allowInitiators
     */
    String buildAllowInitiator(boolean anybody, List<String> roles, List<String> members, List<String> departments);

    /**
     * 查询某个用户可发起的流程列表
     *
     * @param userId 用户编号
     * @return 流程列表
     */
    List<MyProcess> queryMyAllowInitProcess(String userId);

    /**
     * 查询某人发起的审批
     *
     * @param parameter 发起人
     * @return list of process instance
     */
    List<MyProcessInstance> queryMine(InstanceQueryParameter parameter);

    /**
     * 保存流程实例至草稿态
     *
     * @param initiator  发起人
     * @param processKey 流程定义关键字
     * @param variables  流程发起变量
     * @return id of draft
     */
    int saveDraft(String initiator, String processKey, Map<String, Object> variables);

    /**
     * 将flowable中查询出来的待办任务进行包装成流程实例记录
     * @param tasks 待办任务
     * @return list of process instance
     */
    List<MyProcessInstance> transformTasks(List<Task> tasks);
}
