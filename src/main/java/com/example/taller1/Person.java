package com.example.taller1;

public class Person {
    private int age;
    private String document;
    private String name;
    private String phone;
    private String email;

    public Person(String document, String name, String phone, String email, int age) {
        this.age = age;
        this.document = document;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    @Override
    public String toString() {
        return name + " (" + document + ")";
    }
}
