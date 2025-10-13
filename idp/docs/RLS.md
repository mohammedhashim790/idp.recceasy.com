# Tenant-based Row-Level Security (RLS) Utilities

This project includes a lightweight mechanism to access the current user's context (username, tenant_id, permissions) and use it to scope data access by tenant.

Components
- AuthContextFilter: Parses Authorization: Bearer <JWT> and optional X-Session-Token headers and, if valid, exposes JWT claims to the request via CurrentUser.
- CurrentUser: Simple accessor with methods getUsername(), getTenantId(), getPermissions(), hasPermission(permission).

Usage patterns
1) Inject CurrentUser where you need tenant or permission decisions:

```
@Service
public class SomeService {
  private final CurrentUser currentUser;
  private final SomeRepository repo;

  public SomeService(CurrentUser currentUser, SomeRepository repo) {
    this.currentUser = currentUser;
    this.repo = repo;
  }

  public List<SomeEntity> listForTenant() {
    String tenantId = currentUser.getTenantId();
    if (tenantId == null) throw new IllegalStateException("Tenant not resolved from token");
    return repo.findAllByTenantId(tenantId);
  }
}
```

2) Permissions:
- If your JWT includes a "permissions" claim (comma-separated), use currentUser.hasPermission("resource:action").
- The Login endpoint can issue such a claim when your user/role model is available.

Notes
- WebSecurityConfig still permits all to keep the service simple; AuthContextFilter does not block requests. It only fills context for consumers that opt-in.
- To enforce protection, add authorization rules or checks in controllers/services using CurrentUser.
- User model stores tenantId (string referencing Tenant.tenantId). Login places this as tenant_id in the JWT and the AuthContextFilter exposes it via CurrentUser.getTenantId(). Ensure that new users created via admin are assigned a valid tenantId.
