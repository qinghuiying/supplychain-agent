package com.supplychain.prompt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.supplychain.common.api.ApiCode;
import com.supplychain.common.exception.BizException;
import com.supplychain.config.SystemPromptProperties;
import com.supplychain.prompt.entity.SystemPromptEntity;
import com.supplychain.prompt.mapper.SystemPromptMapper;
import com.supplychain.prompt.model.SystemPromptCreateRequest;
import com.supplychain.prompt.model.SystemPromptResponse;
import com.supplychain.prompt.model.SystemPromptUpdateRequest;
import com.supplychain.prompt.util.SystemPromptBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
public class SystemPromptService {

    private final SystemPromptMapper systemPromptMapper;
    private final SystemPromptProperties properties;

    public SystemPromptService(SystemPromptMapper systemPromptMapper, SystemPromptProperties properties) {
        this.systemPromptMapper = systemPromptMapper;
        this.properties = properties;
    }

    /**
     * 按 agentCode + env 查询当前生效提示词，未命中时按配置返回回退提示词。
     */
    public SystemPromptResponse getPrompt(String agentCode, String env) {
        String actualEnv = StringUtils.hasText(env) ? env : properties.getDefaultEnv();
        LambdaQueryWrapper<SystemPromptEntity> wrapper = new LambdaQueryWrapper<SystemPromptEntity>()
                .eq(SystemPromptEntity::getAgentCode, agentCode)
                .eq(SystemPromptEntity::getEnv, actualEnv)
                .eq(SystemPromptEntity::getEnabled, 1)
                .orderByDesc(SystemPromptEntity::getVersion)
                .orderByDesc(SystemPromptEntity::getUpdatedAt)
                .last("limit 1");
        SystemPromptEntity entity = systemPromptMapper.selectOne(wrapper);
        if (entity != null && StringUtils.hasText(entity.getPromptContent())) {
            return new SystemPromptResponse(
                    entity.getAgentCode(),
                    entity.getEnv(),
                    entity.getPromptContent(),
                    entity.getVersion(),
                    false
            );
        }
        if (!properties.isFallbackEnabled()) {
            throw new BizException(ApiCode.BIZ_NOT_FOUND.code(), "未找到系统提示词，请检查 agentCode 和 env。agentCode=" + agentCode + ", env=" + actualEnv);
        }
        return new SystemPromptResponse(agentCode, actualEnv, properties.getFallbackPrompt(), 0, true);
    }

    /**
     * 新增一条系统提示词记录。
     */
    public SystemPromptResponse createPrompt(SystemPromptCreateRequest request) {
        if (!StringUtils.hasText(request.getAgentCode())) {
            throw new BizException(ApiCode.PARAM_INVALID.code(), "参数错误：agentCode 不能为空。");
        }
        if (!StringUtils.hasText(request.getEnv())) {
            throw new BizException(ApiCode.PARAM_INVALID.code(), "参数错误：env 不能为空。");
        }
        String promptContent = resolvePromptContent(
                request.getPrompt(),
                request.getRole(),
                request.getResponsibility(),
                request.getCapability(),
                request.getScenario()
        );
        if (!StringUtils.hasText(promptContent)) {
            throw new BizException(ApiCode.PARAM_INVALID.code(), "参数错误：请提供 prompt，或完整提供 role/responsibility/capability/scenario。");
        }

        int version = request.getVersion() != null ? request.getVersion() : nextVersion(request.getAgentCode(), request.getEnv());
        int enabled = request.getEnabled() != null ? request.getEnabled() : 1;

        SystemPromptEntity entity = new SystemPromptEntity();
        entity.setAgentCode(request.getAgentCode());
        entity.setEnv(request.getEnv());
        entity.setPromptContent(promptContent);
        entity.setVersion(version);
        entity.setEnabled(enabled);
        systemPromptMapper.insert(entity);

        return new SystemPromptResponse(entity.getAgentCode(), entity.getEnv(), entity.getPromptContent(), entity.getVersion(), false);
    }

    /**
     * 按主键更新系统提示词记录。
     */
    public SystemPromptResponse updatePrompt(Long id, SystemPromptUpdateRequest request) {
        SystemPromptEntity entity = systemPromptMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ApiCode.BIZ_NOT_FOUND.code(), "未找到要更新的系统提示词，id=" + id);
        }
        String promptContent = resolvePromptContent(
                request.getPrompt(),
                request.getRole(),
                request.getResponsibility(),
                request.getCapability(),
                request.getScenario()
        );
        if (StringUtils.hasText(promptContent)) {
            entity.setPromptContent(promptContent);
        }
        if (request.getVersion() != null) {
            entity.setVersion(request.getVersion());
        }
        if (request.getEnabled() != null) {
            entity.setEnabled(request.getEnabled());
        }
        systemPromptMapper.updateById(entity);

        SystemPromptEntity latest = systemPromptMapper.selectById(id);
        return new SystemPromptResponse(latest.getAgentCode(), latest.getEnv(), latest.getPromptContent(), latest.getVersion(), false);
    }

    /**
     * 按主键删除系统提示词记录。
     */
    public void deletePrompt(Long id) {
        SystemPromptEntity entity = systemPromptMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ApiCode.BIZ_NOT_FOUND.code(), "未找到要删除的系统提示词，id=" + id);
        }
        systemPromptMapper.deleteById(id);
    }

    /**
     * 计算下一版本号。
     */
    private int nextVersion(String agentCode, String env) {
        LambdaQueryWrapper<SystemPromptEntity> wrapper = new LambdaQueryWrapper<SystemPromptEntity>()
                .eq(SystemPromptEntity::getAgentCode, agentCode)
                .eq(SystemPromptEntity::getEnv, env)
                .orderByDesc(SystemPromptEntity::getVersion)
                .last("limit 1");
        SystemPromptEntity latest = systemPromptMapper.selectOne(wrapper);
        if (latest == null || latest.getVersion() == null) {
            return 1;
        }
        return latest.getVersion() + 1;
    }

    /**
     * 按优先级解析提示词内容：结构化字段构建 > prompt 原文（需通过格式校验）。
     */
    private String resolvePromptContent(
            String prompt,
            String role,
            List<String> responsibility,
            String capability,
            String scenario
    ) {
        if (SystemPromptBuilder.hasAllLayers(role, responsibility, capability, scenario)) {
            return SystemPromptBuilder.build(role, responsibility, capability, scenario);
        }
        if (StringUtils.hasText(prompt)) {
            if (!SystemPromptBuilder.isValidDecisionPrompt(prompt)) {
                throw new BizException(
                        ApiCode.PARAM_INVALID.code(),
                        SystemPromptBuilder.invalidFormatMessage()
                );
            }
            return prompt;
        }
        if (StringUtils.hasText(role)
                || !Objects.isNull(responsibility)
                || StringUtils.hasText(capability)
                || StringUtils.hasText(scenario)) {
            throw new BizException(ApiCode.PARAM_INVALID.code(), SystemPromptBuilder.structuredFieldsRequiredMessage());
        }
        return null;
    }
}
