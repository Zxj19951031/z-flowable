package org.zipper.flowable.app.dto.parameter;

import org.zipper.flowable.app.constant.enums.InstanceStage;

/**
 * 实例列表查询条件
 *
 * @author zhuxj
 * @since 2020/10/29
 */
public class InstanceQueryParameter {

    /**
     * 流程发起人名称
     */
    private String initiator;
    /**
     * 流程实例阶段状态
     */
    private InstanceStage stage;


    public InstanceQueryParameter(String initiator, InstanceStage stage) {
        this.initiator = initiator;
        this.stage = stage;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public InstanceStage getStage() {
        return stage;
    }

    public void setStage(InstanceStage stage) {
        this.stage = stage;
    }
}
