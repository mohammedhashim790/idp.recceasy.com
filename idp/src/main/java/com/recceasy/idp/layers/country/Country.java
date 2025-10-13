package com.recceasy.idp.layers.country;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

import java.sql.Timestamp;

@Entity

public class Country {

    @Id
    @Column(nullable = false, unique = true)
    private String countryCode;

    @Column(unique = true)
    private String countryName;

    @Column
    private String region;


    private Long createdTime;

    @PrePersist
    protected void onCreate() {
        this.createdTime = System.currentTimeMillis();
    }


    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }
}
