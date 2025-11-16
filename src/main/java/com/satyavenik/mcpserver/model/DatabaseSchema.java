package com.satyavenik.mcpserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Database Schema Model - Represents a database schema with tables
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseSchema {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("tables")
    private List<TableSchema> tables;
    
    @JsonProperty("description")
    private String description;
}
