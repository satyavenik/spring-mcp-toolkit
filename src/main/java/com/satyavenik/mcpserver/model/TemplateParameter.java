package com.satyavenik.mcpserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Template Parameter Model - Represents a parameter in SQL template
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateParameter {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("required")
    private boolean required;
    
    @JsonProperty("description")
    private String description;
}
