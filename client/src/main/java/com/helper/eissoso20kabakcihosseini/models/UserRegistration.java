package com.helper.eissoso20kabakcihosseini.models;

public class UserRegistration {

    private String email;
    private String password;
    private Address address;


    public UserRegistration(String email, String password, Address address) {
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
