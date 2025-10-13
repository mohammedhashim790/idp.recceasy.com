package com.recceasy.idp.layers.tenantSequence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantSequenceRepository extends JpaRepository<TenantSequence, Long> {
}
