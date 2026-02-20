package com.example.taller1;

public class Bicycle {
    private String serialNumber;
    private String brand;
    private String type;
    private String color;
    private int year;
    private Client owner;

    public Bicycle(String serialNumber, String brand, String type, String color, int year, Client owner) {
        this.serialNumber = serialNumber;
        this.brand = brand;
        this.type = type;
        this.color = color;
        this.year = year;
        this.owner = owner;
    }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public Client getOwner() { return owner; }
    public void setOwner(Client owner) { this.owner = owner; }

    public String getOwnerDocument() {
        return owner != null ? owner.getDocument() : "";
    }

    public String getOwnerName() {
        return owner != null ? owner.getName() : "";
    }

    @Override
    public String toString() {
        return brand + " - " + type + " (" + color + ") - Serial: " + serialNumber;
    }
}
