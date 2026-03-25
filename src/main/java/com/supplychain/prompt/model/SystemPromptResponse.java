package com.supplychain.prompt.model;

public record SystemPromptResponse(
        String agentCode,
        String env,
        String prompt,
        Integer version,
        boolean fallback
) {
}
