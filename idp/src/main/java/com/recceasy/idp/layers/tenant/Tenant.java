package com.recceasy.idp.layers.tenant;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.recceasy.idp.layers.country.Country;
import com.recceasy.idp.layers.domain.Domain;
import com.recceasy.idp.utils.RecceasyTime;
import jakarta.persistence.*;

@Entity
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String organisationName;

    private Long createdTime;

    @Column(nullable = false)
    private String country_id;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "country_id", referencedColumnName = "countryCode", insertable = false, updatable = false)
    private Country country;


    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(updatable = false)
    private Domain domain;


    @PrePersist
    protected void onCreate() {
        this.createdTime = RecceasyTime.now();
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }


    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }
}

