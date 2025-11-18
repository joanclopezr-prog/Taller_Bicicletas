package com.example.parcial3;

import java.util.ArrayList;
import java.util.List;

public class DoctorRepository {
    private static DoctorRepository instance;
    private List<Doctor> doctors;

    private DoctorRepository() {
        doctors = new ArrayList<>();

        doctors.add(new Doctor("12345678", "Dr. Carlos Martínez", "3001234567", "carlos.martinez@hospital.com", 45, "Cardiología"));
        doctors.add(new Doctor("87654321", "Dra. Ana López", "3007654321", "ana.lopez@hospital.com", 38, "Pediatría"));
        doctors.add(new Doctor("11223344", "Dr. Roberto García", "3001122334", "roberto.garcia@hospital.com", 52, "Neurología"));
    }

    public static DoctorRepository getInstance() {
        if (instance == null) {
            instance = new DoctorRepository();
        }
        return instance;
    }

    public List<Doctor> getDoctors() {
        return new ArrayList<>(doctors);
    }

    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    public void updateDoctor(Doctor doctorUpdate) {
        for (int i = 0; i < doctors.size(); i++) {
            if (doctors.get(i).getDocument().equals(doctorUpdate.getDocument())) {
                doctors.set(i, doctorUpdate);
                break;
            }
        }
    }

    public void deleteDoctor(String document) {
        doctors.removeIf(doctor -> doctor.getDocument().equals(document));
    }

    public Doctor searchDoctorPerDocument(String document) {
        return doctors.stream()
                .filter(doctor -> doctor.getDocument().equals(document))
                .findFirst()
                .orElse(null);
    }
}