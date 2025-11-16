package com.satyavenik.mcpserver.service;

import com.satyavenik.mcpserver.model.SqlTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TemplateServiceTest {

    @Autowired
    private TemplateService templateService;

    @Test
    void testGetAllTemplates() {
        List<SqlTemplate> templates = templateService.getAllTemplates();
        
        assertNotNull(templates);
        assertTrue(templates.size() >= 6);
        
        // Verify basic_select template exists
        assertTrue(templates.stream()
                .anyMatch(t -> t.getName().equals("basic_select")));
    }

    @Test
    void testGetTemplateByName() {
        SqlTemplate template = templateService.getTemplateByName("basic_select");
        
        assertNotNull(template);
        assertEquals("basic_select", template.getName());
        assertEquals("SELECT", template.getType());
        assertNotNull(template.getTemplate());
    }

    @Test
    void testGetTemplateByNameNotFound() {
        SqlTemplate template = templateService.getTemplateByName("nonexistent");
        
        assertNull(template);
    }
}
