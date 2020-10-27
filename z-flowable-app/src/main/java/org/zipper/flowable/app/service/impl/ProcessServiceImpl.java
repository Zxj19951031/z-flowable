package org.zipper.flowable.app.service.impl;

import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zipper.flowable.app.constant.enums.ProcessStatus;
import org.zipper.flowable.app.constant.errors.SystemError;
import org.zipper.flowable.app.dto.parameter.ProcessQueryParameter;
import org.zipper.flowable.app.dto.parameter.ProcessSaveParameter;
import org.zipper.flowable.app.entity.Process;
import org.zipper.flowable.app.mapper.ProcessMapper;
import org.zipper.flowable.app.service.FlowableService;
import org.zipper.flowable.app.service.ProcessService;
import org.zipper.helper.exception.HelperException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


    public int save(ProcessSaveParameter parameter) {
        if (parameter == null) {
            throw HelperException.newException(SystemError.PARAMETER_ERROR, "ProcessSaveParameter cannot be null");
        }
        return save(parameter.getId(), parameter.getName(), parameter.getXml(), parameter.getFormId());
    }

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
     * @param id   流程定义记录ID
     * @param name 流程名称，非空
     * @param xml  流程bpmn2.0标准内容
     * @return ID of 流程定义
     */
    public int save(Integer id, String name, String xml, Integer formId) {

        if (name == null || "".equals(name)) {
            throw HelperException.newException(SystemError.PARAMETER_ERROR, "流程名称不可为空，请输入正确的流程名称");
        }
        if (xml == null || "".equals(xml)) {
            throw HelperException.newException(SystemError.PARAMETER_ERROR, "xml of process cannot be null or empty");
        }
        if (formId == null) {
            throw HelperException.newException(SystemError.PARAMETER_ERROR, "流程表单未绑定，请进行表单绑定");
        }

        Process process = null;
        String key = flowableService.getMainProcessKey(xml);
        if (id == null) {
            if (processMapper.selectCntByKey(key) > 0) {
                throw HelperException.newException(SystemError.PARAMETER_ERROR, "流程关键字已存在，请重新填写");
            }

            LOGGER.info("新增流程{}", name);
            LOGGER.debug(xml);

            process = new Process(name, key, xml, formId);
            processMapper.insert(process);
        } else {
            LOGGER.info("更新流程{}", name);
            LOGGER.debug(xml);

            process = processMapper.selectById(id);
            if (process == null) {
                throw HelperException.newException(SystemError.PARAMETER_ERROR, "该流程不存在，请确认");
            }
            if (!process.getProcessKey().equals(key)) {
                throw HelperException.newException(SystemError.PARAMETER_ERROR, "流程关键字不可修改");
            }
            process.setName(name);
            process.setXml(xml);

            processMapper.updateById(process);
        }

        return process.getId();
    }

    @Transactional
    public int saveAndDeploy(ProcessSaveParameter parameter) {
        if (parameter == null) {
            throw HelperException.newException(SystemError.PARAMETER_ERROR, "ProcessSaveParameter cannot be null");
        }
        return saveAndDeploy(parameter.getId(), parameter.getName(), parameter.getXml(), parameter.getFormId());
    }

    @Transactional
    public int saveAndDeploy(Integer id, String name, String xml, Integer formId) {
        int result = save(id, name, xml, formId);
        deploy(id);
        return result;
    }

    public List<Process> list(ProcessQueryParameter parameter) {

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

    public boolean initiate(String initiator, String processKey, Map<String, Object> variables) {

        ProcessInstance instance = this.flowableService.startProcess(initiator, processKey, variables);
        LOGGER.debug("用户[{}]发起流程 [{}] 成功", initiator, instance.getName());
        return true;
    }

    @Override
    @Transactional
    public boolean deploy(int id) {
        LOGGER.info("发布流程{}", id);
        Process process = processMapper.selectById(id);
        if (process == null) {
            throw HelperException.newException(SystemError.PARAMETER_ERROR, "该流程不存在，请确认");
        }
        Deployment deployment = this.flowableService.deploy(process.getName(), process.getXml());
        processMapper.updateDeployStatus(ProcessStatus.PUBLISHED, id);
        return deployment != null;
    }

}
