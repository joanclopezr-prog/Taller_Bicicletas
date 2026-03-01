package com.example.taller1;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentRepository {
    private static AppointmentRepository instance;
    private List<Appointment> appointments;
    private int counterId = 4;

    private AppointmentRepository() {
        appointments = new ArrayList<>();

        ClientRepository clientRepo = ClientRepository.getInstance();
        MechanicRepository mechanicRepo = MechanicRepository.getInstance();
        BicycleRepository bicycleRepo = BicycleRepository.getInstance();

        if (!clientRepo.getClients().isEmpty() &&
                !mechanicRepo.getMechanics().isEmpty() &&
                !bicycleRepo.getBicycles().isEmpty()) {

            List<Bicycle> bicycles = bicycleRepo.getBicycles();

            appointments.add(new Appointment("F001",
                    clientRepo.getClients().get(0),
                    mechanicRepo.getMechanics().get(0),
                    bicycles.get(0),
                    LocalDate.now().minusDays(2),
                    LocalTime.of(9, 30),
                    "Freno delantero no funciona",
                    "Pastillas de freno desgastadas",
                    "Cambio de pastillas de freno y ajuste"));

            appointments.add(new Appointment("F002",
                    clientRepo.getClients().get(1),
                    mechanicRepo.getMechanics().get(1),
                    bicycles.get(1),
                    LocalDate.now().minusDays(1),
                    LocalTime.of(14, 15),
                    "Cambios no responden",
                    "Cable de cambios estirado",
                    "Ajuste de tensión de cables"));
        }
    }

    public static AppointmentRepository getInstance() {
        if (instance == null) {
            instance = new AppointmentRepository();
        }
        return instance;
    }

    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments);
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        counterId++;
    }

    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
    }

    public Appointment findById(String id) {
        return appointments.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Appointment> findByDate(LocalDate date) {
        return appointments.stream()
                .filter(a -> a.getEntryDate().equals(date))
                .toList();
    }

    public List<Appointment> findByClient(String clientDocument) {
        return appointments.stream()
                .filter(a -> a.getClient().getDocument().equals(clientDocument))
                .toList();
    }

    public List<Appointment> findByBicycle(String serialNumber) {
        return appointments.stream()
                .filter(a -> a.getBicycle().getSerialNumber().equals(serialNumber))
                .toList();
    }

    public String generateNewId() {
        return "F" + String.format("%03d", counterId);
    }
}