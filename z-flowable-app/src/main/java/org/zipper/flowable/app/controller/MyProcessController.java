package org.zipper.flowable.app.controller;

import com.github.pagehelper.PageHelper;
import org.flowable.task.api.Task;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zipper.flowable.app.constant.enums.InstanceStage;
import org.zipper.flowable.app.dto.parameter.InstanceQueryParameter;
import org.zipper.flowable.app.dto.parameter.ProcessInitiateParameter;
import org.zipper.flowable.app.dto.parameter.TaskFinishParameter;
import org.zipper.flowable.app.entity.MyProcess;
import org.zipper.flowable.app.entity.MyProcessInstance;
import org.zipper.flowable.app.security.AuthenticationUtil;
import org.zipper.flowable.app.service.FlowableService;
import org.zipper.flowable.app.service.ProcessService;
import org.zipper.helper.web.response.ResponseEntity;

import javax.annotation.Resource;
import java.util.List;

/**
 * 我的流程实例
 *
 * @author zhuxj
 * @since 2020/10/13
 */
@RestController
@RequestMapping(value = "myProcess")
public class MyProcessController {

    @Resource
    private ProcessService processService;
    @Resource
    private FlowableService flowableService;


    /**
     * 查询我可以发起的流程列表
     * 权限上只要能发起流程那么就可以访问该接口数据
     *
     * @return list of process
     */
    @GetMapping(value = "/allow/init/list")
    @PreAuthorize(value = "hasAuthority('myProcess_mine_init')")
    public ResponseEntity<List<MyProcess>> defineList() {
        String initiator = AuthenticationUtil.getAuthentication().getName();
        return ResponseEntity.success(processService.queryMyAllowInitProcess(initiator));
    }

    /**
     * 流程实例保存至草稿态
     * 权限上只要能访问"我的流程"且有"发起流程"权限即可存至草稿
     *
     * @return id of draft
     */
    @PostMapping(value = "/draft/save")
    @PreAuthorize(value = "hasAuthority('myProcess') and hasAuthority('myProcess_mine_init')")
    public ResponseEntity<Integer> saveDraft(@RequestBody ProcessInitiateParameter parameter) {
        String initiator = AuthenticationUtil.getAuthentication().getName();
        int result = this.processService.saveDraft(initiator, parameter.getProcessKey(), parameter.getVariables());
        return ResponseEntity.success(result);
    }

    /**
     * 发起流程
     *
     * @param parameter 流程变量
     * @return id of instance
     */
    @PostMapping(value = "initiate")
    @PreAuthorize(value = "hasAuthority('myProcess') and hasAuthority('myProcess_mine_init')")
    public ResponseEntity<String> initiate(@RequestBody ProcessInitiateParameter parameter) {
        String initiator = AuthenticationUtil.getAuthentication().getName();
        String result = this.processService.initiate(initiator, parameter.getProcessKey(), parameter.getVariables());
        return ResponseEntity.success(result);
    }

    /**
     * 我发起的
     * 分页查询
     *
     * @param pageSize 单页大小
     * @param pageNum  页码
     * @return list of my instance
     */
    @GetMapping(value = "mine/list")
    @PreAuthorize(value = "hasAuthority('myProcess') and hasAuthority('myProcess_mine_query')")
    public ResponseEntity<List<MyProcessInstance>> minePage(@RequestParam Integer pageSize,
                                                            @RequestParam Integer pageNum,
                                                            @RequestParam(required = false) InstanceStage stage) {

        String initiator = AuthenticationUtil.getAuthentication().getName();
        InstanceQueryParameter parameter = new InstanceQueryParameter(initiator, stage);
        PageHelper.startPage(pageNum, pageSize);
        List<MyProcessInstance> instances = this.processService.queryMine(parameter);
        return ResponseEntity.success(instances);
    }

    /**
     * 我的代办
     * 分页查询
     *
     * @param pageSize 单页大小
     * @param pageNum  页码
     * @return list of todo instance
     */
    @PostMapping(value = "todo/list")
    @PreAuthorize(value = "hasAuthority('myProcess') and hasAuthority('myProcess_todo_query')")
    public ResponseEntity<List<MyProcessInstance>> todoPage(@RequestParam Integer pageSize,
                                                            @RequestParam Integer pageNum) {

        String initiator = AuthenticationUtil.getAuthentication().getName();
        List<Task> tasks = this.flowableService.queryTodo(initiator);
        PageHelper.startPage(pageSize, pageNum);
        List<MyProcessInstance> instances = this.processService.transformTasks(tasks);
        return ResponseEntity.success(instances);
    }


    /**
     * 我的经办
     * 分页查询
     *
     * @param pageSize 单页大小
     * @param pageNum  页码
     * @return list of done instance
     */
    @PostMapping(value = "done/list")
    @PreAuthorize(value = "hasAuthority('myProcess') and hasAuthority('myProcess_done_query')")
    public ResponseEntity donePage(@RequestParam Integer pageSize,
                                   @RequestParam Integer pageNum) {

        String user = "zhuxj";
        this.flowableService.queryDone(pageNum, pageSize, user);
        return ResponseEntity.success(null);
    }

    /**
     * 完成用户任务
     *
     * @param parameter 任务参数包含任务编号和流程参数等
     * @return
     * @see TaskFinishParameter
     */
    @PostMapping(value = "finish")
    @PreAuthorize(value = "hasAuthority('myProcess') and hasAuthority('myProcess_todo_finish')")
    public ResponseEntity finish(@RequestBody TaskFinishParameter parameter) {

        String user = "zhuxj";
        this.flowableService.finishTask(user, parameter.getTaskId(), parameter.getLocalVariables());
        return ResponseEntity.success(null);
    }
}
