package com.recceasy.idp.layers.domain;

import com.recceasy.idp.handlers.ExceptionHandlers.DomainAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainService {

    @Autowired
    private DomainRepository domainRepository;


    public Domain createDomain(Domain domain) throws DomainAlreadyExistsException {
        if (this.domainExists(domain)) {
            throw new DomainAlreadyExistsException();
        }
        return domainRepository.save(domain);
    }

    public Domain findDomainById(Long id) {
        return domainRepository.findDomainById(id);
    }

    public boolean domainExists(Domain domain) {
        return domainRepository.existsDomainByDomainName(domain.getDomainName());
    }

    public boolean domainExists(String domainName) {
        boolean exists = domainRepository.existsDomainByDomainNameContaining(domainName);
        return domainRepository.existsDomainByDomainName(domainName);
    }




}

