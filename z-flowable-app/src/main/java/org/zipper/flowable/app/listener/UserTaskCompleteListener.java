package org.zipper.flowable.app.listener;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 用户任务 complete 事件监听器
 * assignment->create->complete->delete
 *
 * @author zhuxj
 * @since 2020/10/13
 */
@Component
public class UserTaskCompleteListener implements TaskListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserTaskCompleteListener.class);

    /**
     * 用户任务complete事件，在delete前触发
     * 由{@link org.flowable.engine.TaskService#complete(String, Map, boolean)}递交任务变量Map
     * {@link DelegateTask#getVariables()}获取全部变量
     * {@link DelegateTask#getVariablesLocal()}获取任务递交变量对应上文Map
     *
     * @param delegateTask 委派任务
     */
    @Override
    public void notify(DelegateTask delegateTask) {

        LOGGER.debug("user:[{}] task completed!", delegateTask.getAssignee());
    }

}