package com.supplychain.prompt.util;

import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class PromptContext {

    private String base;
    private String role;
    private List<String> responsibilities;
    private String scene;
    private String rules;
    private String output;
    private String userType;
    private Map<String, String> tools;
}
