package org.zipper.flowable.app.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zipper.helper.web.response.ResponseEntity;

import java.util.HashMap;

@RestController
@RequestMapping(value = "myProcess")
public class MyProcessController {

    /**
     * 发起流程
     *
     * @param variables 流程变量
     * @return
     */
    @PostMapping(value = "initiate")
    public ResponseEntity initiate(@RequestBody HashMap<String, Object> variables) {

        return ResponseEntity.success(null);
    }
}
