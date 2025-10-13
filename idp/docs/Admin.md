# Admin User Management

Endpoints under /admin/users allow administrators to manage users.

Roles
- One role per user: SUPER_USER (via /auth/register), ADMIN, INTERNAL, EXTERNAL.

Important tenant behavior
- Self-registration (/auth/register) may result in a user with null tenantId (no tenant context required).
- Admin-created users must be assigned to the admin’s current tenant. The tenant is resolved from the caller's JWT/context (CurrentUser.getTenantId()) and is not accepted in the request body.

Endpoints
- POST /admin/users
  - Body: { "username": "<email or phone>", "phone": "<optional>", "role": "ADMIN|INTERNAL|EXTERNAL" }
  - Behavior: Creates user with status RESET_REQUIRED and enabled=false. Temporary password is generated and stored. User is mapped to the tenantId derived from the admin’s token (tenant_id claim). If the tenant cannot be resolved or does not exist, a 400 is returned.
  - Response: { userId, username, role, temporaryPassword, status }
  - Notes: SUPER_USER cannot be created via admin endpoint. The tenant_id must refer to an existing Tenant.tenantId; this is validated using the context-derived tenant.
- PUT /admin/users/{id}/force-reset
  - Sets user status to RESET_REQUIRED, enabled=false, and rotates a temporary password.
- DELETE /admin/users/{id}
  - Deletes the user. (Associated artifacts like password rows may need cascading in future iterations.)

Username policy for admin create
- Admin may set username to either an email or a phone number. Minimal validation is applied; integrate stricter validators as needed.

Register endpoint moved
- /auth/register now lives in AuthController. It still validates email and password and creates a SUPER_USER. Tenant remains nullable for this flow.

Security
- Current security config permits all requests for simplicity. Use CurrentUser or Spring Security method security to guard /admin/** routes in production.
