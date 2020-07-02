package com.helper.eissoso20kabakcihosseini.models;

public class User {

    private String email;
    private Address address;
    private String id;

    public User() {
    }

    public User(String email, Address address, String id) {
        this.email = email;
        this.address = address;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", address=" + address +
                ", id='" + id + '\'' +
                '}';
    }
}
