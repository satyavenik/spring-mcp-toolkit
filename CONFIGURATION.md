# MCP Server Configuration Guide

## Overview

This guide provides detailed configuration instructions for setting up and running the Spring MCP Toolkit server.

## Server Configuration

### 1. Basic Configuration

The main configuration file is located at `src/main/resources/application.yml`:

```yaml
server:
  port: 8080                    # HTTP port for the server
  
spring:
  application:
    name: spring-mcp-toolkit    # Application name
    
logging:
  level:
    com.satyavenik.mcpserver: INFO  # Application logging level
    org.springframework: INFO        # Spring framework logging level
```

### 2. Port Configuration

To change the server port, modify `application.yml`:

```yaml
server:
  port: 9090  # Use any available port
```

Or set via environment variable:

```bash
export SERVER_PORT=9090
mvn spring-boot:run
```

Or via command line argument:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

### 3. Logging Configuration

Adjust logging levels in `application.yml`:

```yaml
logging:
  level:
    root: INFO
    com.satyavenik.mcpserver: DEBUG
    org.springframework.web: DEBUG
  file:
    name: logs/mcp-server.log
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

### 4. CORS Configuration (for web clients)

If you need to enable CORS, create a configuration class:

```java
@Configuration
public class CorsConfig {
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/mcp/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "POST", "OPTIONS")
                    .allowedHeaders("*");
            }
        };
    }
}
```

## MCP Client Configuration

### Claude Desktop Configuration

To use this server with Claude Desktop, add to your Claude configuration file:

**macOS**: `~/Library/Application Support/Claude/claude_desktop_config.json`
**Windows**: `%APPDATA%\Claude\claude_desktop_config.json`

```json
{
  "mcpServers": {
    "spring-mcp-toolkit": {
      "command": "curl",
      "args": [
        "-X", "POST",
        "http://localhost:8080/mcp",
        "-H", "Content-Type: application/json",
        "-d"
      ],
      "url": "http://localhost:8080/mcp"
    }
  }
}
```

### Generic MCP Client Configuration

For other MCP clients, use these connection details:

- **Protocol**: HTTP
- **Transport**: JSON-RPC 2.0
- **Endpoint**: `http://localhost:8080/mcp`
- **Methods**: POST for all requests

## Database Schema Configuration

### Custom Schema from Configuration File

Create a JSON file with your schema (e.g., `src/main/resources/schemas/myschema.json`):

```json
{
  "name": "my_database",
  "description": "My custom database",
  "tables": [
    {
      "name": "customers",
      "description": "Customer information",
      "columns": [
        {
          "name": "id",
          "type": "INTEGER",
          "nullable": false,
          "description": "Primary key"
        },
        {
          "name": "name",
          "type": "VARCHAR(100)",
          "nullable": false,
          "description": "Customer name"
        }
      ],
      "primaryKey": ["id"],
      "foreignKeys": []
    }
  ]
}
```

Then load it in `SchemaService`:

```java
@Service
public class SchemaService {
    
    @Value("classpath:schemas/myschema.json")
    private Resource schemaResource;
    
    public DatabaseSchema getExampleSchema() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
            schemaResource.getInputStream(), 
            DatabaseSchema.class
        );
    }
}
```

### Schema from Database Metadata

To load schema from a live database, add JDBC dependencies:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

Configure datasource in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: 
```

Then implement schema introspection:

```java
@Service
public class SchemaService {
    
    @Autowired
    private DataSource dataSource;
    
    public DatabaseSchema getSchemaFromDatabase() throws SQLException {
        DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
        // Use metaData to introspect database schema
        // Build and return DatabaseSchema object
    }
}
```

## SQL Template Configuration

### Adding Custom Templates

Create custom templates in `TemplateService`:

```java
@Service
public class TemplateService {
    
    public List<SqlTemplate> getAllTemplates() {
        List<SqlTemplate> templates = new ArrayList<>();
        
        // Add your custom template
        templates.add(SqlTemplate.builder()
            .name("custom_query")
            .type("SELECT")
            .template("SELECT {columns} FROM {table} WHERE {condition} ORDER BY {order_by}")
            .description("Custom SELECT with ORDER BY")
            .parameters(Arrays.asList(
                TemplateParameter.builder()
                    .name("columns")
                    .type("string")
                    .required(true)
                    .description("Columns to select")
                    .build(),
                // Add more parameters...
            ))
            .examples(Arrays.asList(
                "SELECT * FROM users WHERE active = true ORDER BY created_at DESC"
            ))
            .build());
        
        return templates;
    }
}
```

### Template Configuration File

Store templates in a JSON configuration file:

`src/main/resources/templates/sql-templates.json`:

```json
{
  "templates": [
    {
      "name": "window_function",
      "type": "SELECT",
      "template": "SELECT {columns}, {window_function} OVER (PARTITION BY {partition_by} ORDER BY {order_by}) FROM {table}",
      "description": "Window function query template",
      "parameters": [
        {
          "name": "columns",
          "type": "string",
          "required": true,
          "description": "Columns to select"
        }
      ],
      "examples": [
        "SELECT user_id, order_id, ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY created_at) FROM orders"
      ]
    }
  ]
}
```

## Security Configuration

### Basic Authentication

Add Spring Security dependency:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

Configure in `application.yml`:

```yaml
spring:
  security:
    user:
      name: admin
      password: changeme
