package com.satyavenik.mcpserver.service;

import com.satyavenik.mcpserver.model.DatabaseSchema;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SchemaServiceTest {

    @Autowired
    private SchemaService schemaService;

    @Test
    void testGetExampleSchema() {
        DatabaseSchema schema = schemaService.getExampleSchema();
        
        assertNotNull(schema);
        assertEquals("sample_ecommerce", schema.getName());
        assertNotNull(schema.getTables());
        assertEquals(4, schema.getTables().size());
        
        // Verify users table exists
        assertTrue(schema.getTables().stream()
                .anyMatch(t -> t.getName().equals("users")));
    }
}
