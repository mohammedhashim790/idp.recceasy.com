package com.recceasy.idp.layers.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainRepository extends JpaRepository<Domain, Long> {

    Domain findDomainByDomainName(String domainName);


    Domain findDomainById(Long id);


    boolean existsDomainByDomainName(String domainName);

    boolean existsDomainByDomainNameContaining(String domainName);
}

