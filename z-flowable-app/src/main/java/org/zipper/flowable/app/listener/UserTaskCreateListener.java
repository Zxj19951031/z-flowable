package org.zipper.flowable.app.listener;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 用户任务 create 事件监听器
 * assignment->create->complete->delete
 *
 * @author zhuxj
 * @since 2020/10/13
 */
@Component
public class UserTaskCreateListener implements TaskListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserTaskCreateListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        LOGGER.debug("user:[{}] task created!", delegateTask.getAssignee());
    }
}
