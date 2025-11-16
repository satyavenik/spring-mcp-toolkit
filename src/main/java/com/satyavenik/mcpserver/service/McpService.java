package com.satyavenik.mcpserver.service;

import com.satyavenik.mcpserver.model.DatabaseSchema;
import com.satyavenik.mcpserver.model.SqlTemplate;
import com.satyavenik.mcpserver.protocol.McpError;
import com.satyavenik.mcpserver.protocol.McpRequest;
import com.satyavenik.mcpserver.protocol.McpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MCP Protocol Service - Handles MCP protocol requests
 */
@Service
@Slf4j
public class McpService {

    private final SchemaService schemaService;
    private final TemplateService templateService;

    public McpService(SchemaService schemaService, TemplateService templateService) {
        this.schemaService = schemaService;
        this.templateService = templateService;
    }

    /**
     * Process MCP request and return response
     * @param request MCP request
     * @return MCP response
     */
    public McpResponse processRequest(McpRequest request) {
        log.info("Processing MCP request: method={}, id={}", request.getMethod(), request.getId());

        try {
            Object result = handleMethod(request.getMethod(), request.getParams());
            return McpResponse.builder()
                    .jsonrpc("2.0")
                    .id(request.getId())
                    .result(result)
                    .build();
        } catch (Exception e) {
            log.error("Error processing MCP request", e);
            return McpResponse.builder()
                    .jsonrpc("2.0")
                    .id(request.getId())
                    .error(McpError.builder()
                            .code(-32603)
                            .message("Internal error: " + e.getMessage())
                            .build())
                    .build();
        }
    }

    /**
     * Handle different MCP methods
     */
    private Object handleMethod(String method, Object params) {
        switch (method) {
            case "initialize":
                return handleInitialize();
            case "tools/list":
                return handleToolsList();
            case "tools/call":
                return handleToolsCall(params);
            case "resources/list":
                return handleResourcesList();
            case "resources/read":
                return handleResourcesRead(params);
            default:
                throw new IllegalArgumentException("Unknown method: " + method);
        }
    }

    /**
     * Handle initialize request
     */
    private Map<String, Object> handleInitialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("protocolVersion", "2024-11-05");
        result.put("serverInfo", Map.of(
                "name", "Spring MCP Toolkit",
                "version", "1.0.0"
        ));
        result.put("capabilities", Map.of(
                "tools", Map.of(),
                "resources", Map.of()
        ));
        return result;
    }

    /**
     * Handle tools/list request - returns available tools
     */
    private Map<String, Object> handleToolsList() {
        Map<String, Object> result = new HashMap<>();
        result.put("tools", List.of(
                Map.of(
                        "name", "get_schema",
                        "description", "Get database schema for SQL generation context",
                        "inputSchema", Map.of(
                                "type", "object",
                                "properties", Map.of(),
                                "required", List.of()
                        )
                ),
                Map.of(
                        "name", "get_templates",
                        "description", "Get SQL generation templates",
                        "inputSchema", Map.of(
                                "type", "object",
                                "properties", Map.of(),
                                "required", List.of()
                        )
                ),
                Map.of(
                        "name", "get_template",
                        "description", "Get specific SQL template by name",
                        "inputSchema", Map.of(
                                "type", "object",
                                "properties", Map.of(
                                        "name", Map.of(
                                                "type", "string",
                                                "description", "Template name"
                                        )
                                ),
                                "required", List.of("name")
                        )
                )
        ));
        return result;
    }

    /**
     * Handle tools/call request - executes a tool
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> handleToolsCall(Object params) {
        Map<String, Object> paramsMap = (Map<String, Object>) params;
        String toolName = (String) paramsMap.get("name");
        Map<String, Object> arguments = (Map<String, Object>) paramsMap.getOrDefault("arguments", new HashMap<>());

        Object content;
        switch (toolName) {
            case "get_schema":
                DatabaseSchema schema = schemaService.getExampleSchema();
                content = schema;
                break;
            case "get_templates":
                List<SqlTemplate> templates = templateService.getAllTemplates();
                content = templates;
                break;
            case "get_template":
                String templateName = (String) arguments.get("name");
                SqlTemplate template = templateService.getTemplateByName(templateName);
                content = template != null ? template : Map.of("error", "Template not found");
                break;
            default:
                throw new IllegalArgumentException("Unknown tool: " + toolName);
        }

        return Map.of(
                "content", List.of(
                        Map.of(
                                "type", "text",
                                "text", content.toString()
                        )
                )
        );
    }

    /**
     * Handle resources/list request - returns available resources
     */
    private Map<String, Object> handleResourcesList() {
        Map<String, Object> result = new HashMap<>();
        result.put("resources", List.of(
                Map.of(
                        "uri", "schema://database/ecommerce",
                        "name", "E-commerce Database Schema",
                        "description", "Complete schema for e-commerce database",
                        "mimeType", "application/json"
                ),
                Map.of(
                        "uri", "templates://sql/all",
                        "name", "SQL Templates",
                        "description", "All available SQL generation templates",
                        "mimeType", "application/json"
                )
        ));
        return result;
    }

    /**
     * Handle resources/read request - reads a resource
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> handleResourcesRead(Object params) {
        Map<String, Object> paramsMap = (Map<String, Object>) params;
        String uri = (String) paramsMap.get("uri");

        Object content;
        if (uri.startsWith("schema://")) {
            content = schemaService.getExampleSchema();
        } else if (uri.startsWith("templates://")) {
            content = templateService.getAllTemplates();
        } else {
            throw new IllegalArgumentException("Unknown resource URI: " + uri);
        }

        return Map.of(
                "contents", List.of(
                        Map.of(
                                "uri", uri,
                                "mimeType", "application/json",
                                "text", content.toString()
                        )
                )
        );
    }
}
