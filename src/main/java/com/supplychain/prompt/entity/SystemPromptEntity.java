package com.supplychain.prompt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("sys_system_prompt")
public class SystemPromptEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String agentCode;
    private String env;
    private String promptContent;
    private Integer version;
    private Integer enabled;
    private LocalDateTime updatedAt;

    /**
     * 获取主键 ID。
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键 ID。
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 agent 编码。
     */
    public String getAgentCode() {
        return agentCode;
    }

    /**
     * 设置 agent 编码。
     */
    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    /**
     * 获取环境标识。
     */
    public String getEnv() {
        return env;
    }

    /**
     * 设置环境标识。
     */
    public void setEnv(String env) {
        this.env = env;
    }

    /**
     * 获取系统提示词内容。
     */
    public String getPromptContent() {
        return promptContent;
    }

    /**
     * 设置系统提示词内容。
     */
    public void setPromptContent(String promptContent) {
        this.promptContent = promptContent;
    }

    /**
     * 获取版本号。
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * 设置版本号。
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * 获取启用状态。
     */
    public Integer getEnabled() {
        return enabled;
    }

    /**
     * 设置启用状态。
     */
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    /**
     * 获取更新时间。
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 设置更新时间。
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
