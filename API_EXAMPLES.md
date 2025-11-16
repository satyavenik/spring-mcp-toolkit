# API Examples

This document provides comprehensive examples of using the Spring MCP Toolkit API.

## Table of Contents

1. [Basic Requests](#basic-requests)
2. [Tools API](#tools-api)
3. [Resources API](#resources-api)
4. [Error Handling](#error-handling)
5. [Integration Examples](#integration-examples)

## Basic Requests

### Health Check

Simple health check to verify the server is running.

**Request:**
```bash
curl http://localhost:8080/mcp/health
```

**Response:**
```
MCP Server is running
```

### Initialize Connection

Initialize the MCP connection and get server capabilities.

**Request:**
```bash
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 1,
    "method": "initialize",
    "params": {}
  }'
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "result": {
    "protocolVersion": "2024-11-05",
    "serverInfo": {
      "name": "Spring MCP Toolkit",
      "version": "1.0.0"
    },
    "capabilities": {
      "tools": {},
      "resources": {}
    }
  }
}
```

## Tools API

### List Available Tools

Get a list of all available tools.

**Request:**
```bash
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 2,
    "method": "tools/list",
    "params": {}
  }'
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 2,
  "result": {
    "tools": [
      {
        "name": "get_schema",
        "description": "Get database schema for SQL generation context",
        "inputSchema": {
          "type": "object",
          "properties": {},
          "required": []
        }
      },
      {
        "name": "get_templates",
        "description": "Get SQL generation templates",
        "inputSchema": {
          "type": "object",
          "properties": {},
          "required": []
        }
      },
      {
        "name": "get_template",
        "description": "Get specific SQL template by name",
        "inputSchema": {
          "type": "object",
          "properties": {
            "name": {
              "type": "string",
              "description": "Template name"
            }
          },
          "required": ["name"]
        }
      }
    ]
  }
}
```

### Get Database Schema

Retrieve the complete database schema with tables, columns, and relationships.

**Request:**
```bash
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 3,
    "method": "tools/call",
    "params": {
      "name": "get_schema",
      "arguments": {}
    }
  }'
```

**Response (truncated):**
```json
{
  "jsonrpc": "2.0",
  "id": 3,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "DatabaseSchema(name=sample_ecommerce, tables=[TableSchema(name=users, columns=[...]), ...])"
      }
    ]
  }
}
```

### Get All SQL Templates

Retrieve all available SQL generation templates.

**Request:**
```bash
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 4,
    "method": "tools/call",
    "params": {
      "name": "get_templates",
      "arguments": {}
    }
  }'
```

### Get Specific Template

Retrieve a specific SQL template by name.

**Request:**
```bash
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 5,
    "method": "tools/call",
    "params": {
      "name": "get_template",
      "arguments": {
        "name": "basic_select"
      }
    }
  }'
```

**Available Template Names:**
- `basic_select` - Basic SELECT query with WHERE clause
- `inner_join` - INNER JOIN query
- `basic_insert` - INSERT statement
- `basic_update` - UPDATE statement with WHERE clause
- `basic_delete` - DELETE statement with WHERE clause
- `aggregate` - Aggregate query with GROUP BY

## Resources API

### List Available Resources

Get a list of all available resources.

**Request:**
```bash
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 6,
    "method": "resources/list",
    "params": {}
  }'
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 6,
  "result": {
    "resources": [
      {
        "uri": "schema://database/ecommerce",
        "name": "E-commerce Database Schema",
        "description": "Complete schema for e-commerce database",
        "mimeType": "application/json"
      },
      {
        "uri": "templates://sql/all",
        "name": "SQL Templates",
        "description": "All available SQL generation templates",
        "mimeType": "application/json"
      }
    ]
  }
}
```

### Read Resource - Database Schema

Read the database schema resource.

**Request:**
```bash
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 7,
    "method": "resources/read",
    "params": {
      "uri": "schema://database/ecommerce"
    }
  }'
```

### Read Resource - SQL Templates

Read all SQL templates resource.

**Request:**
```bash
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 8,
    "method": "resources/read",
    "params": {
      "uri": "templates://sql/all"
    }
  }'
```

## Error Handling

### Invalid Method

**Request:**
```bash
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 100,
    "method": "invalid_method",
    "params": {}
  }'
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 100,
  "error": {
    "code": -32603,
    "message": "Internal error: Unknown method: invalid_method"
  }
}
```

### Invalid Tool Name

**Request:**
```bash
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 101,
    "method": "tools/call",
    "params": {
      "name": "invalid_tool",
      "arguments": {}
    }
  }'
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 101,
  "error": {
    "code": -32603,
    "message": "Internal error: Unknown tool: invalid_tool"
  }
}
```

### Template Not Found

**Request:**
```bash
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 102,
    "method": "tools/call",
    "params": {
      "name": "get_template",
      "arguments": {
        "name": "nonexistent_template"
      }
    }
  }'
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 102,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "{error=Template not found}"
      }
    ]
  }
}
```

## Integration Examples

### Python Integration

```python
import requests
import json

# Base URL
base_url = "http://localhost:8080/mcp"

# Initialize connection
def initialize():
    payload = {
        "jsonrpc": "2.0",
        "id": 1,
        "method": "initialize",
        "params": {}
    }
    response = requests.post(base_url, json=payload)
    return response.json()

# Get database schema
def get_schema():
    payload = {
        "jsonrpc": "2.0",
        "id": 2,
        "method": "tools/call",
        "params": {
            "name": "get_schema",
            "arguments": {}
        }
    }
    response = requests.post(base_url, json=payload)
    return response.json()

# Get all templates
def get_templates():
    payload = {
        "jsonrpc": "2.0",
        "id": 3,
        "method": "tools/call",
        "params": {
            "name": "get_templates",
            "arguments": {}
        }
    }
    response = requests.post(base_url, json=payload)
    return response.json()

# Usage
if __name__ == "__main__":
    # Initialize
    init_response = initialize()
    print("Initialized:", json.dumps(init_response, indent=2))
    
    # Get schema
    schema = get_schema()
    print("Schema:", json.dumps(schema, indent=2))
    
    # Get templates
    templates = get_templates()
    print("Templates:", json.dumps(templates, indent=2))
```

### Node.js Integration

```javascript
const axios = require('axios');

const baseUrl = 'http://localhost:8080/mcp';

// Initialize connection
async function initialize() {
  const payload = {
    jsonrpc: '2.0',
    id: 1,
    method: 'initialize',
    params: {}
  };
  const response = await axios.post(baseUrl, payload);
  return response.data;
}

// Get database schema
async function getSchema() {
  const payload = {
    jsonrpc: '2.0',
    id: 2,
    method: 'tools/call',
    params: {
      name: 'get_schema',
      arguments: {}
    }
  };
  const response = await axios.post(baseUrl, payload);
  return response.data;
}

// Get specific template
async function getTemplate(name) {
  const payload = {
    jsonrpc: '2.0',
    id: 3,
    method: 'tools/call',
    params: {
      name: 'get_template',
      arguments: { name }
    }
  };
  const response = await axios.post(baseUrl, payload);
  return response.data;
}

// Usage
(async () => {
  try {
    // Initialize
    const initResponse = await initialize();
    console.log('Initialized:', JSON.stringify(initResponse, null, 2));
    
    // Get schema
    const schema = await getSchema();
    console.log('Schema:', JSON.stringify(schema, null, 2));
    
    // Get specific template
    const template = await getTemplate('basic_select');
    console.log('Template:', JSON.stringify(template, null, 2));
  } catch (error) {
    console.error('Error:', error.message);
  }
})();
```

### Java Integration

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class McpClient {
    private static final String BASE_URL = "http://localhost:8080/mcp";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();
    
    public static String initialize() throws Exception {
        Map<String, Object> payload = Map.of(
            "jsonrpc", "2.0",
            "id", 1,
            "method", "initialize",
            "params", Map.of()
        );
        return sendRequest(payload);
    }
    
    public static String getSchema() throws Exception {
        Map<String, Object> payload = Map.of(
            "jsonrpc", "2.0",
            "id", 2,
            "method", "tools/call",
            "params", Map.of(
                "name", "get_schema",
                "arguments", Map.of()
            )
        );
        return sendRequest(payload);
    }
    
    private static String sendRequest(Map<String, Object> payload) throws Exception {
        String json = mapper.writeValueAsString(payload);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
        
        HttpResponse<String> response = client.send(request, 
            HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    
    public static void main(String[] args) {
        try {
            System.out.println("Initialize: " + initialize());
            System.out.println("Schema: " + getSchema());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### cURL Script Example

```bash
#!/bin/bash

BASE_URL="http://localhost:8080/mcp"

# Initialize
echo "=== Initialize ==="
curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{}}' | jq .

# List tools
echo -e "\n=== List Tools ==="
curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":2,"method":"tools/list","params":{}}' | jq .

# Get schema
echo -e "\n=== Get Schema ==="
curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":3,"method":"tools/call","params":{"name":"get_schema","arguments":{}}}' | jq .

# Get all templates
echo -e "\n=== Get All Templates ==="
curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":4,"method":"tools/call","params":{"name":"get_templates","arguments":{}}}' | jq .

# Get specific template
echo -e "\n=== Get Basic Select Template ==="
curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":5,"method":"tools/call","params":{"name":"get_template","arguments":{"name":"basic_select"}}}' | jq .

# List resources
echo -e "\n=== List Resources ==="
curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":6,"method":"resources/list","params":{}}' | jq .
```

## Testing with Postman

### Collection Setup

1. Create a new Postman collection named "Spring MCP Toolkit"
2. Set base URL variable: `{{base_url}}` = `http://localhost:8080`
3. Add requests for each endpoint

### Example Request in Postman

**Method:** POST  
**URL:** `{{base_url}}/mcp`  
**Headers:** 
- Content-Type: application/json

**Body (raw JSON):**
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "initialize",
  "params": {}
}
```

## Performance Tips

1. **Reuse connections** - Keep HTTP connections alive when making multiple requests
2. **Cache schema** - Cache the database schema locally if it doesn't change frequently
3. **Batch requests** - If your client supports it, batch multiple JSON-RPC requests
4. **Use async** - Make non-blocking requests when possible

## Troubleshooting

### Connection Refused
- Verify the server is running: `curl http://localhost:8080/mcp/health`
- Check if the port is correct
- Check firewall settings

### Invalid JSON Response
- Ensure request has proper Content-Type header
- Validate JSON syntax in request body
- Check server logs for errors

### Slow Response
- Check server logs for performance issues
- Verify network connectivity
- Consider caching responses

## Additional Resources

- [MCP Specification](https://spec.modelcontextprotocol.io/)
- [JSON-RPC 2.0 Specification](https://www.jsonrpc.org/specification)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
