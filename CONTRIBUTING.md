# Contributing to Spring MCP Toolkit

Thank you for your interest in contributing to the Spring MCP Toolkit! This document provides guidelines for contributing to the project.

## How to Contribute

### Reporting Bugs

If you find a bug, please create an issue with:
- Clear description of the bug
- Steps to reproduce
- Expected behavior
- Actual behavior
- Environment details (OS, Java version, etc.)
- Relevant logs or error messages

### Suggesting Features

We welcome feature suggestions! Please create an issue with:
- Clear description of the feature
- Use case and benefits
- Potential implementation approach (optional)
- Any relevant examples or references

### Pull Requests

1. **Fork the repository**
   ```bash
   git clone https://github.com/satyavenik/spring-mcp-toolkit.git
   cd spring-mcp-toolkit
   ```

2. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make your changes**
   - Follow the coding standards (see below)
   - Add tests for new functionality
   - Update documentation as needed

4. **Test your changes**
   ```bash
   mvn clean test
   mvn clean package
   ```

5. **Commit your changes**
   ```bash
   git add .
   git commit -m "feat: add your feature description"
   ```
   
   Follow [Conventional Commits](https://www.conventionalcommits.org/):
   - `feat:` for new features
   - `fix:` for bug fixes
   - `docs:` for documentation changes
   - `test:` for test additions/changes
   - `refactor:` for code refactoring
   - `chore:` for maintenance tasks

6. **Push to your fork**
   ```bash
   git push origin feature/your-feature-name
   ```

7. **Create a Pull Request**
   - Provide a clear description of changes
   - Reference any related issues
   - Ensure all tests pass
   - Wait for review

## Coding Standards

### Java Code Style

- Follow standard Java naming conventions
- Use meaningful variable and method names
- Keep methods focused and small
- Add JavaDoc for public methods and classes
- Use Lombok annotations to reduce boilerplate

### Code Organization

```
src/
├── main/
│   ├── java/com/satyavenik/mcpserver/
│   │   ├── controller/    # REST controllers
│   │   ├── model/         # Data models
│   │   ├── protocol/      # MCP protocol models
│   │   ├── service/       # Business logic
│   │   └── config/        # Configuration classes
│   └── resources/
│       └── application.yml
└── test/
    └── java/com/satyavenik/mcpserver/
        ├── controller/    # Controller tests
        └── service/       # Service tests
```

### Testing

- Write unit tests for all new functionality
- Aim for high test coverage (>80%)
- Use descriptive test method names
- Follow AAA pattern (Arrange, Act, Assert)

Example test:
```java
@Test
void testGetExampleSchema() {
    // Arrange
    SchemaService service = new SchemaService();
    
    // Act
    DatabaseSchema schema = service.getExampleSchema();
    
    // Assert
    assertNotNull(schema);
    assertEquals("sample_ecommerce", schema.getName());
}
```

### Documentation

- Update README.md for major changes
- Add or update API documentation in API_EXAMPLES.md
- Update CONFIGURATION.md for configuration changes
- Include inline comments for complex logic
- Keep QUICKSTART.md up to date

## Development Setup

### Prerequisites

- JDK 17 or higher
- Maven 3.6+
- Git
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code)

### Setup Steps

1. Clone the repository
   ```bash
   git clone https://github.com/satyavenik/spring-mcp-toolkit.git
   cd spring-mcp-toolkit
   ```

2. Build the project
   ```bash
   mvn clean install
   ```

3. Run tests
   ```bash
   mvn test
   ```

4. Run the application
   ```bash
   mvn spring-boot:run
   ```

5. Verify it's working
   ```bash
   curl http://localhost:8080/mcp/health
   ```

### IDE Setup

#### IntelliJ IDEA

1. Import project as Maven project
2. Enable annotation processing for Lombok
   - Settings → Build, Execution, Deployment → Compiler → Annotation Processors
   - Check "Enable annotation processing"
3. Install Lombok plugin
4. Set Java SDK to 17+

#### Eclipse

1. Import as Existing Maven Project
2. Install Lombok
   - Download lombok.jar
   - Run: `java -jar lombok.jar`
   - Select Eclipse installation
3. Set Java Compiler to 17+

#### VS Code

1. Install Java Extension Pack
2. Install Lombok Annotations Support
3. Configure Java: `java.configuration.runtimes`

## Types of Contributions Needed

### High Priority

- [ ] Additional SQL templates (window functions, CTEs, etc.)
- [ ] Support for multiple database schemas
- [ ] Schema introspection from live databases
- [ ] WebSocket transport for MCP
- [ ] Performance optimizations
- [ ] Additional documentation and examples

### Medium Priority

- [ ] Docker and Kubernetes configurations
- [ ] CI/CD pipeline setup
- [ ] Integration tests
- [ ] Query validation
- [ ] Schema versioning
- [ ] Metrics and monitoring

### Good First Issues

- [ ] Add more SQL template examples
- [ ] Improve error messages
- [ ] Add logging for debugging
- [ ] Fix typos in documentation
- [ ] Add code comments
- [ ] Update dependencies

## Code Review Process

1. All submissions require review
2. Reviewers will check:
   - Code quality and style
   - Test coverage
   - Documentation
   - Backward compatibility
3. Address review comments
4. Once approved, maintainers will merge

## Release Process

We follow semantic versioning (MAJOR.MINOR.PATCH):
- MAJOR: Breaking changes
- MINOR: New features (backward compatible)
- PATCH: Bug fixes (backward compatible)

## Community

- Be respectful and constructive
- Help others in discussions
- Share knowledge and experiences
- Follow the Code of Conduct

## Questions?

- Create a discussion on GitHub
- Open an issue for bugs or features
- Check existing documentation

## License

By contributing, you agree that your contributions will be licensed under the MIT License.

## Thank You!

Your contributions help make Spring MCP Toolkit better for everyone. We appreciate your time and effort!
