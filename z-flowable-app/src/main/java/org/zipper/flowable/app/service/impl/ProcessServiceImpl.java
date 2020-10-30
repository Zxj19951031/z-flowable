package org.zipper.flowable.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zipper.flowable.app.constant.enums.AllowInitiatorType;
import org.zipper.flowable.app.constant.enums.InstanceStage;
import org.zipper.flowable.app.constant.enums.ProcessStatus;
import org.zipper.flowable.app.constant.errors.SystemError;
import org.zipper.flowable.app.dto.parameter.InstanceQueryParameter;
import org.zipper.flowable.app.dto.parameter.ProcessQueryParameter;
import org.zipper.flowable.app.dto.parameter.ProcessSaveParameter;
import org.zipper.flowable.app.entity.Member;
import org.zipper.flowable.app.entity.MyProcess;
import org.zipper.flowable.app.entity.MyProcessInstance;
import org.zipper.flowable.app.entity.Role;
import org.zipper.flowable.app.mapper.AuthenticationMapper;
import org.zipper.flowable.app.mapper.ProcessInstanceMapper;
import org.zipper.flowable.app.mapper.ProcessMapper;
import org.zipper.flowable.app.mapper.RoleMapper;
import org.zipper.flowable.app.service.FlowableService;
import org.zipper.flowable.app.service.ProcessService;
import org.zipper.helper.exception.HelperException;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhuxj
 * @since 2020/10/12
 */
