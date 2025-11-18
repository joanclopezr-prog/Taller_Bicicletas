package com.example.parcial3;

import java.time.LocalDate;

public class Appointment {
    private String id;
    private Patient patient;
    private Doctor doctor;
    private double total;
    private LocalDate date;

    public Appointment(String id, Patient patient, Doctor doctor, LocalDate date) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.total = calculateTotal();
    }

    private double calculateTotal() {

        return 100000.0;
    }

    public String getId() { return id; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public double getTotal() { return total; }
    public LocalDate getDate() { return date; }
}