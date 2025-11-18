package com.example.parcial3;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class AppointmentRepository {
    private static AppointmentRepository instance;
    private List<Appointment> appointments;
    private int counterId = 1;

    private AppointmentRepository() {
        appointments = new ArrayList<>();

        PatientRepository patientRepo = PatientRepository.getInstance();
        DoctorRepository doctorRepo = DoctorRepository.getInstance();

        if (!patientRepo.getPatients().isEmpty() && !doctorRepo.getDoctors().isEmpty()) {
            appointments.add(new Appointment("F001", patientRepo.getPatients().get(0), doctorRepo.getDoctors().get(0), LocalDate.now().minusDays(2)));
            appointments.add(new Appointment("F002", patientRepo.getPatients().get(1), doctorRepo.getDoctors().get(1), LocalDate.now().minusDays(1)));
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