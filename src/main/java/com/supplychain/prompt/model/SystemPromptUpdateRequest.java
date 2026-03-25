package com.supplychain.prompt.model;

public class SystemPromptUpdateRequest {

    private String prompt;
    private String role;
    private String responsibility;
    private String capability;
    private String scenario;
    private Integer version;
    private Integer enabled;

    /**
     * 获取提示词内容。
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * 设置提示词内容。
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    /**
     * 获取角色定义。
     */
    public String getRole() {
        return role;
    }

    /**
     * 设置角色定义。
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * 获取职责定义。
     */
    public String getResponsibility() {
        return responsibility;
    }

    /**
     * 设置职责定义。
     */
    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    /**
     * 获取能力定义。
     */
    public String getCapability() {
        return capability;
    }

    /**
     * 设置能力定义。
     */
    public void setCapability(String capability) {
        this.capability = capability;
    }

    /**
     * 获取场景定义。
     */
    public String getScenario() {
        return scenario;
    }

    /**
     * 设置场景定义。
     */
    public void setScenario(String scenario) {
        this.scenario = scenario;
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
}
