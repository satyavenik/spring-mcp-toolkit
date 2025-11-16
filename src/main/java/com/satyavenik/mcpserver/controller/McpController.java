package com.satyavenik.mcpserver.controller;

import com.satyavenik.mcpserver.protocol.McpRequest;
import com.satyavenik.mcpserver.protocol.McpResponse;
import com.satyavenik.mcpserver.service.McpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * MCP Controller - REST endpoint for MCP protocol
 */
@RestController
@RequestMapping("/mcp")
@Slf4j
public class McpController {

    private final McpService mcpService;

    public McpController(McpService mcpService) {
        this.mcpService = mcpService;
    }

    /**
     * Handle MCP JSON-RPC requests
     * @param request MCP request
     * @return MCP response
     */
    @PostMapping
    public ResponseEntity<McpResponse> handleMcpRequest(@RequestBody McpRequest request) {
        log.info("Received MCP request: {}", request.getMethod());
        McpResponse response = mcpService.processRequest(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("MCP Server is running");
    }
}
