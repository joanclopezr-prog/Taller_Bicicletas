package com.example.taller1;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class AppointmentRepository {
    private static AppointmentRepository instance;
    private List<Appointment> appointments;
    private int counterId = 1;

    private AppointmentRepository() {
        appointments = new ArrayList<>();

        ClientRepository clientRepo = ClientRepository.getInstance();
        MechanicRepository mechanicRepo = MechanicRepository.getInstance();

        if (!clientRepo.getClients().isEmpty() && !mechanicRepo.getMechanics().isEmpty()) {
            appointments.add(new Appointment("F001", clientRepo.getClients().get(0), mechanicRepo.getMechanics().get(0), LocalDate.now().minusDays(2)));
            appointments.add(new Appointment("F002", clientRepo.getClients().get(1), mechanicRepo.getMechanics().get(1), LocalDate.now().minusDays(1)));
        }
    }

    public static AppointmentRepository getInstance() {
        if (instance == null) {
            instance = new AppointmentRepository();
        }
        return instance;
    }

    public List<Appointment> getBills() {
        return new ArrayList<>(appointments);
    }

    public void addBill(Appointment appointment) {
        appointments.add(appointment);
        counterId++;
    }

    public String generateNewId() {
        return "F" + String.format("%03d", counterId);
    }
}