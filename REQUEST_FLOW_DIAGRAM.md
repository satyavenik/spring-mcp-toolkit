# MCP Server Request Flow Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                    SPRING MCP TOOLKIT SERVER                     │
│                     http://localhost:8080                        │
└─────────────────────────────────────────────────────────────────┘
                                 │
                                 │
                    ┌────────────┴────────────┐
                    │                         │
            ┌───────▼────────┐       ┌───────▼────────┐
            │  GET /mcp/health│       │  POST /mcp     │
            │                │       │                │
            │   Health Check │       │   MCP Protocol │
            └────────────────┘       └────────┬───────┘
                    │                         │
                    ▼                         │
              "Server Running"                │
                                             │
                    ┌────────────────────────┴────────────────────────┐
                    │                                                  │
                    │         JSON-RPC 2.0 REQUEST ROUTER             │
                    │                                                  │
                    └┬──────────┬──────────┬──────────┬───────────────┘
                     │          │          │          │
        ┌────────────▼┐  ┌─────▼─────┐  ┌▼─────────┐  ┌▼──────────────┐
        │ initialize  │  │tools/list │  │tools/call│  │resources/list │
        └────────────┬┘  └─────┬─────┘  └┬─────────┘  └┬──────────────┘
                     │          │         │             │
                     ▼          ▼         │             ▼
              ┌──────────┐ ┌────────┐    │      ┌────────────┐
              │ Server   │ │ Tool   │    │      │ Resource   │
              │ Info     │ │ List   │    │      │ List       │
              └──────────┘ └────────┘    │      └────────────┘
                                          │
                   ┌──────────────────────┼──────────────────────┐
                   │                      │                      │
            ┌──────▼────────┐    ┌───────▼──────┐    ┌─────────▼─────────┐
            │  get_schema   │    │get_templates │    │  get_template     │
            │               │    │              │    │                   │
            │  Tool         │    │  Tool        │    │  Tool             │
            └──────┬────────┘    └───────┬──────┘    └─────────┬─────────┘
                   │                     │                      │
                   │                     │                      │
            ┌──────▼────────┐    ┌───────▼──────┐    ┌─────────▼─────────┐
            │ SchemaService │    │TemplateSvc   │    │  TemplateSvc      │
            └──────┬────────┘    └───────┬──────┘    └─────────┬─────────┘
                   │                     │                      │
                   │                     │                      │
            ┌──────▼────────┐    ┌───────▼──────┐    ┌─────────▼─────────┐
            │ DatabaseSchema│    │ All Templates│    │ Single Template   │
            │               │    │              │    │                   │
            │ • users       │    │ • basic_sel  │    │ • basic_select    │
            │ • products    │    │ • inner_join │    │ • inner_join      │
            │ • orders      │    │ • left_join  │    │ • insert          │
            │ • order_items │    │ • insert     │    │ • update          │
            │               │    │ • update     │    │ • delete          │
            │ + Columns     │    │ • delete     │    │ • aggregate       │
            │ + Types       │    │ • aggregate  │    │ • subquery        │
            │ + ForeignKeys │    │ • subquery   │    │ • union           │
            │               │    │ • union      │    │ • left_join       │
            └───────────────┘    └──────────────┘    └───────────────────┘


═══════════════════════════════════════════════════════════════════
                        POSTMAN TEST FLOW
═══════════════════════════════════════════════════════════════════

STEP 1: HEALTH CHECK
┌──────────────────────────┐
│ GET /mcp/health          │──────────────► "MCP Server is running"
└──────────────────────────┘

STEP 2: INITIALIZE SESSION
┌──────────────────────────┐
│ POST /mcp                │
│ method: "initialize"     │──────────────► Server Info
│ params: {}               │                Protocol Version
└──────────────────────────┘                Capabilities

STEP 3: DISCOVER TOOLS
┌──────────────────────────┐
│ POST /mcp                │
│ method: "tools/list"     │──────────────► [get_schema,
│ params: {}               │                 get_templates,
└──────────────────────────┘                 get_template]

STEP 4: GET DATABASE SCHEMA
┌──────────────────────────┐
│ POST /mcp                │
│ method: "tools/call"     │──────────────► Database Structure
│ params: {                │                • Tables
│   name: "get_schema"     │                • Columns
│   arguments: {}          │                • Types
│ }                        │                • Relationships
└──────────────────────────┘

STEP 5: GET ALL TEMPLATES
┌──────────────────────────┐
│ POST /mcp                │
│ method: "tools/call"     │──────────────► Array of Templates
│ params: {                │                • Name
│   name: "get_templates"  │                • Description
│   arguments: {}          │                • Template
│ }                        │                • Parameters
└──────────────────────────┘                • Examples

