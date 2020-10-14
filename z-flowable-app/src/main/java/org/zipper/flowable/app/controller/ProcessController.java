package org.zipper.flowable.app.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.zipper.flowable.app.constant.enums.ProcessStatus;
import org.zipper.flowable.app.dto.ProcessSaveParameter;
import org.zipper.flowable.app.entity.Process;
import org.zipper.flowable.app.service.ProcessService;
import org.zipper.helper.web.response.ResponseEntity;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程定义管理
 *
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
    public ResponseEntity<Long> save(@RequestBody ProcessSaveParameter parameter) {

        long id = processService.save(parameter.getId(), parameter.getName(), parameter.getXml());

        return ResponseEntity.success(id);
    }

    /**
     * 新增/编辑并发布流程
     *
     * @return id of the record
     */
    @PostMapping(value = "saveAndDeploy")
    public ResponseEntity<Long> saveAndDeploy(@RequestBody ProcessSaveParameter parameter) {

        long id = processService.saveAndDeploy(parameter.getId(), parameter.getName(), parameter.getXml());

        return ResponseEntity.success(id);
    }


    /**
     * 查看分页列表
     *
     * @return 分页记录
     */
    @GetMapping(value = "page")
    public ResponseEntity<PageInfo<Process>> page(@RequestParam Integer pageSize,
                                                  @RequestParam Integer pageNum,
                                                  @RequestParam String name,
                                                  @RequestParam Integer processStatus) {
        PageHelper.startPage(pageNum, pageSize);
        List<Process> processes = this.processService.page(name, ProcessStatus.get(processStatus));
        return ResponseEntity.success(new PageInfo<>(processes));
    }

    /**
     * 批量删除
     *
     * @param ids 编号列表
     * @return true or false
     */
    @DeleteMapping(value = "delete")
    public ResponseEntity<Boolean> delete(@RequestBody ArrayList<Integer> ids) {

        boolean result = this.processService.delete(ids);
        return ResponseEntity.success(result);
    }
}
