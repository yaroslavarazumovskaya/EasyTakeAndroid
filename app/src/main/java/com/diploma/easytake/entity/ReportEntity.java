package com.diploma.easytake.entity;

import com.diploma.easytake.enums.Category;

public class ReportEntity {

    private Integer id;
    private String text;
    private Category category;
    private String date;

    public ReportEntity(){}

    public ReportEntity(Integer id, String text, Category category, String date) {
        this.id = id;
        this.text = text;
        this.category = category;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
