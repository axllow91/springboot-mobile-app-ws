package com.mrn.mobileappws.ui.model.response;

import java.util.List;

// This is a response model (this will be returned so we don't need this many user details
// because we don't need to return sensitive information for our security)
public class UserRest {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<AddressesRest> addresses;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<AddressesRest> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressesRest> addresses) {
        this.addresses = addresses;
    }
}