STEP 6: GET SPECIFIC TEMPLATE
┌──────────────────────────┐
│ POST /mcp                │
│ method: "tools/call"     │──────────────► Single Template
│ params: {                │                {
│   name: "get_template"   │                  name: "basic_select"
│   arguments: {           │                  template: "SELECT..."
│     name: "basic_select" │                  parameters: [...]
│   }                      │                  example: "..."
│ }                        │                }
└──────────────────────────┘

STEP 7: LIST RESOURCES
┌──────────────────────────┐
│ POST /mcp                │
│ method: "resources/list" │──────────────► Resource URIs
│ params: {}               │                • schema://...
└──────────────────────────┘                • templates://...

STEP 8: READ RESOURCE
┌──────────────────────────┐
│ POST /mcp                │
│ method: "resources/read" │──────────────► Resource Content
│ params: {                │                (Schema or Templates)
│   uri: "schema://..."    │
│ }                        │
└──────────────────────────┘


═══════════════════════════════════════════════════════════════════
                    HOW AI USES THIS SERVER
═══════════════════════════════════════════════════════════════════

┌─────────────────────────────────────────────────────────────────┐
│                        AI ASSISTANT                              │
│                    (Claude, GPT, etc.)                           │
└───────────────────────────────┬─────────────────────────────────┘
                                │
                USER ASKS: "Show me all orders for john@example.com"
                                │
                                ▼
                    ┌───────────────────────┐
                    │ AI calls get_schema   │
                    └───────────┬───────────┘
                                │
                                ▼
                    ┌───────────────────────────────┐
                    │ AI learns:                    │
                    │ • users table has 'email'     │
                    │ • orders table exists         │
                    │ • orders.user_id → users.id   │
                    └───────────┬───────────────────┘
                                │
                                ▼
                    ┌───────────────────────┐
                    │ AI calls get_template │
                    │ name: "inner_join"    │
                    └───────────┬───────────┘
                                │
                                ▼
                    ┌───────────────────────────────┐
                    │ AI gets JOIN template:        │
                    │ SELECT {cols}                 │
                    │ FROM {table1}                 │
                    │ INNER JOIN {table2}           │
                    │ ON {condition}                │
                    │ WHERE {where}                 │
                    └───────────┬───────────────────┘
                                │
                                ▼
                    ┌───────────────────────────────┐
                    │ AI GENERATES SQL:             │
                    │                               │
                    │ SELECT o.*                    │
                    │ FROM orders o                 │
                    │ INNER JOIN users u            │
                    │   ON o.user_id = u.id         │
                    │ WHERE u.email =               │
                    │   'john@example.com'          │
                    └───────────┬───────────────────┘
                                │
                                ▼
                    ┌───────────────────────┐
                    │ Returns SQL to User   │
                    └───────────────────────┘


═══════════════════════════════════════════════════════════════════
                        ERROR HANDLING
═══════════════════════════════════════════════════════════════════

INVALID METHOD
┌──────────────────────────┐
│ method: "invalid_method" │──────────────► ERROR: -32603
└──────────────────────────┘                "Unknown method"

INVALID TOOL
┌──────────────────────────┐
│ name: "invalid_tool"     │──────────────► ERROR: -32603
└──────────────────────────┘                "Unknown tool"

TEMPLATE NOT FOUND
┌──────────────────────────┐
│ name: "nonexistent"      │──────────────► {error: "Template not found"}
└──────────────────────────┘


═══════════════════════════════════════════════════════════════════
                      JSON-RPC 2.0 FORMAT
═══════════════════════════════════════════════════════════════════

REQUEST                              RESPONSE (SUCCESS)
┌──────────────────────┐            ┌──────────────────────┐
│ {                    │            │ {                    │
│   "jsonrpc": "2.0",  │───────────►│   "jsonrpc": "2.0",  │
│   "id": 1,           │            │   "id": 1,           │
│   "method": "...",   │            │   "result": {...}    │
│   "params": {...}    │            │ }                    │
│ }                    │            └──────────────────────┘
└──────────────────────┘
                                    RESPONSE (ERROR)
                                    ┌──────────────────────┐
                                    │ {                    │
                                    │   "jsonrpc": "2.0",  │
                                    │   "id": 1,           │
                                    │   "error": {         │
                                    │     "code": -32603,  │
                                    │     "message": "..." │
                                    │   }                  │
                                    │ }                    │
                                    └──────────────────────┘
```

