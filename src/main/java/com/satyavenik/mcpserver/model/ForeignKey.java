package com.satyavenik.mcpserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Foreign Key Model - Represents a foreign key relationship
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForeignKey {
    
    @JsonProperty("columns")
    private List<String> columns;
    
    @JsonProperty("referencedTable")
    private String referencedTable;
    
    @JsonProperty("referencedColumns")
    private List<String> referencedColumns;
}
