package com.recceasy.idp.dto.tenant;

public class RegisterTenant {

    private String tenantName;

    private String countryCode;

    private String domain;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantName() {
        return tenantName;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
