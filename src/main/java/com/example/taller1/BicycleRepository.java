package com.example.taller1;

import java.util.ArrayList;
import java.util.List;

public class BicycleRepository {
    private static BicycleRepository instance;
    private List<Bicycle> bicycles;

    private BicycleRepository() {
        bicycles = new ArrayList<>();

        ClientRepository clientRepo = ClientRepository.getInstance();
        if (!clientRepo.getClients().isEmpty()) {
            bicycles.add(new Bicycle("SN123456", "Trek", "MTB", "Rojo", 2022, clientRepo.getClients().get(0)));
            bicycles.add(new Bicycle("SN789012", "Specialized", "Ruta", "Azul", 2023, clientRepo.getClients().get(1)));
        }
    }

    public static BicycleRepository getInstance() {
        if (instance == null) {
            instance = new BicycleRepository();
        }
        return instance;
    }

    public List<Bicycle> getBicycles() {
        return new ArrayList<>(bicycles);
    }

    public void addBicycle(Bicycle bicycle) {
        bicycles.add(bicycle);
    }

    public void updateBicycle(Bicycle bicycleUpdate) {
        for (int i = 0; i < bicycles.size(); i++) {
            if (bicycles.get(i).getSerialNumber().equals(bicycleUpdate.getSerialNumber())) {
                bicycles.set(i, bicycleUpdate);
                break;
            }
        }
    }

    public void deleteBicycle(String serialNumber) {
        bicycles.removeIf(bicycle -> bicycle.getSerialNumber().equals(serialNumber));
    }

    public Bicycle searchBicyclePerSerial(String serialNumber) {
        return bicycles.stream()
                .filter(bicycle -> bicycle.getSerialNumber().equals(serialNumber))
                .findFirst()
                .orElse(null);
    }

    public List<Bicycle> getBicyclesByClient(String clientDocument) {
        List<Bicycle> result = new ArrayList<>();
        for (Bicycle bicycle : bicycles) {
            if (bicycle.getOwner() != null && bicycle.getOwner().getDocument().equals(clientDocument)) {
                result.add(bicycle);
            }
        }
        return result;
    }
}
