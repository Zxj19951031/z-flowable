package org.zipper.flowable.app.service;

import java.util.List;

/**
 * 候选人查询
 *
 * @author zhuxj
 * @since 2020/10/14
 */
public interface AssigneeService {

    /**
     * 发起人的直接主管用户编号
     *
     * @param initiator 任务发起人
     * @return userid(s)
     */
    public List<String> getDirectSupervisor(String initiator);
}
