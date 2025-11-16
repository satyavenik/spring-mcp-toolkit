package com.satyavenik.mcpserver.service;

import com.satyavenik.mcpserver.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Schema Service - Provides database schema information for SQL generation
 */
@Service
public class SchemaService {

    /**
     * Get example database schema for demonstration
     * @return DatabaseSchema with sample tables
     */
    public DatabaseSchema getExampleSchema() {
        return DatabaseSchema.builder()
                .name("sample_ecommerce")
                .description("Sample e-commerce database schema")
                .tables(createExampleTables())
                .build();
    }

    private List<TableSchema> createExampleTables() {
        List<TableSchema> tables = new ArrayList<>();

        // Users table
        tables.add(TableSchema.builder()
                .name("users")
                .description("User accounts")
                .columns(Arrays.asList(
                        ColumnSchema.builder()
                                .name("id")
                                .type("INTEGER")
                                .nullable(false)
                                .description("Primary key")
                                .build(),
                        ColumnSchema.builder()
                                .name("username")
                                .type("VARCHAR(50)")
                                .nullable(false)
                                .description("Unique username")
                                .build(),
                        ColumnSchema.builder()
                                .name("email")
                                .type("VARCHAR(100)")
                                .nullable(false)
                                .description("User email address")
                                .build(),
                        ColumnSchema.builder()
                                .name("created_at")
                                .type("TIMESTAMP")
                                .nullable(false)
                                .defaultValue("CURRENT_TIMESTAMP")
                                .description("Account creation timestamp")
                                .build()
                ))
                .primaryKey(Arrays.asList("id"))
                .foreignKeys(new ArrayList<>())
                .build());

        // Products table
        tables.add(TableSchema.builder()
                .name("products")
                .description("Product catalog")
                .columns(Arrays.asList(
                        ColumnSchema.builder()
                                .name("id")
                                .type("INTEGER")
                                .nullable(false)
                                .description("Primary key")
                                .build(),
                        ColumnSchema.builder()
                                .name("name")
                                .type("VARCHAR(100)")
                                .nullable(false)
                                .description("Product name")
                                .build(),
                        ColumnSchema.builder()
                                .name("description")
                                .type("TEXT")
                                .nullable(true)
                                .description("Product description")
                                .build(),
                        ColumnSchema.builder()
                                .name("price")
                                .type("DECIMAL(10,2)")
                                .nullable(false)
                                .description("Product price")
                                .build(),
                        ColumnSchema.builder()
                                .name("stock")
                                .type("INTEGER")
                                .nullable(false)
                                .defaultValue("0")
                                .description("Available stock")
                                .build()
                ))
                .primaryKey(Arrays.asList("id"))
                .foreignKeys(new ArrayList<>())
                .build());

        // Orders table
        tables.add(TableSchema.builder()
                .name("orders")
                .description("Customer orders")
                .columns(Arrays.asList(
                        ColumnSchema.builder()
                                .name("id")
                                .type("INTEGER")
                                .nullable(false)
                                .description("Primary key")
                                .build(),
                        ColumnSchema.builder()
                                .name("user_id")
                                .type("INTEGER")
                                .nullable(false)
                                .description("Foreign key to users")
                                .build(),
                        ColumnSchema.builder()
                                .name("total")
                                .type("DECIMAL(10,2)")
                                .nullable(false)
                                .description("Order total amount")
                                .build(),
                        ColumnSchema.builder()
                                .name("status")
                                .type("VARCHAR(20)")
                                .nullable(false)
                                .description("Order status")
                                .build(),
                        ColumnSchema.builder()
                                .name("created_at")
                                .type("TIMESTAMP")
                                .nullable(false)
                                .defaultValue("CURRENT_TIMESTAMP")
                                .description("Order creation timestamp")
                                .build()
                ))
                .primaryKey(Arrays.asList("id"))
                .foreignKeys(Arrays.asList(
                        ForeignKey.builder()
                                .columns(Arrays.asList("user_id"))
                                .referencedTable("users")
                                .referencedColumns(Arrays.asList("id"))
                                .build()
                ))
                .build());

        // Order Items table
        tables.add(TableSchema.builder()
                .name("order_items")
                .description("Items in orders")
                .columns(Arrays.asList(
                        ColumnSchema.builder()
                                .name("id")
                                .type("INTEGER")
                                .nullable(false)
                                .description("Primary key")
                                .build(),
                        ColumnSchema.builder()
                                .name("order_id")
                                .type("INTEGER")
                                .nullable(false)
                                .description("Foreign key to orders")
                                .build(),
                        ColumnSchema.builder()
                                .name("product_id")
                                .type("INTEGER")
                                .nullable(false)
                                .description("Foreign key to products")
                                .build(),
                        ColumnSchema.builder()
                                .name("quantity")
                                .type("INTEGER")
                                .nullable(false)
                                .description("Quantity ordered")
                                .build(),
                        ColumnSchema.builder()
                                .name("price")
                                .type("DECIMAL(10,2)")
                                .nullable(false)
                                .description("Price at time of order")
                                .build()
                ))
                .primaryKey(Arrays.asList("id"))
                .foreignKeys(Arrays.asList(
                        ForeignKey.builder()
                                .columns(Arrays.asList("order_id"))
                                .referencedTable("orders")
                                .referencedColumns(Arrays.asList("id"))
                                .build(),
                        ForeignKey.builder()
                                .columns(Arrays.asList("product_id"))
                                .referencedTable("products")
                                .referencedColumns(Arrays.asList("id"))
                                .build()
                ))
                .build());

        return tables;
    }
}
