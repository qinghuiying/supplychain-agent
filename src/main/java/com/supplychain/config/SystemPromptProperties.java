package com.supplychain.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "system-prompt")
public class SystemPromptProperties {

    private String defaultEnv = "dev";
    private String fallbackPrompt = "You are a helpful assistant.";
    private boolean fallbackEnabled = true;

    /**
     * 获取默认环境。
     */
    public String getDefaultEnv() {
        return defaultEnv;
    }

    /**
     * 设置默认环境。
     */
    public void setDefaultEnv(String defaultEnv) {
        this.defaultEnv = defaultEnv;
    }

    /**
     * 获取回退提示词内容。
     */
    public String getFallbackPrompt() {
        return fallbackPrompt;
    }

    /**
     * 设置回退提示词内容。
     */
    public void setFallbackPrompt(String fallbackPrompt) {
        this.fallbackPrompt = fallbackPrompt;
    }

    /**
     * 获取是否启用回退。
     */
    public boolean isFallbackEnabled() {
        return fallbackEnabled;
    }

    /**
     * 设置是否启用回退。
     */
    public void setFallbackEnabled(boolean fallbackEnabled) {
        this.fallbackEnabled = fallbackEnabled;
    }
}
