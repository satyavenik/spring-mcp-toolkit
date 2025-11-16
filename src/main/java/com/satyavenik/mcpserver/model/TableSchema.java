package com.satyavenik.mcpserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Table Schema Model - Represents a database table
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableSchema {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("columns")
    private List<ColumnSchema> columns;
    
    @JsonProperty("primaryKey")
    private List<String> primaryKey;
    
    @JsonProperty("foreignKeys")
    private List<ForeignKey> foreignKeys;
    
    @JsonProperty("description")
    private String description;
}
