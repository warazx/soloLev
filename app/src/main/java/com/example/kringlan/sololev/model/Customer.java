package com.example.kringlan.sololev.model;

public class Customer {
    private static int idCounter = 0;

    private int id;
    private String name;
    private String phoneNumber;
    private String address;
    private long createdDate;

    public Customer(String name, String phoneNumber, String address) {
        this.id = idCounter;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.createdDate = System.currentTimeMillis();
        idCounter++;
    }

    public Customer(int id, String name, String phoneNumber, String address, long createdDate) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.createdDate = createdDate;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Customer.idCounter = idCounter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }
}
