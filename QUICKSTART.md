# Quick Start Guide

Get the Spring MCP Toolkit up and running in minutes.

## Prerequisites

- Java 17 or higher installed
- Maven 3.6+ installed

## Quick Start Steps

### 1. Clone and Build

```bash
# Clone the repository
git clone https://github.com/satyavenik/spring-mcp-toolkit.git
cd spring-mcp-toolkit

# Build the project
mvn clean install

# Start the server
mvn spring-boot:run
```

The server will start on `http://localhost:8080`

### 2. Verify Server is Running

```bash
curl http://localhost:8080/mcp/health
```

Expected response: `MCP Server is running`

### 3. Test MCP Protocol

#### Initialize Connection

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

#### Get Database Schema

```bash
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
  }' | jq .
```

#### Get SQL Templates

```bash
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
  }' | jq .
```

## What You Get

### Sample E-commerce Database Schema

The server provides a sample schema with:
- **users** table (id, username, email, created_at)
- **products** table (id, name, description, price, stock)
- **orders** table (id, user_id, total, status, created_at)
- **order_items** table (id, order_id, product_id, quantity, price)

### SQL Templates

Pre-built templates for:
- SELECT queries
- JOIN operations
- INSERT statements
- UPDATE statements
- DELETE statements
- Aggregate queries

## Using with Claude Desktop

Add to your Claude Desktop configuration:

**macOS**: `~/Library/Application Support/Claude/claude_desktop_config.json`

```json
{
  "mcpServers": {
    "spring-mcp-toolkit": {
      "url": "http://localhost:8080/mcp"
    }
  }
}
```

Restart Claude Desktop, and it will automatically connect to your MCP server.

## Common Use Cases

### 1. Ask Claude to Generate SQL

"Using the database schema, write a SQL query to find all orders for a specific user along with their details."

### 2. Explore Database Structure

"What tables are available in the database and how are they related?"

### 3. Generate Complex Queries

"Create a query to find the top 5 users with the most orders and their total spending."

## Next Steps

- Read the [full README](README.md) for detailed information
- Check [CONFIGURATION.md](CONFIGURATION.md) for advanced setup
- Customize the schema in `SchemaService.java`
- Add your own SQL templates in `TemplateService.java`

## Troubleshooting

### Server won't start
```bash
# Check if Java is installed
java -version

# Check if port 8080 is available
lsof -i :8080
```

### Maven build fails
```bash
# Clean and rebuild
mvn clean install -U
```

### Can't connect from client
```bash
# Verify server is running
curl http://localhost:8080/mcp/health
```

## Getting Help

- Check the logs: Look for error messages in console output
- Review documentation: See [README.md](README.md) for detailed docs
- Open an issue: Report problems on GitHub

## Example Session

```bash
# 1. Start server
mvn spring-boot:run

# 2. In another terminal, test the connection
curl http://localhost:8080/mcp/health

# 3. Initialize MCP
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{}}'

# 4. List available tools
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":2,"method":"tools/list","params":{}}'

# 5. Get schema
curl -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":3,"method":"tools/call","params":{"name":"get_schema","arguments":{}}}'

# Success! Your MCP server is working.
```

That's it! You're now ready to use the Spring MCP Toolkit for SQL generation.
