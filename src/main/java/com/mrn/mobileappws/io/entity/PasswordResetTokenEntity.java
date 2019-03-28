package com.mrn.mobileappws.io.entity;

import javax.persistence.*;
import java.io.Serializable;

// this will be persisted in our db
@Entity(name = "password_reset_tokens")
public class PasswordResetTokenEntity implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    private String token;

    // one token can be associated for one user
    @OneToOne()
    @JoinColumn(name = "users_id")
    private UserEntity userDetails;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserEntity getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserEntity userDetails) {
        this.userDetails = userDetails;
    }
}
