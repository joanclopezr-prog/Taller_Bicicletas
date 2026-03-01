package com.example.taller1;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private String id;
    private Client client;
    private Mechanic mechanic;
    private Bicycle bicycle;
    private LocalDate entryDate;
    private LocalTime entryTime;
    private String serviceReason;
    private String diagnosis;
    private String workDone;
    private double totalCost;

    public Appointment(String id, Client client, Mechanic mechanic, Bicycle bicycle,
                       LocalDate entryDate, LocalTime entryTime, String serviceReason,
                       String diagnosis, String workDone) {
        this.id = id;
        this.client = client;
        this.mechanic = mechanic;
        this.bicycle = bicycle;
        this.entryDate = entryDate;
        this.entryTime = entryTime;
        this.serviceReason = serviceReason;
        this.diagnosis = diagnosis;
        this.workDone = workDone;
        this.totalCost = calculateTotal();
    }

    private double calculateTotal() {
        if (mechanic != null) {
            switch (mechanic.getSpecialty()) {
                case "Frenos": return 150000.0;
                case "Transmisión": return 180000.0;
                case "Suspensión": return 120000.0;
                case "Eléctrica": return 120000.0;
                default: return 100000.0;
            }
        }
        return 100000.0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Mechanic getMechanic() { return mechanic; }
    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
        this.totalCost = calculateTotal();
    }

    public Bicycle getBicycle() { return bicycle; }
    public void setBicycle(Bicycle bicycle) { this.bicycle = bicycle; }

    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }

    public LocalTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalTime entryTime) { this.entryTime = entryTime; }

    public String getServiceReason() { return serviceReason; }
    public void setServiceReason(String serviceReason) { this.serviceReason = serviceReason; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getWorkDone() { return workDone; }
    public void setWorkDone(String workDone) { this.workDone = workDone; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
}