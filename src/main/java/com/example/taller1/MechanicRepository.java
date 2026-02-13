package com.example.parcial3;

import java.util.ArrayList;
import java.util.List;

public class MechanicRepository {
    private static MechanicRepository instance;
    private List<Mechanic> mechanics;

    private MechanicRepository() {
        mechanics = new ArrayList<>();

        mechanics.add(new Mechanic("12345678", "Dr. Carlos Martínez", "3001234567", "carlos.martinez@hospital.com", 45, "Cardiología"));
        mechanics.add(new Mechanic("87654321", "Dra. Ana López", "3007654321", "ana.lopez@hospital.com", 38, "Pediatría"));
        mechanics.add(new Mechanic("11223344", "Dr. Roberto García", "3001122334", "roberto.garcia@hospital.com", 52, "Neurología"));
    }

    public static MechanicRepository getInstance() {
        if (instance == null) {
            instance = new MechanicRepository();
        }
        return instance;
    }

    public List<Mechanic> getMechanics() {
        return new ArrayList<>(mechanics);
    }

    public void addMechanic(Mechanic mechanic) {
        mechanics.add(mechanic);
    }

    public void updateMechanic(Mechanic mechanicUpdate) {
        for (int i = 0; i < mechanics.size(); i++) {
            if (mechanics.get(i).getDocument().equals(mechanicUpdate.getDocument())) {
                mechanics.set(i, mechanicUpdate);
                break;
            }
        }
    }

    public void deleteMechanic(String document) {
        mechanics.removeIf(mechanic -> mechanic.getDocument().equals(document));
    }

    public Mechanic searchMechanicPerDocument(String document) {
        return mechanics.stream()
                .filter(mechanic -> mechanic.getDocument().equals(document))
                .findFirst()
                .orElse(null);
    }
}