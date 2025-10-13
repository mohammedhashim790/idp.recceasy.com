package com.recceasy.idp.layers.tenantSequence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenantSequenceService {

    @Autowired
    private TenantSequenceRepository sequenceRepository;

    @Transactional
    public Long getNextSequence() {
        TenantSequence seq = sequenceRepository.save(new TenantSequence());
        return seq.getId();
    }
}
