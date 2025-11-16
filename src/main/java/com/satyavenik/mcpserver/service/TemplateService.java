package com.satyavenik.mcpserver.service;

import com.satyavenik.mcpserver.model.SqlTemplate;
import com.satyavenik.mcpserver.model.TemplateParameter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Template Service - Provides SQL generation templates
 */
@Service
public class TemplateService {

    /**
     * Get all available SQL templates
     * @return List of SQL templates
     */
    public List<SqlTemplate> getAllTemplates() {
        List<SqlTemplate> templates = new ArrayList<>();

        // SELECT template
        templates.add(SqlTemplate.builder()
                .name("basic_select")
                .type("SELECT")
                .template("SELECT {columns} FROM {table} WHERE {condition}")
                .description("Basic SELECT query template")
                .parameters(Arrays.asList(
                        TemplateParameter.builder()
                                .name("columns")
                                .type("string")
                                .required(true)
                                .description("Comma-separated list of columns or *")
                                .build(),
                        TemplateParameter.builder()
                                .name("table")
                                .type("string")
                                .required(true)
                                .description("Table name")
                                .build(),
                        TemplateParameter.builder()
                                .name("condition")
                                .type("string")
                                .required(false)
                                .description("WHERE clause condition")
                                .build()
                ))
                .examples(Arrays.asList(
                        "SELECT * FROM users WHERE email = 'user@example.com'",
                        "SELECT id, username FROM users WHERE created_at > '2024-01-01'"
                ))
                .build());

        // JOIN template
        templates.add(SqlTemplate.builder()
                .name("inner_join")
                .type("JOIN")
                .template("SELECT {columns} FROM {table1} INNER JOIN {table2} ON {join_condition}")
                .description("INNER JOIN query template")
                .parameters(Arrays.asList(
                        TemplateParameter.builder()
                                .name("columns")
                                .type("string")
                                .required(true)
                                .description("Columns to select with table aliases")
                                .build(),
                        TemplateParameter.builder()
                                .name("table1")
                                .type("string")
                                .required(true)
                                .description("First table name")
                                .build(),
                        TemplateParameter.builder()
                                .name("table2")
                                .type("string")
                                .required(true)
                                .description("Second table name")
                                .build(),
                        TemplateParameter.builder()
                                .name("join_condition")
                                .type("string")
                                .required(true)
                                .description("JOIN condition")
                                .build()
                ))
                .examples(Arrays.asList(
                        "SELECT u.username, o.total FROM users u INNER JOIN orders o ON u.id = o.user_id",
                        "SELECT p.name, oi.quantity FROM products p INNER JOIN order_items oi ON p.id = oi.product_id"
                ))
                .build());

        // INSERT template
        templates.add(SqlTemplate.builder()
                .name("basic_insert")
                .type("INSERT")
                .template("INSERT INTO {table} ({columns}) VALUES ({values})")
                .description("Basic INSERT query template")
                .parameters(Arrays.asList(
                        TemplateParameter.builder()
                                .name("table")
                                .type("string")
                                .required(true)
                                .description("Table name")
                                .build(),
                        TemplateParameter.builder()
                                .name("columns")
                                .type("string")
                                .required(true)
                                .description("Comma-separated column names")
                                .build(),
                        TemplateParameter.builder()
                                .name("values")
                                .type("string")
                                .required(true)
                                .description("Comma-separated values")
                                .build()
                ))
                .examples(Arrays.asList(
                        "INSERT INTO users (username, email) VALUES ('john_doe', 'john@example.com')",
                        "INSERT INTO products (name, price, stock) VALUES ('Laptop', 999.99, 10)"
                ))
                .build());

        // UPDATE template
        templates.add(SqlTemplate.builder()
                .name("basic_update")
                .type("UPDATE")
                .template("UPDATE {table} SET {assignments} WHERE {condition}")
                .description("Basic UPDATE query template")
                .parameters(Arrays.asList(
                        TemplateParameter.builder()
                                .name("table")
                                .type("string")
                                .required(true)
                                .description("Table name")
                                .build(),
                        TemplateParameter.builder()
                                .name("assignments")
                                .type("string")
                                .required(true)
                                .description("Column assignments (e.g., col1 = val1, col2 = val2)")
                                .build(),
                        TemplateParameter.builder()
                                .name("condition")
                                .type("string")
                                .required(true)
                                .description("WHERE clause condition")
                                .build()
                ))
                .examples(Arrays.asList(
                        "UPDATE users SET email = 'newemail@example.com' WHERE id = 1",
                        "UPDATE products SET stock = stock - 1 WHERE id = 5"
                ))
                .build());

        // DELETE template
        templates.add(SqlTemplate.builder()
                .name("basic_delete")
                .type("DELETE")
                .template("DELETE FROM {table} WHERE {condition}")
                .description("Basic DELETE query template")
                .parameters(Arrays.asList(
                        TemplateParameter.builder()
                                .name("table")
                                .type("string")
                                .required(true)
                                .description("Table name")
                                .build(),
                        TemplateParameter.builder()
                                .name("condition")
                                .type("string")
                                .required(true)
                                .description("WHERE clause condition")
                                .build()
                ))
                .examples(Arrays.asList(
                        "DELETE FROM users WHERE id = 10",
                        "DELETE FROM orders WHERE status = 'cancelled'"
                ))
                .build());

        // Aggregate template
        templates.add(SqlTemplate.builder()
                .name("aggregate")
                .type("SELECT")
                .template("SELECT {group_columns}, {aggregate_function}({column}) FROM {table} GROUP BY {group_columns}")
                .description("Aggregate query with GROUP BY template")
                .parameters(Arrays.asList(
                        TemplateParameter.builder()
                                .name("group_columns")
                                .type("string")
                                .required(true)
                                .description("Columns to group by")
                                .build(),
                        TemplateParameter.builder()
                                .name("aggregate_function")
                                .type("string")
                                .required(true)
                                .description("Aggregate function (COUNT, SUM, AVG, MAX, MIN)")
                                .build(),
                        TemplateParameter.builder()
                                .name("column")
                                .type("string")
                                .required(true)
                                .description("Column to aggregate")
                                .build(),
                        TemplateParameter.builder()
                                .name("table")
                                .type("string")
                                .required(true)
                                .description("Table name")
                                .build()
                ))
                .examples(Arrays.asList(
                        "SELECT user_id, COUNT(*) FROM orders GROUP BY user_id",
                        "SELECT status, SUM(total) FROM orders GROUP BY status"
                ))
                .build());

        return templates;
    }

    /**
     * Get template by name
     * @param name Template name
     * @return SqlTemplate or null if not found
     */
    public SqlTemplate getTemplateByName(String name) {
        return getAllTemplates().stream()
                .filter(t -> t.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
