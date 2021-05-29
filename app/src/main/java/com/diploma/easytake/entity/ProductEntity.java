package com.diploma.easytake.entity;

public class ProductEntity {

    private Integer id;
    private String name;
    private Float price;
    private Float cost;
    private Integer amount;

    public ProductEntity(){

    }

    public ProductEntity(Integer id, String name, Float price, Float cost, Integer amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.amount = amount;
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
