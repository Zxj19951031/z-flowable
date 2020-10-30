package org.zipper.flowable.app.mapper;

import org.zipper.flowable.app.dto.parameter.InstanceQueryParameter;
import org.zipper.flowable.app.entity.MyProcessInstance;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author zhuxj
 * @since 2020/10/28
 */
public interface ProcessInstanceMapper {
    /**
     * 新增一条流程实例记录
     * @param myInstance 流程实例
     * @return 受影响行数
     */
    int insert(MyProcessInstance myInstance);

    /**
     * 查询流程实例
     * @param parameter 查询参数
     * @return list of MyProcessInstance
     */
    List<MyProcessInstance> select(InstanceQueryParameter parameter);

    /**
     * 批量查询流程实例
     * @param instanceIds
     * @return
     */
    List<MyProcessInstance> selectInInstanceIds(Collection<String> instanceIds);
}
