package com.helper.eissoso20kabakcihosseini.models;

public class Address {

    private String city;
    private String street;
    private String streetNumber;
    private Integer zipcode;

    public Address(String city, String street, String streetNumber, Integer zipcode) {
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }
}