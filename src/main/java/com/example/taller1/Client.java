package com.example.taller1;

public class Client extends Person {

    public Client(String document, String name, String phone, String email, int age) {
        super(document, name, phone, email, age);

    }

    public Client(String document, String name, String phone, String email) {
        super(document, name, phone, email, 0);

    }
    @Override
    public String toString() {
        return getName() + " (" + getDocument() + ") - ";
    }
}