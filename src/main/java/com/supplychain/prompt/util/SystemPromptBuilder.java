package com.supplychain.prompt.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SystemPromptBuilder {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Pattern DECISION_PROMPT_PATTERN = Pattern.compile(
            "(?s)^\\s*你是.+?Agent\\s*【职责】\\s*(.+?)\\s*【规则】\\s*(.+?)\\s*【输出要求】\\s*(.+?)\\s*$"
    );
    public static final String DEFAULT_OUTPUT_JSON = "{\n"
            + "  \"final_decision\": \"\",\n"
            + "  \"actions\": [\n"
            + "    {\"type\": \"create_order\", \"params\": {}}\n"
            + "  ]\n"
            + "}";
    private static final String FORMAT_HINT = "你是...Agent + 【职责】 + 【规则】 + 【输出要求】（且【输出要求】必须是合法 JSON 对象）";

    private SystemPromptBuilder() {}

    /**
     * 推荐使用：结构化构建 Prompt（支持扩展）
     */
    public static String build(PromptContext ctx) {

        StringBuilder builder = new StringBuilder();
        if (StringUtils.hasText(ctx.getBase())) {
            builder.append(ctx.getBase()).append("\n\n");
        }
        builder.append("你是").append(ctx.getRole()).append("\n\n");
        builder.append("【职责】\n");
        for (String r : ctx.getResponsibilities()) {
            builder.append("- ").append(r.trim()).append("\n");
        }
        builder.append("\n");
        if (StringUtils.hasText(ctx.getScene())) {
            builder.append("【当前场景】\n");
            builder.append(ctx.getScene()).append("\n\n");
        }
        builder.append("【规则】\n");
        builder.append(ctx.getRules()).append("\n\n");
        if (ctx.getTools() != null && !ctx.getTools().isEmpty()) {
            builder.append("【可用工具】\n");
            ctx.getTools().forEach((name, desc) ->
                    builder.append("- ").append(name).append(": ").append(desc).append("\n")
            );
            builder.append("\n");
        }
        builder.append("【输出要求】\n");
        builder.append(ctx.getOutput()).append("\n");
        if (StringUtils.hasText(ctx.getUserType())) {
            builder.append("\n【输出风格】\n");
            builder.append(ctx.getUserType());
        }

        return builder.toString();
    }

    /**
     * 兼容旧调用：通过 role/responsibility/rules(scene) 生成标准 Prompt。
     */
    public static String build(String role, List<String> responsibility, String rules, String scene) {
        PromptContext ctx = new PromptContext();
        ctx.setRole(role);
        ctx.setResponsibilities(responsibility);
        ctx.setRules(rules);
        ctx.setScene(scene);
        ctx.setOutput(DEFAULT_OUTPUT_JSON);
        validate(ctx);
        return build(ctx);
    }

    /**
     * 是否完整提供结构化字段。
     */
    public static boolean hasAllLayers(String role, List<String> responsibility, String capability, String scenario) {
        return StringUtils.hasText(role)
                && !Objects.isNull(responsibility)
                && StringUtils.hasText(capability)
                && StringUtils.hasText(scenario);
    }

    /**
     * 校验 prompt 是否满足固定结构，并且【输出要求】是合法 JSON 对象。
     */
    public static boolean isValidDecisionPrompt(String prompt) {
        if (!StringUtils.hasText(prompt)) {
            return false;
        }
        Matcher matcher = DECISION_PROMPT_PATTERN.matcher(prompt);
        if (!matcher.matches()) {
            return false;
        }
        String outputJson = matcher.group(3);
        return StringUtils.hasText(matcher.group(1))
                && StringUtils.hasText(matcher.group(2))
                && isValidJsonObject(outputJson);
    }

    /**
     * 校验输出 JSON 是否合法
     */
    public static boolean isValidJsonObject(String json) {
        if (!StringUtils.hasText(json)) {
            return false;
        }
        try {
            JsonNode node = OBJECT_MAPPER.readTree(json);
            return node != null && node.isObject();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 提供统一的格式错误提示。
     */
    public static String invalidFormatMessage() {
        return "prompt 格式错误：必须满足 " + FORMAT_HINT + "。";
    }

    /**
     * 提供统一的结构化字段缺失提示。
     */
    public static String structuredFieldsRequiredMessage() {
        return "参数错误：role/responsibility/capability/scenario 需要同时提供。";
    }

    /**
     * 基础字段校验（避免 NPE）
     */
    public static void validate(PromptContext ctx) {
        if (!StringUtils.hasText(ctx.getRole())) {
            throw new IllegalArgumentException("参数错误：role 不能为空。");
        }
        if (!Objects.isNull(ctx.getResponsibilities())) {
            throw new IllegalArgumentException("参数错误：responsibility 不能为空。");
        }
        if (!StringUtils.hasText(ctx.getRules())) {
            throw new IllegalArgumentException("参数错误：rules 不能为空。");
        }
        if (!StringUtils.hasText(ctx.getOutput())) {
            throw new IllegalArgumentException("参数错误：output 不能为空。");
        }
        if (!isValidJsonObject(ctx.getOutput())) {
            throw new IllegalArgumentException("参数错误：output 必须是合法 JSON 对象。");
        }
    }
}
