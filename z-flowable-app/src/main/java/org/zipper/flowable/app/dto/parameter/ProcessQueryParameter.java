package org.zipper.flowable.app.dto.parameter;

import org.zipper.flowable.app.constant.enums.ProcessStatus;
import org.zipper.flowable.app.constant.enums.Status;

/**
 * @author zhuxj
 * @since 2020/10/26
 */
public class ProcessQueryParameter{
    private String name;
    private ProcessStatus deployStatus;

    public ProcessQueryParameter(String name, ProcessStatus processStatus) {
        this.name = name;
        this.deployStatus = processStatus;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProcessStatus getDeployStatus() {
        return deployStatus;
    }

    public void setDeployStatus(ProcessStatus deployStatus) {
        this.deployStatus = deployStatus;
    }
}
