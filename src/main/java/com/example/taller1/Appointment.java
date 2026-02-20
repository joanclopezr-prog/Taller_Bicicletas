package com.example.taller1;

import java.time.LocalDate;

public class Appointment {
    private String id;
    private Client client;
    private Mechanic mechanic;
    private double total;
    private LocalDate date;

    public Appointment(String id, Client client, Mechanic mechanic, LocalDate date) {
        this.id = id;
        this.client = client;
        this.mechanic = mechanic;
        this.date = date;
        this.total = calculateTotal();
    }

    private double calculateTotal() {
        // Lógica de cálculo según especialidad del mecánico
        return 100000.0; // Placeholder
    }

    public String getId() { return id; }
    public Client getClient() { return client; }
    public Mechanic getMechanic() { return mechanic; }
    public double getTotal() { return total; }
    public LocalDate getDate() { return date; }
}