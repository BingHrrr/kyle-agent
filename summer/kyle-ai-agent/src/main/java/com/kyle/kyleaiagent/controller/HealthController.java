package com.kyle.kyleaiagent.controller;

import com.kyle.kyleaiagent.common.BaseResponse;
import com.kyle.kyleaiagent.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/check")
    public BaseResponse<String> healthCheck() {
        return ResultUtils.success("ok");
    }
}
