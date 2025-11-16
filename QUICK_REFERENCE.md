# MCP Server - Quick Reference Card

## Server Info
- **Base URL**: `http://localhost:8080`
- **Endpoint**: `POST /mcp`
- **Health Check**: `GET /mcp/health`

---

## MCP Methods

| Method | Purpose | Parameters |
|--------|---------|------------|
| `initialize` | Start MCP session | None |
| `tools/list` | List available tools | None |
| `tools/call` | Execute a tool | `name`, `arguments` |
| `resources/list` | List available resources | None |
| `resources/read` | Read a resource | `uri` |

---

## Available Tools

| Tool Name | Description | Arguments |
|-----------|-------------|-----------|
| `get_schema` | Get database schema | None |
| `get_templates` | Get all SQL templates | None |
| `get_template` | Get specific template | `name` (string) |

---

## SQL Template Names

| Template | Description |
|----------|-------------|
| `basic_select` | SELECT with WHERE |
| `inner_join` | INNER JOIN query |
| `left_join` | LEFT JOIN query |
| `insert` | INSERT statement |
| `update` | UPDATE statement |
| `delete` | DELETE statement |
| `aggregate` | GROUP BY query |
| `subquery` | Subquery pattern |
| `union` | UNION query |

---

## Resource URIs

| URI | Description |
|-----|-------------|
| `schema://database/ecommerce` | Database schema |
| `templates://sql/all` | All SQL templates |

---

## Request Template

```json
{
    "jsonrpc": "2.0",
    "id": 1,
    "method": "METHOD_NAME",
    "params": {
        // parameters here
    }
}
```

---

## Common Requests

### Get Schema
```json
{
    "jsonrpc": "2.0",
    "id": 1,
    "method": "tools/call",
    "params": {
        "name": "get_schema",
        "arguments": {}
    }
}
```

### Get All Templates
```json
{
    "jsonrpc": "2.0",
    "id": 2,
    "method": "tools/call",
    "params": {
        "name": "get_templates",
        "arguments": {}
    }
}
```

### Get Specific Template
```json
{
    "jsonrpc": "2.0",
    "id": 3,
    "method": "tools/call",
    "params": {
        "name": "get_template",
        "arguments": {
            "name": "basic_select"
        }
    }
}
```

---

## Error Codes

| Code | Meaning |
|------|---------|
| `-32700` | Parse error |
| `-32600` | Invalid request |
| `-32601` | Method not found |
| `-32602` | Invalid params |
| `-32603` | Internal error |

---

## cURL Examples

### Health Check
```bash
curl http://localhost:8080/mcp/health
```

### Initialize
```bash
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{}}'
```

### Get Schema
```bash
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":1,"method":"tools/call","params":{"name":"get_schema","arguments":{}}}'
```

### Get Templates
```bash
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":1,"method":"tools/call","params":{"name":"get_templates","arguments":{}}}'
```

---

## Database Schema Structure

The example schema includes:
- **users** table (id, username, email, created_at)
- **products** table (id, name, price, stock, category)
- **orders** table (id, user_id, order_date, status, total_amount)
- **order_items** table (id, order_id, product_id, quantity, price)

Foreign Keys:
- orders.user_id → users.id
- order_items.order_id → orders.id
- order_items.product_id → products.id

---

## Testing Workflow

1. ✅ Health check
2. ✅ Initialize session
3. ✅ List available tools
4. ✅ Get database schema
5. ✅ Get SQL templates
6. ✅ Get specific template
7. ✅ List resources
8. ✅ Read resources

---

## Start Server

```bash
mvn spring-boot:run
```

Or with Maven Wrapper:
```bash
./mvnw spring-boot:run
```

---

## Import to Postman

1. Open Postman
2. Click **Import**
3. Select `Spring-MCP-Toolkit.postman_collection.json`
4. Start testing!

---

**Documentation**: See `POSTMAN_GUIDE.md` for detailed instructions
# Postman Collection Guide

## Import the Collection

1. Open Postman
2. Click **Import** button (top left)
3. Select **File** tab
4. Choose `Spring-MCP-Toolkit.postman_collection.json`
5. Click **Import**

## Start the Server

Before testing, make sure the MCP server is running:

```bash
mvn spring-boot:run
```

The server will start on `http://localhost:8080`

## Available Requests

### 1. Health Check
- **Method**: GET
- **URL**: `http://localhost:8080/mcp/health`
- **Purpose**: Verify server is running
- **Expected Response**: `"MCP Server is running"`

---

### 2. Initialize
- **Method**: POST
- **Purpose**: Initialize MCP session and get server capabilities
- **Request**:
```json
{
    "jsonrpc": "2.0",
    "id": 1,
    "method": "initialize",
    "params": {}
}
```
- **Response**: Returns protocol version, server info, and capabilities

---

### 3. List All Tools
- **Method**: POST
- **Purpose**: Get list of all available tools
- **Request**:
```json
{
    "jsonrpc": "2.0",
    "id": 2,
    "method": "tools/list",
    "params": {}
}
```
- **Response**: Returns array of tools:
  - `get_schema` - Get database schema
  - `get_templates` - Get all SQL templates
  - `get_template` - Get specific template by name

---

