package org.zipper.flowable.app.mapper;

import org.zipper.flowable.app.entity.MyProcessInstance;

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
}
