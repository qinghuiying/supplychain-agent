package com.supplychain.prompt.controller;

import com.supplychain.common.api.ApiResponse;
import com.supplychain.common.api.ApiResponseUtils;
import com.supplychain.prompt.model.SystemPromptCreateRequest;
import com.supplychain.prompt.model.SystemPromptResponse;
import com.supplychain.prompt.model.SystemPromptUpdateRequest;
import com.supplychain.prompt.service.SystemPromptService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system-prompts")
public class SystemPromptController {

    private final SystemPromptService systemPromptService;

    public SystemPromptController(SystemPromptService systemPromptService) {
        this.systemPromptService = systemPromptService;
    }

    /**
     * 查询当前生效的系统提示词。
     */
    @GetMapping("/current")
    public ApiResponse<SystemPromptResponse> current(
            @RequestParam("agentCode") String agentCode,
            @RequestParam(value = "env", required = false) String env
    ) {
        return ApiResponseUtils.ok(systemPromptService.getPrompt(agentCode, env));
    }

    /**
     * 新增系统提示词。
     */
    @PostMapping
    public ApiResponse<SystemPromptResponse> create(@RequestBody SystemPromptCreateRequest request) {
        return ApiResponseUtils.ok(systemPromptService.createPrompt(request));
    }

    /**
     * 编辑系统提示词。
     */
    @PutMapping("/{id}")
    public ApiResponse<SystemPromptResponse> update(
            @PathVariable("id") Long id,
            @RequestBody SystemPromptUpdateRequest request
    ) {
        return ApiResponseUtils.ok(systemPromptService.updatePrompt(id, request));
    }

    /**
     * 删除系统提示词。
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        systemPromptService.deletePrompt(id);
        return ApiResponseUtils.ok();
    }
}
