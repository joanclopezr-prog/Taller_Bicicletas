package com.example.taller1;

import java.util.ArrayList;
import java.util.List;

public class MechanicRepository {
    private static MechanicRepository instance;
    private List<Mechanic> mechanics;

    private MechanicRepository() {
        mechanics = new ArrayList<>();

        mechanics.add(new Mechanic("12345678", "M. Carlos Martínez", "3001234567", "carlos.martinez@taller.com", 45, "Frenos"));
        mechanics.add(new Mechanic("87654321", "M. Ana López", "3007654321", "ana.lopez@taller.com", 38, "Transmisión"));
        mechanics.add(new Mechanic("11223344", "M. Roberto García", "3001122334", "roberto.garcia@taller.com", 52, "Eléctrica"));
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