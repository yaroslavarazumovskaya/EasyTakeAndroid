package com.diploma.easytake.entity;

public class OrderEntity {

    private Integer id;
    private String date;
    private Integer duration;
    private Boolean activeStatus;
    private ProductEntity productEntity;
    private ClientEntity clientEntity;

    public OrderEntity(){}

    public OrderEntity(Integer id, String date, Integer duration, Boolean activeStatus,
                       ProductEntity productEntity, ClientEntity clientEntity) {
        this.id = id;
        this.date = date;
        this.duration = duration;
        this.activeStatus = activeStatus;
        this.productEntity = productEntity;
        this.clientEntity = clientEntity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }

    public ClientEntity getClientEntity() {
        return clientEntity;
    }

    public void setClientEntity(ClientEntity clientEntity) {
        this.clientEntity = clientEntity;
    }
}
