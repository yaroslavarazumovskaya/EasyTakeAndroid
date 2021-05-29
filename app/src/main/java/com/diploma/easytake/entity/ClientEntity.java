package com.diploma.easytake.entity;

public class ClientEntity {

    private Integer id;
    private String name;
    private String phone;
    private String passport;

    public ClientEntity(Integer id, String name, String phone, String passport) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.passport = passport;
    }

    public ClientEntity(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }
}