```

Or create custom security config:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/mcp/health").permitAll()
                .requestMatchers("/mcp/**").authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
```

### API Key Authentication

Implement custom API key filter:

```java
@Component
public class ApiKeyFilter extends OncePerRequestFilter {
    
    @Value("${api.key}")
    private String apiKey;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) 
            throws ServletException, IOException {
        
        String requestApiKey = request.getHeader("X-API-Key");
        
        if (apiKey.equals(requestApiKey)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid API Key");
        }
    }
}
```

Add to `application.yml`:

```yaml
api:
  key: your-secret-api-key-here
```

## Production Deployment

### Building for Production

```bash
# Build JAR file
mvn clean package

# Run the JAR
java -jar target/spring-mcp-toolkit-1.0.0-SNAPSHOT.jar
```

### Production Configuration

Create `application-prod.yml`:

```yaml
server:
  port: 8080
  compression:
    enabled: true
  http2:
    enabled: true

spring:
  application:
    name: spring-mcp-toolkit
    
logging:
  level:
    root: WARN
    com.satyavenik.mcpserver: INFO
  file:
    name: /var/log/mcp-server/application.log
    max-size: 10MB
    max-history: 30

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: when-authorized
```

Run with production profile:

```bash
java -jar target/spring-mcp-toolkit-1.0.0-SNAPSHOT.jar --spring.profiles.active=prod
```

### Environment Variables

Set environment variables for sensitive configuration:

```bash
export SERVER_PORT=8080
export API_KEY=your-secret-key
export SPRING_PROFILES_ACTIVE=prod
java -jar target/spring-mcp-toolkit-1.0.0-SNAPSHOT.jar
```

### Systemd Service

Create `/etc/systemd/system/mcp-server.service`:

```ini
[Unit]
Description=Spring MCP Toolkit Server
After=network.target

[Service]
Type=simple
User=mcpserver
WorkingDirectory=/opt/mcp-server
ExecStart=/usr/bin/java -jar /opt/mcp-server/spring-mcp-toolkit-1.0.0-SNAPSHOT.jar
Restart=always
RestartSec=10
Environment="SERVER_PORT=8080"
Environment="SPRING_PROFILES_ACTIVE=prod"

[Install]
WantedBy=multi-user.target
```

Enable and start:

```bash
sudo systemctl daemon-reload
sudo systemctl enable mcp-server
sudo systemctl start mcp-server
sudo systemctl status mcp-server
```

### Docker Configuration

Create `Dockerfile`:

```dockerfile
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/spring-mcp-toolkit-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:

```bash
docker build -t spring-mcp-toolkit .
docker run -p 8080:8080 spring-mcp-toolkit
```

With environment variables:

```bash
docker run -p 8080:8080 \
  -e SERVER_PORT=8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  spring-mcp-toolkit
```

### Nginx Reverse Proxy

Configure Nginx as reverse proxy:

```nginx
server {
    listen 80;
    server_name mcp.example.com;
    
    location /mcp {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

## Monitoring and Observability

### Actuator Configuration

Add actuator dependency (already included):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Configure endpoints in `application.yml`:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always
  metrics:
    export:
      prometheus:
        enabled: true
```

Access metrics:
- Health: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`
- Info: `http://localhost:8080/actuator/info`

## Troubleshooting

### Common Configuration Issues

1. **Port already in use**
   - Change port in application.yml
   - Find and kill process: `lsof -ti:8080 | xargs kill -9`

2. **Out of memory**
   - Increase heap size: `java -Xmx2g -jar app.jar`

3. **Connection refused**
   - Check if server is running
   - Verify firewall rules
   - Check network connectivity

### Logging Configuration for Debugging

```yaml
logging:
  level:
    root: DEBUG
    com.satyavenik.mcpserver: TRACE
    org.springframework.web: DEBUG
    org.springframework.web.servlet.mvc: TRACE
```

## Best Practices

1. **Use profiles** for different environments (dev, test, prod)
2. **Externalize configuration** using environment variables
3. **Enable compression** for better performance
4. **Set up monitoring** with actuator endpoints
5. **Use HTTPS** in production
6. **Implement rate limiting** for public APIs
7. **Configure proper logging** for troubleshooting
8. **Regular backups** of configuration files
9. **Document custom changes** to schema and templates
10. **Version your schema** definitions

## References

- [Spring Boot Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Model Context Protocol](https://spec.modelcontextprotocol.io/)
