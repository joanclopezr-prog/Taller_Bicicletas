package com.example.parcial3;

public class Client extends Person {
    private String eps;

    public Client(String document, String name, String phone, String email, int age, String eps) {
        super(document, name, phone, email, age);
        this.eps = eps;
    }

    public Client(String document, String name, String phone, String email) {
        super(document, name, phone, email, 0);
        this.eps = "";
    }


    public Client(String eps, String document, String name, String phone, String email, int age) {
        super(document, name, phone, email, age);
        this.eps = eps;
    }

    public String getEps() {
        return eps;
    }

    public void setEps(String eps) {
        this.eps = eps;
    }

    @Override
    public String toString() {
        return getName() + " (" + getDocument() + ") - " + eps;
    }
}