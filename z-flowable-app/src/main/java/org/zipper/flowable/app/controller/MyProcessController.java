package org.zipper.flowable.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zipper.flowable.app.dto.parameter.ProcessInitiateParameter;
import org.zipper.flowable.app.dto.parameter.TaskFinishParameter;
import org.zipper.flowable.app.security.AuthenticationUtil;
import org.zipper.flowable.app.service.FlowableService;
import org.zipper.flowable.app.service.ProcessService;
import org.zipper.helper.web.response.ResponseEntity;

import javax.annotation.Resource;

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
     * 发起流程
     *
     * @param parameter 流程变量
     * @return true or false
     */
    @PostMapping(value = "initiate")
    @PreAuthorize(value = "hasAuthority('myProcess') and hasAuthority('myProcess_init')")
    public ResponseEntity<String> initiate(@RequestBody ProcessInitiateParameter parameter) {

        String initiator = AuthenticationUtil.getAuthentication().getName();
        this.processService.initiate(initiator, parameter.getProcessKey(), parameter.getVariables());
        return ResponseEntity.success("success");
    }

    /**
     * 我发起的
     * 分页查询
     *
     * @param pageSize 单页大小
     * @param pageNum  页码
     * @return list of my instance
     */
    @PostMapping(value = "mine/list")
    @PreAuthorize(value = "hasAuthority('myProcess') and hasAuthority('myProcess_mine_query')")
    public ResponseEntity minePage(@RequestParam Integer pageSize,
                                   @RequestParam Integer pageNum) {

        String initiator = AuthenticationUtil.getAuthentication().getName();
        this.flowableService.queryMine(pageNum, pageSize, initiator);
        return ResponseEntity.success(null);
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
    public ResponseEntity todoPage(@RequestParam Integer pageSize,
                                   @RequestParam Integer pageNum) {
        String user = "zhuxj";
        this.flowableService.queryTodo(pageNum, pageSize, user);
        return ResponseEntity.success(null);
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
