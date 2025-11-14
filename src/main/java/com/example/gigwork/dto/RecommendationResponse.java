package com.example.gigwork.dto;

public class RecommendationResponse {
    private Long id;
    private String title;
    private String category;
    private String company;
    private String location;
    private String salary;
    private String salaryType;
    private String description;
    private Integer suitability; // 적합도 점수 (0-100)

    public RecommendationResponse() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSuitability() {
        return suitability;
    }

    public void setSuitability(Integer suitability) {
        this.suitability = suitability;
    }
}
