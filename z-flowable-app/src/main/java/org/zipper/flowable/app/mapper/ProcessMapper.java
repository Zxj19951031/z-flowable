package org.zipper.flowable.app.mapper;

import org.apache.ibatis.annotations.Param;
import org.zipper.flowable.app.constant.enums.ProcessStatus;
import org.zipper.flowable.app.dto.parameter.ProcessQueryParameter;
import org.zipper.flowable.app.entity.Process;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuxj
 * @since 2020/10/26
 */
public interface ProcessMapper {
    /**
     * 新增一条流程定义记录
     *
     * @param process 流程定义{@link Process}
     * @return 受影响行数
     */
    int insert(Process process);

    /**
     * 查询一个流程定义记录
     *
     * @param id 流程定义编号
     * @return 流程定义
     */
    Process selectById(Integer id);

    /**
     * 更新一条流程定义记录的name和xml字段
     *
     * @param process 流程定义{@link Process}
     * @return 受影响行数
     */
    int updateById(Process process);

    /**
     * 根据参数查询流程定义列表
     *
     * @param parameter 查询参数{@link ProcessQueryParameter}
     * @return list of process
     */
    List<Process> selectByParameter(ProcessQueryParameter parameter);

    /**
     * 逻辑删除流程
     *
     * @param ids 待删除的流程编号
     * @return 受影响行数
     */
    int delete(@Param("ids") ArrayList<Integer> ids);

    /**
     * 更新发布状态
     *
     * @param processStatus 发布状态 {@link ProcessStatus}
     * @param id            记录编号
     * @return 受影响行数
     */
    int updateDeployStatus(@Param("processStatus") ProcessStatus processStatus, @Param("id") Integer id);

    /**
     * 通过Key查询记录数，不区分status
     *
     * @param key processKey
     * @return cnt
     */
    int selectCntByKey(String key);

    /**
     * 查询符合当前发起身份的流程列表
     *
     * @param identities 身份特征
     * @return 流程列表
     */
    List<Process> selectByAllowInitiator(@Param("identities") List<String> identities);
}
