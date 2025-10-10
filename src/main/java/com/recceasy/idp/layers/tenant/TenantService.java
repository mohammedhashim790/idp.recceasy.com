package com.recceasy.idp.layers.tenant;

import com.recceasy.idp.layers.domain.Domain;
import com.recceasy.idp.layers.tenant.utils.TenantUtils;
import com.recceasy.idp.layers.tenantSequence.TenantSequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TenantSequenceService sequenceService;

    @Transactional
    public Tenant createTenant(String name, String countryCode, String domainName) {
        if (tenantRepository.existsTenantByOrganisationName(name)) {
            throw new DuplicateKeyException("Organisation name already exists");
        }
        Long sequence = sequenceService.getNextSequence();
        String tenantId = TenantUtils.nextTenantId(countryCode, sequence);
        Tenant tenant = new Tenant();
        tenant.setTenantId(tenantId);
        tenant.setDomain(new Domain(domainName, tenantId));
        tenant.setOrganisationName(name);
        tenant.setCountry_id(countryCode);
        tenantRepository.save(tenant);
        tenant = tenantRepository.findById(tenant.getId()).orElse(null);
        return tenant;
    }

    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    public Tenant getTenantById(Long tenantId) {
        return tenantRepository.findById(tenantId).orElse(null);
    }

    public Tenant getTenantByTenantId(String tenantId) {
        return tenantRepository.findTenantByTenantId(tenantId).orElse(null);
    }


}

