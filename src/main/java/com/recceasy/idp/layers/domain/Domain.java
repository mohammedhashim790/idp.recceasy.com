package com.recceasy.idp.layers.domain;


import jakarta.persistence.*;

@Entity
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String tenantId;


    @Column(name = "domainName", unique = true, nullable = false)
    private String domainName;


    public Domain(String domainName, String tenantId) {
        if (domainName == null || domainName.isEmpty()) {
            throw new IllegalArgumentException("Domain Name cannot be null");
        }
        if (!DomainValidator.validateDomain(domainName)) {
            throw new IllegalArgumentException("Domain Name is invalid. It must contains only alphabets");
        }
        this.domainName = DomainValidator.getSecondLevelDomain(domainName);
        this.tenantId = tenantId;
    }

    public Domain() {

    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
}