### 4. Get Database Schema
- **Method**: POST
- **Purpose**: Retrieve complete database structure
- **Request**:
```json
{
    "jsonrpc": "2.0",
    "id": 3,
    "method": "tools/call",
    "params": {
        "name": "get_schema",
        "arguments": {}
    }
}
```
- **Response**: Returns database schema with tables, columns, types, and relationships

---

### 5. Get All SQL Templates
- **Method**: POST
- **Purpose**: Retrieve all available SQL generation templates
- **Request**:
```json
{
    "jsonrpc": "2.0",
    "id": 4,
    "method": "tools/call",
    "params": {
        "name": "get_templates",
        "arguments": {}
    }
}
```
- **Response**: Returns array of all SQL templates

---

### 6. Get Specific Template
- **Method**: POST
- **Purpose**: Retrieve a specific SQL template by name
- **Request**:
```json
{
    "jsonrpc": "2.0",
    "id": 5,
    "method": "tools/call",
    "params": {
        "name": "get_template",
        "arguments": {
            "name": "basic_select"
        }
    }
}
```

**Available Template Names**:
- `basic_select` - Basic SELECT with WHERE clause
- `inner_join` - INNER JOIN between tables
- `left_join` - LEFT JOIN query
- `insert` - INSERT statement
- `update` - UPDATE statement
- `delete` - DELETE statement
- `aggregate` - GROUP BY with aggregation
- `subquery` - Subquery pattern
- `union` - UNION query

---

### 7. List All Resources
- **Method**: POST
- **Purpose**: Get list of all available resources
- **Request**:
```json
{
    "jsonrpc": "2.0",
    "id": 11,
    "method": "resources/list",
    "params": {}
}
```
- **Response**: Returns available resources with URIs

---

### 8. Read Schema Resource
- **Method**: POST
- **Purpose**: Read database schema as a resource
- **Request**:
```json
{
    "jsonrpc": "2.0",
    "id": 12,
    "method": "resources/read",
    "params": {
        "uri": "schema://database/ecommerce"
    }
}
```

---

### 9. Read Templates Resource
- **Method**: POST
- **Purpose**: Read all SQL templates as a resource
- **Request**:
```json
{
    "jsonrpc": "2.0",
    "id": 13,
    "method": "resources/read",
    "params": {
        "uri": "templates://sql/all"
    }
}
```

---

## Testing Workflow

### Typical Usage Sequence:

1. **Health Check** - Verify server is up
2. **Initialize** - Start MCP session
3. **List Tools** - See what's available
4. **Get Schema** - Understand database structure
5. **Get Templates** - See available SQL patterns
6. **Get Specific Template** - Get a particular template for SQL generation

### Example: AI SQL Generation Flow

```
1. AI calls "get_schema"
   → Learns about users, orders, products tables

2. AI calls "get_template" with name="inner_join"
   → Gets JOIN template pattern

3. AI generates SQL:
   SELECT u.username, o.order_date
   FROM users u
   INNER JOIN orders o ON u.id = o.user_id
   WHERE o.status = 'completed'
```

---

## Error Testing Requests

The collection includes error test cases:

1. **Invalid Method** - Tests unknown method handling
2. **Invalid Tool** - Tests unknown tool handling
3. **Template Not Found** - Tests missing template handling

---

## JSON-RPC 2.0 Format

All requests follow JSON-RPC 2.0 specification:

```json
{
    "jsonrpc": "2.0",      // Protocol version
    "id": 1,               // Request ID (for tracking)
    "method": "...",       // MCP method name
    "params": {}           // Method parameters
}
```

**Success Response**:
```json
{
    "jsonrpc": "2.0",
    "id": 1,
    "result": { ... }
}
```

**Error Response**:
```json
{
    "jsonrpc": "2.0",
    "id": 1,
    "error": {
        "code": -32603,
        "message": "Error description"
    }
}
```

---

## Variables

The collection includes these variables:
- `baseUrl` = `http://localhost:8080`
- `mcpEndpoint` = `{{baseUrl}}/mcp`

You can modify these if your server runs on a different port.

---

## Tips

1. **Sequential Testing**: Run requests in order from top to bottom
2. **Check Response Status**: Should be `200 OK`
3. **Validate JSON**: Responses follow JSON-RPC 2.0 format
4. **Request IDs**: Change the `id` field to track different requests
5. **Pretty Print**: Use Postman's JSON formatter for readable responses

---

## Troubleshooting

### Server Not Running
**Error**: Connection refused
**Solution**: Start the server with `mvn spring-boot:run`

### Invalid Response
**Error**: Unexpected response format
**Solution**: Check that Content-Type is `application/json`

### Method Not Found
**Error**: `"error": {"code": -32603, "message": "Unknown method"}`
**Solution**: Verify the method name in your request

---

## Next Steps

After testing with Postman:
1. Integrate with AI assistants (Claude, GPT, etc.)
2. Customize schema in `SchemaService.java`
3. Add custom templates in `TemplateService.java`
4. Implement database connectivity for real schemas

---

## Additional Resources

- [MCP Protocol Specification](https://modelcontextprotocol.io/)
- [JSON-RPC 2.0 Specification](https://www.jsonrpc.org/specification)
- [Project README](./README.md)
- [API Examples](./API_EXAMPLES.md)

