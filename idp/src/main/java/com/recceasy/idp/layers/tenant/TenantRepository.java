package com.recceasy.idp.layers.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findTenantByTenantId(String tenantId);

    boolean existsTenantByOrganisationName(String name);
}

