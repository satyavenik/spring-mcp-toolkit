package com.satyavenik.mcpserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Column Schema Model - Represents a database column
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnSchema {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("nullable")
    private boolean nullable;
    
    @JsonProperty("defaultValue")
    private String defaultValue;
    
    @JsonProperty("description")
    private String description;
}
