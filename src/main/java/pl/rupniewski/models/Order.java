package pl.rupniewski.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Order implements Serializable, Comparable<Order>{

    private Long id;
    private Users users;
    private Shop shop;
    private Service service;
    private Date date;
    private Employee employee;
    private Double rating;
    private String comment;

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(date, order.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }



    @Override
    public int compareTo(Order o) {
        return this.getDate().compareTo(o.getDate());
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", users=" + users +
                ", shop=" + shop +
                ", service=" + service +
                ", date=" + date +
                ", employee=" + employee +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}
