package com.satyavenik.mcpserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SQL Template Model - Represents a SQL generation template
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SqlTemplate {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("type")
    private String type; // SELECT, INSERT, UPDATE, DELETE, JOIN, etc.
    
    @JsonProperty("template")
    private String template;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("parameters")
    private List<TemplateParameter> parameters;
    
    @JsonProperty("examples")
    private List<String> examples;
}
