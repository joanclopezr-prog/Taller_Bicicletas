package com.example.taller1;

public class Mechanic extends Person {
    private String specialty;

    public Mechanic(String document, String name, String phone, String email, int age, String specialty) {
        super(document, name, phone, email, age);
        this.specialty = specialty;
    }


    public Mechanic(String document, String name, String specialty) {
        super(document, name, "", "", 0);
        this.specialty = specialty;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    @Override
    public String toString() {
        return getName() + " (" + getDocument() + ") - " + specialty;
    }
}