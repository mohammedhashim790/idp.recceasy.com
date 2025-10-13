package com.recceasy.idp.layers.password;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Password {

    @Id
    @JsonIgnore
    protected String userId;

    @JsonIgnore
    protected String password;

    public Password(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public Password() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
