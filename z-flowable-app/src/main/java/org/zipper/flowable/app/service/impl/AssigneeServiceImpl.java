package org.zipper.flowable.app.service.impl;

import org.springframework.stereotype.Service;
import org.zipper.flowable.app.service.AssigneeService;

import java.util.List;

/**
 * @author zhuxj
 * @since 2020/10/14
 */
@Service
public class AssigneeServiceImpl implements AssigneeService {

    /**
     * @param initiator 任务发起人
     * @return 直接主管编号
     */
    @Override
    public List<String> getDirectSupervisor(String initiator) {
        return null;
    }
}
