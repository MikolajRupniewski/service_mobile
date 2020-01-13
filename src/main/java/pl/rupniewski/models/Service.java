package pl.rupniewski.models;

import android.util.Log;

import java.io.Serializable;
import java.time.Duration;

public class Service implements Serializable {
    private Long id;
    private String name;
    private Duration durationObj;
    public String duration;
    private Double price;
    private Category serviceCategory;

    public Service(Long id, String name, Duration durationObj, String duration, Double price, Category serviceCategory) {
        this.id = id;
        this.name = name;
        this.durationObj = durationObj;
        this.duration = duration;
        this.price = price;
        this.serviceCategory = serviceCategory;
    }

    public Service() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Category getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(Category serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public Duration getDurationObj() {
        return durationObj;
    }

    public void setDurationObj(Duration durationObj) {
        this.durationObj = durationObj;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
