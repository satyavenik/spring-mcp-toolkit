# Spring MCP Toolkit

A Spring Boot-based Model Context Protocol (MCP) server that provides database schema and SQL generation templates to MCP clients for intelligent SQL query generation.

## Overview

This MCP server exposes database schema information and SQL query templates through the Model Context Protocol, enabling AI assistants and other MCP clients to generate accurate SQL queries with proper context about your database structure.

## Features

- **MCP Protocol Support**: Full implementation of MCP (Model Context Protocol) for context sharing
- **Database Schema Context**: Provides detailed database schema information including:
  - Table structures
  - Column definitions with types and constraints
  - Primary and foreign key relationships
  - Table and column descriptions
- **SQL Templates**: Pre-built SQL query templates for common operations:
  - SELECT queries
  - JOIN operations
  - INSERT statements
  - UPDATE statements
  - DELETE statements
  - Aggregate queries with GROUP BY
- **RESTful API**: HTTP-based JSON-RPC 2.0 endpoint for MCP communication
- **Easy Integration**: Simple to integrate with any MCP client

## Architecture

The server implements the MCP specification with:
- JSON-RPC 2.0 protocol for request/response
- Tools API for executable operations
- Resources API for static context
- Proper error handling and logging

## Prerequisites

- Java 17 or higher
- Maven 3.6+ or Gradle 7+
- Spring Boot 3.2.0

## Installation & Setup

### 1. Clone the repository

```bash
git clone https://github.com/satyavenik/spring-mcp-toolkit.git
cd spring-mcp-toolkit
```

### 2. Build the project

```bash
mvn clean install
```

### 3. Run the server

```bash
mvn spring-boot:run
```

The server will start on `http://localhost:8080`

## Configuration

### Application Properties

The server can be configured via `src/main/resources/application.yml`:

```yaml
server:
  port: 8080  # Change port if needed

spring:
  application:
    name: spring-mcp-toolkit
```

### Custom Schema

To provide your own database schema, modify the `SchemaService.java` class or extend it to load schema from:
- Database metadata
- Configuration files
- External services

## API Reference

### MCP Endpoint

**Base URL**: `http://localhost:8080/mcp`

### Health Check

```bash
GET http://localhost:8080/mcp/health
```

### MCP Methods

All MCP requests use POST to `/mcp` with JSON-RPC 2.0 format:

#### 1. Initialize

Request:
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "initialize",
  "params": {}
}
```

Response:
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

#### 2. List Tools

Request:
```json
{
  "jsonrpc": "2.0",
  "id": 2,
  "method": "tools/list",
  "params": {}
}
```

Response:
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

#### 3. Call Tool - Get Schema

Request:
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

Response: Returns complete database schema with tables, columns, and relationships.

#### 4. Call Tool - Get Templates

Request:
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

Response: Returns all available SQL templates.

#### 5. Call Tool - Get Specific Template

Request:
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

Response: Returns the specified SQL template with parameters and examples.

#### 6. List Resources

Request:
```json
{
  "jsonrpc": "2.0",
  "id": 6,
  "method": "resources/list",
  "params": {}
}
```

Response:
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

#### 7. Read Resource

Request:
```json
{
  "jsonrpc": "2.0",
  "id": 7,
  "method": "resources/read",
  "params": {
    "uri": "schema://database/ecommerce"
  }
}
```

Response: Returns the content of the specified resource.

## Example Database Schema

The server includes a sample e-commerce database schema with:

- **users**: User accounts with id, username, email, created_at
- **products**: Product catalog with id, name, description, price, stock
- **orders**: Customer orders with id, user_id, total, status, created_at
- **order_items**: Order line items with id, order_id, product_id, quantity, price

Relationships:
- orders.user_id → users.id
- order_items.order_id → orders.id
- order_items.product_id → products.id

## SQL Templates

### Available Templates

1. **basic_select**: Basic SELECT query with WHERE clause
2. **inner_join**: INNER JOIN query
3. **basic_insert**: INSERT statement
4. **basic_update**: UPDATE statement with WHERE clause
5. **basic_delete**: DELETE statement with WHERE clause
6. **aggregate**: Aggregate query with GROUP BY

Each template includes:
- Template structure with placeholders
- Parameter definitions
- Example queries

## Usage with MCP Clients

### Example with curl

```bash
# Initialize
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 1,
    "method": "initialize",
    "params": {}
  }'

# Get schema
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 2,
    "method": "tools/call",
    "params": {
      "name": "get_schema",
      "arguments": {}
    }
  }'

# Get templates
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 3,
    "method": "tools/call",
    "params": {
      "name": "get_templates",
      "arguments": {}
    }
  }'
```

### Integration with AI Assistants

MCP clients (like Claude Desktop or other AI assistants) can connect to this server to:
1. Discover available database tables and columns
2. Understand relationships between tables
3. Access SQL query templates
4. Generate accurate SQL queries based on schema context

## Development

### Project Structure

```
spring-mcp-toolkit/
├── src/
│   ├── main/
│   │   ├── java/com/satyavenik/mcpserver/
│   │   │   ├── McpServerApplication.java
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   │   └── McpController.java
│   │   │   ├── model/
│   │   │   │   ├── ColumnSchema.java
│   │   │   │   ├── DatabaseSchema.java
│   │   │   │   ├── ForeignKey.java
│   │   │   │   ├── SqlTemplate.java
│   │   │   │   ├── TableSchema.java
│   │   │   │   └── TemplateParameter.java
│   │   │   ├── protocol/
│   │   │   │   ├── McpError.java
│   │   │   │   ├── McpRequest.java
│   │   │   │   └── McpResponse.java
│   │   │   └── service/
│   │   │       ├── McpService.java
│   │   │       ├── SchemaService.java
│   │   │       └── TemplateService.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/
├── pom.xml
└── README.md
```

### Adding Custom Schema

Extend `SchemaService` to add your own database schema:

```java
@Service
public class CustomSchemaService extends SchemaService {
    
    @Override
    public DatabaseSchema getExampleSchema() {
        // Return your custom schema
    }
}
```

### Adding Custom Templates

Extend `TemplateService` to add custom SQL templates:

```java
@Service
public class CustomTemplateService extends TemplateService {
    
    @Override
    public List<SqlTemplate> getAllTemplates() {
        List<SqlTemplate> templates = super.getAllTemplates();
        // Add your custom templates
        return templates;
    }
}
```

## Testing

Run tests with:

```bash
mvn test
```

## Troubleshooting

### Server won't start
- Check if port 8080 is available
- Verify Java 17+ is installed: `java -version`
- Check application logs for errors

### MCP client can't connect
- Verify server is running: `curl http://localhost:8080/mcp/health`
- Check firewall settings
- Ensure client is configured with correct URL

### Schema not loading
- Check SchemaService implementation
- Verify JSON serialization is working
- Check application logs

## Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is open source and available under the MIT License.

## Support

For issues, questions, or contributions:
- Create an issue on GitHub
- Submit a pull request
- Contact the maintainers

## Roadmap

- [ ] Support for multiple database schemas
- [ ] Schema introspection from live databases
- [ ] WebSocket transport for MCP
- [ ] Schema versioning
- [ ] Query validation
- [ ] Performance metrics
- [ ] Docker support
- [ ] Kubernetes deployment configs

## References

- [Model Context Protocol Specification](https://spec.modelcontextprotocol.io/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [JSON-RPC 2.0 Specification](https://www.jsonrpc.org/specification)
