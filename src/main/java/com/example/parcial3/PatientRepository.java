package com.example.parcial3;

import java.util.ArrayList;
import java.util.List;

public class PatientRepository {
    private static PatientRepository instance;
    private List<Patient> patients;

    private PatientRepository() {
        patients = new ArrayList<>();

        patients.add(new Patient("Medimas", "1090272139", "Alejandro Campo", "3224229356", "alejo@gmail.com", 20));
        patients.add(new Patient("SaludTotal", "108425322", "Danilo Ladino", "3224229358", "danilo@gmail.com", 20));
    }

    public static PatientRepository getInstance() {
        if (instance == null) {
            instance = new PatientRepository();
        }
        return instance;
    }

    public List<Patient> getPatients() {
        return new ArrayList<>(patients);
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void updatePatient(Patient patientActualizado) {
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getDocument().equals(patientActualizado.getDocument())) {
                patients.set(i, patientActualizado);
                break;
            }
        }
    }

    public void deletePatient(String document) {
        patients.removeIf(patient -> patient.getDocument().equals(document));
    }

    public Patient searchPatientPerDocument(String document) {
        return patients.stream()
                .filter(patient -> patient.getDocument().equals(document))
                .findFirst()
                .orElse(null);
    }
}