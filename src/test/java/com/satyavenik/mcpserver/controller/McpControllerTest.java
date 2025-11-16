package com.satyavenik.mcpserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.satyavenik.mcpserver.protocol.McpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class McpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testHealth() throws Exception {
        mockMvc.perform(get("/mcp/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("MCP Server is running"));
    }

    @Test
    void testInitialize() throws Exception {
        McpRequest request = McpRequest.builder()
                .jsonrpc("2.0")
                .id(1)
                .method("initialize")
                .params(null)
                .build();

        mockMvc.perform(post("/mcp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jsonrpc").value("2.0"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.result.protocolVersion").exists())
                .andExpect(jsonPath("$.result.serverInfo.name").value("Spring MCP Toolkit"));
    }

    @Test
    void testToolsList() throws Exception {
        McpRequest request = McpRequest.builder()
                .jsonrpc("2.0")
                .id(2)
                .method("tools/list")
                .params(null)
                .build();

        mockMvc.perform(post("/mcp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.tools").isArray())
                .andExpect(jsonPath("$.result.tools[0].name").exists());
    }
}
