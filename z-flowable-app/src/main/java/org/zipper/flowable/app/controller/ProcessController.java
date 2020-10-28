package org.zipper.flowable.app.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zipper.flowable.app.constant.enums.ProcessStatus;
import org.zipper.flowable.app.dto.parameter.ProcessQueryParameter;
import org.zipper.flowable.app.dto.parameter.ProcessSaveParameter;
import org.zipper.flowable.app.entity.MyProcess;
import org.zipper.flowable.app.service.ProcessService;
import org.zipper.helper.web.response.ResponseEntity;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程定义管理
 *
 * @author zhuxj
 * @since 2020/10/13
 */
@RestController
@RequestMapping(value = "process")
public class ProcessController {

    @Resource
    private ProcessService processService;


    /**
     * 新增/编辑流程
     *
     * @return id of the record
     */
    @PostMapping(value = "save")
    @PreAuthorize(value = "hasAuthority('process') and hasAnyAuthority('process_new','process_edit')")
    public ResponseEntity<Integer> save(@RequestBody ProcessSaveParameter parameter) {

        int id = processService.save(parameter);

        return ResponseEntity.success(id);
    }

    /**
     * 新增/编辑并发布流程
     *
     * @return id of the record
     */
    @PostMapping(value = "saveAndDeploy")
    @PreAuthorize(value = "hasAuthority('process') and hasAnyAuthority('process_new','process_edit') and hasAuthority('process_deploy')")
    public ResponseEntity<Integer> saveAndDeploy(@RequestBody ProcessSaveParameter parameter) {

        int id = processService.saveAndDeploy(parameter);

        return ResponseEntity.success(id);
    }


    /**
     * 查看分页列表
     *
     * @return 分页记录
     */
    @GetMapping(value = "page")
    @PreAuthorize(value = "hasAuthority('process') and hasAuthority('process_query')")
    public ResponseEntity<PageInfo<MyProcess>> page(@RequestParam Integer pageSize,
                                                    @RequestParam Integer pageNum,
                                                    @RequestParam(required = false) String name,
                                                    @RequestParam(required = false) ProcessStatus deployStatus) {
        PageHelper.startPage(pageNum, pageSize);
        ProcessQueryParameter parameter = new ProcessQueryParameter(name, deployStatus);
        List<MyProcess> myProcesses = this.processService.list(parameter);
        return ResponseEntity.success(new PageInfo<>(myProcesses));
    }

    /**
     * 批量删除
     *
     * @param ids 编号列表
     * @return true or false
     */
    @PostMapping(value = "del")
    @PreAuthorize(value = "hasAuthority('process') and hasAuthority('process_del')")
    public ResponseEntity<Integer> delete(@RequestBody ArrayList<Integer> ids) {

        int result = this.processService.delete(ids);

        return ResponseEntity.success(result);
    }


    /**
     * 发布流程
     *
     * @param id 流程编号
     * @return true or false
     */
    @PostMapping(value = "deploy")
    @PreAuthorize(value = "hasAuthority('process') and hasAuthority('process_deploy')")
    public ResponseEntity<Boolean> deploy(@RequestParam Integer id) {
        return ResponseEntity.success(processService.deploy(id));
    }
}
