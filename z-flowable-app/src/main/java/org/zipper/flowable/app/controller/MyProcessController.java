package org.zipper.flowable.app.controller;

import org.springframework.web.bind.annotation.*;
import org.zipper.flowable.app.dto.ProcessInitiateParameter;
import org.zipper.flowable.app.dto.TaskFinishParameter;
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
    public ResponseEntity<Boolean> initiate(@RequestBody ProcessInitiateParameter parameter) {

        String initiator = AuthenticationUtil.getAuthentication().getName();
        boolean result = this.processService.initiate(initiator, parameter.getProcessKey(), parameter.getVariables());
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
    @PostMapping(value = "mine/list")
    public ResponseEntity minePage(@RequestParam Integer pageSize,
                                   @RequestParam Integer pageNum) {

        String initiator = "zhuxj";
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
    public ResponseEntity finish(@RequestBody TaskFinishParameter parameter) {

        String user = "zhuxj";
        this.flowableService.finishTask(user, parameter.getTaskId(), parameter.getLocalVariables());
        return ResponseEntity.success(null);
    }
}
