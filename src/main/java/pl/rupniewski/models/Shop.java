package pl.rupniewski.models;

import java.io.Serializable;
import java.util.List;

public class Shop implements Serializable {
    private Long id;
    private Category category;
    private String name;
    private GeoLocation geoLocation;
    private List<String> pictures;
    private ServicePlace servicePlace;
    private List<Service> services;
    private List<Employee> employees;
    private List<Order> orders;
    private Double maxDistance = 0.0;

    public Shop(Long id, Category category, String name, GeoLocation geoLocation, List<String> pictures, ServicePlace servicePlace, List<Service> services, List<Employee> employees, Double maxDistance) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.geoLocation = geoLocation;
        this.pictures = pictures;
        this.servicePlace = servicePlace;
        this.services = services;
        this.employees = employees;
        this.maxDistance = maxDistance;
    }

    public Shop() {
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public ServicePlace getServicePlace() {
        return servicePlace;
    }

    public void setServicePlace(ServicePlace servicePlace) {
        this.servicePlace = servicePlace;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(Double maxDistance) {
        this.maxDistance = maxDistance;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", geoLocation=" + geoLocation +
                ", pictures=" + pictures +
                ", servicePlace=" + servicePlace +
                ", services=" + services +
                ", employees=" + employees +
                ", maxDistance=" + maxDistance +
                '}';
    }
}
