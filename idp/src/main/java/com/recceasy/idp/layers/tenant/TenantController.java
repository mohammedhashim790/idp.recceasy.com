package com.recceasy.idp.layers.tenant;


import com.recceasy.idp.dto.tenant.RegisterTenant;
import com.recceasy.idp.handlers.ExceptionHandlers.DomainAlreadyExistsException;
import com.recceasy.idp.layers.domain.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/tenant")
@RestController
public class TenantController {

    @Autowired
    private TenantService tenantService;
    @Autowired
    private DomainService domainService;


    @PostMapping("/register")
    public Tenant register(@RequestBody RegisterTenant registerTenant) throws DomainAlreadyExistsException {
        if (domainService.domainExists(registerTenant.getDomain())) {
            throw new DomainAlreadyExistsException();
        }
        return tenantService.createTenant(registerTenant.getTenantName(), registerTenant.getCountryCode(), registerTenant.getDomain());
    }

    @GetMapping
    public List<Tenant> list() {
        return tenantService.getAllTenants();
    }

    @GetMapping("/{id}")
    public Tenant getById(@PathVariable("id") String id) {
        return tenantService.getTenantByTenantId(id);
    }


}