@Service
public class ProcessServiceImpl implements ProcessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessService.class);

    @Resource
    private FlowableService flowableService;

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private AuthenticationMapper authenticationMapper;

    @Resource
    private ProcessInstanceMapper processInstanceMapper;

    /**
     * 新增流程：
     * 流程的关键字不可以于现有的流程的关键字重复，因为flowable通过关键字去区分流程定义
     * 新增的流程定义key不能重复，重复的Key会被认定为同一个流程的不同版本，会影响到历史记录查询
     * 对于先删除后新增的场景，也不允许key值重复，否则在用户看来刚新增的流程就有历史记录会有起义
     * 其实对于只新增但不发布的流程，key可以重复，但是到了发布时仍然需要保障key不冲突，所以在新增时就做了限制
     * <p>
     * 更新流程：
     * 同理更新时key是不让改动的，否则就会被flowable视为另一个流程，造成数据错乱
     * 当让对于未发布过的流程进行更新是可以允许key重复的，但是发布时还是校验key不冲突，所以在更新是就做了限制
     *
     * @param parameter {@link ProcessSaveParameter}
     * @return ID of 流程定义
     */
    public int save(ProcessSaveParameter parameter) {
        if (parameter == null) {
            throw HelperException.newException(SystemError.PARAMETER_ERROR, "ProcessSaveParameter cannot be null");
        }
        if (parameter.getName() == null || "".equals(parameter.getName())) {
            throw HelperException.newException(SystemError.PARAMETER_ERROR, "流程名称不可为空，请输入正确的流程名称");
        }
        if (parameter.getXml() == null || "".equals(parameter.getXml())) {
            throw HelperException.newException(SystemError.PARAMETER_ERROR, "xml of process cannot be null or empty");
        }
        if (parameter.getFormId() == null) {
            throw HelperException.newException(SystemError.PARAMETER_ERROR, "流程表单未绑定，请进行表单绑定");
        }

        /*
            如果都没有设置发起人限制则默认是任何人
         */
        if (!parameter.getAllowAnybody()) {
            boolean allowRole = parameter.getAllowRole() != null && parameter.getAllowRole().size() > 0;
            boolean allowMember = parameter.getAllowMember() != null && parameter.getAllowMember().size() > 0;
            boolean allowDept = parameter.getAllowDept() != null && parameter.getAllowDept().size() > 0;
            LOGGER.debug("allowRole:{},allowMember:{},allowDept:{}", allowRole, allowMember, allowDept);
            if (!allowRole && !allowMember && !allowDept) {
                parameter.setAllowAnybody(true);
                LOGGER.warn("检查流程:{} 未设置发起人范围，将默认设置为任何人可发起", parameter.getName());
            }
        }


        MyProcess myProcess = null;
        String key = flowableService.getMainProcessKey(parameter.getXml());
        if (parameter.getId() == null) {
            if (processMapper.selectCntByKey(key) > 0) {
                throw HelperException.newException(SystemError.PARAMETER_ERROR, "流程关键字已存在，请重新填写");
            }

            LOGGER.info("新增流程{}", parameter.getName());
            LOGGER.debug(parameter.getXml());

            myProcess = new MyProcess(parameter.getName(), key, parameter.getXml(), parameter.getFormId());
            myProcess.setAllowInitiator(buildAllowInitiator(parameter.getAllowAnybody(), parameter.getAllowRole(), parameter.getAllowMember(), parameter.getAllowDept()));
            processMapper.insert(myProcess);
        } else {
            LOGGER.info("更新流程{}", parameter.getName());
            LOGGER.debug(parameter.getXml());

            myProcess = processMapper.selectById(parameter.getId());
            if (myProcess == null) {
                throw HelperException.newException(SystemError.PARAMETER_ERROR, "该流程不存在，请确认");
            }
            if (!myProcess.getProcessKey().equals(key)) {
                throw HelperException.newException(SystemError.PARAMETER_ERROR, "流程关键字不可修改");
            }

            myProcess.setName(parameter.getName());
            myProcess.setXml(parameter.getXml());
            myProcess.setAllowInitiator(buildAllowInitiator(parameter.getAllowAnybody(), parameter.getAllowRole(), parameter.getAllowMember(), parameter.getAllowDept()));
            processMapper.updateById(myProcess);
        }

        return myProcess.getId();

    }

    @Transactional
    public int saveAndDeploy(ProcessSaveParameter parameter) {
        int id = save(parameter);
        deploy(id);
        return id;
    }

    @Transactional
    public boolean deploy(int id) {
        LOGGER.info("发布流程{}", id);
        MyProcess myProcess = processMapper.selectById(id);
        if (myProcess == null) {
            throw HelperException.newException(SystemError.PARAMETER_ERROR, "该流程不存在，请确认");
        }
        Deployment deployment = this.flowableService.deploy(myProcess.getName(), myProcess.getXml());
        processMapper.updateDeployStatus(ProcessStatus.PUBLISHED, id);
        return deployment != null;
    }

    public List<MyProcess> list(ProcessQueryParameter parameter) {

        LOGGER.debug("查询流程列表");

        return processMapper.selectByParameter(parameter);
    }

    public int delete(ArrayList<Integer> ids) {

        if (ids == null || ids.size() == 0) {
            throw HelperException.newException(SystemError.PARAMETER_ERROR, "请至少选择一个待删除的流程");
        }
        /*
            删除流程定义只是将记录删除
            如果流程已经部署了，删除不会对flowable有影响
            否则一旦删除了，还没走完的流程就出问题了
         */
        LOGGER.debug("删除记录");
        int cnt = processMapper.delete(ids);
        LOGGER.info("删除流程记录{}条", cnt);
        return cnt;
    }

    /**
     * 发起流程实例，针对 flow_process_instance 表的维护交由流程的启动监听器去实现
     * 否则此处不太好保证顺序，可能会有流程启动后 flow_process_instance 表中记录尚没有新建
     * 导致流程去更新stage状态时失败；
     * 如果放在 {@code flowableService.startProcess} 之前去新增记录的化此时并不知道关联的
     * flowable中process_instance_id，又要在{@code flowableService.startProcess}之后
     * 去更新这个记录造成可能会造成死锁。
     *
     * @param initiator  发起人
     * @param processKey 流程定义key即xml中process标签的id属性
     * @param variables  流程变量
     * @return flowable process instance id
     */
    @Transactional
    public String initiate(String initiator, String processKey, Map<String, Object> variables) {
        LOGGER.debug("用户 [{}] 发起流程 [{}] 参数 [{}]", initiator, processKey,
                JSONObject.toJSONString(variables, true));
        ProcessInstance instance = this.flowableService.startProcess(initiator, processKey, variables);
        return instance.getProcessInstanceId();
    }

    public String buildAllowInitiator(boolean anybody, List<String> roles, List<String> members, List<String> departments) {
        if (anybody) {
            return AllowInitiatorType.ANYBODY.name();
        }
        String[] allowInitiator = new String[3];

        if (roles != null) {
            allowInitiator[0] = StringUtils.join(roles.stream().map(r -> {
                return String.format("%s_%s", AllowInitiatorType.ROLE.name(), r);
            }).collect(Collectors.toList()), ",");
        }

        if (members != null) {
            allowInitiator[1] = StringUtils.join(members.stream().map(m -> {
                return String.format("%s_%s", AllowInitiatorType.MEMBER.name(), m);
            }).collect(Collectors.toList()), ",");
        }

        if (departments != null) {
            allowInitiator[2] = StringUtils.join(departments.stream().map(d -> {
                return String.format("%s_%s", AllowInitiatorType.DEPT.name(), d);
            }).collect(Collectors.toList()), ",");
        }

        return StringUtils.join(Arrays.stream(allowInitiator)
                .filter(l -> l != null && !l.equals("")).collect(Collectors.toList()), ",");
    }

    public List<MyProcess> queryMyAllowInitProcess(String username) {
        Member member = authenticationMapper.selectByUsernameEqual(username);
        if (member == null) {
            throw HelperException.newException(SystemError.PARAMETER_ERROR, "当前用户不存在，请确认登录状态");
        }

        List<String> identities = new ArrayList<>();
        identities.add(String.format("%s_%s", AllowInitiatorType.MEMBER.name(), member.getId()));

        List<Role> roles = roleMapper.selectByUserId(String.valueOf(member.getId()));
        for (Role role : roles) {
            identities.add(String.format("%s_%s", AllowInitiatorType.ROLE.name(), role.getId()));
        }

        LOGGER.debug("当前用户身份有：{}", StringUtils.join(identities));

        return processMapper.selectByAllowInitiator(identities);

    }

    public List<MyProcessInstance> queryMine(InstanceQueryParameter parameter) {
        return this.processInstanceMapper.select(parameter);
    }

    public int saveDraft(String initiator, String processKey, Map<String, Object> variables) {
        MyProcessInstance instance = new MyProcessInstance(initiator, processKey, variables);
        instance.setStage(InstanceStage.DRAFT);
        this.processInstanceMapper.insert(instance);
        LOGGER.info("用户 [{}] 暂存流程 [{}] 至草稿", initiator, processKey);
        return instance.getId();
    }

    public List<MyProcessInstance> transformTasks(List<Task> tasks) {
        if (tasks == null || tasks.size() == 0) {
            LOGGER.warn("tasks is null or empty");
            return null;
        }

        Set<String> instanceIds = tasks.stream().map(TaskInfo::getProcessInstanceId).collect(Collectors.toSet());
        LOGGER.info("等待包装的任务列表长度为 [{}],对应流程实例数 [{}]", tasks.size(), instanceIds.size());
        return this.processInstanceMapper.selectInInstanceIds(instanceIds);
    }
}
