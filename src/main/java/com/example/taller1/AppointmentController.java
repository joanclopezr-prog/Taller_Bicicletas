package com.example.taller1;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.StringConverter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentController {
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<LocalTime> cmbTime;
    @FXML private ComboBox<Client> cmbClient;
    @FXML private ComboBox<Bicycle> cmbBicycle;
    @FXML private ComboBox<Mechanic> cmbMechanic;
    @FXML private TextField txtServiceReason;
    @FXML private TextArea txtDiagnosis;
    @FXML private TextArea txtWorkDone;
    @FXML private TextField txtTotalCost;
    @FXML private TableView<Appointment> tableAppointments;

    @FXML private TableColumn<Appointment, String> colId;
    @FXML private TableColumn<Appointment, String> colClient;
    @FXML private TableColumn<Appointment, String> colBicycle;
    @FXML private TableColumn<Appointment, String> colMechanic;
    @FXML private TableColumn<Appointment, String> colDate;
    @FXML private TableColumn<Appointment, String> colTime;
    @FXML private TableColumn<Appointment, String> colServiceReason;
    @FXML private TableColumn<Appointment, String> colDiagnosis;
    @FXML private TableColumn<Appointment, String> colWorkDone;
    @FXML private TableColumn<Appointment, String> colTotalCost;

    @FXML private Button btnRegister;
    @FXML private Button btnClean;

    private ClientRepository clientRepository;
    private MechanicRepository mechanicRepository;
    private BicycleRepository bicycleRepository;
    private AppointmentRepository appointmentRepository;
    private ObservableList<Appointment> listAppointments;
    private ObservableList<LocalTime> timeSlots;

    private DashboardController dashboardController;

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }
    @FXML
    public void initialize() {
        clientRepository = ClientRepository.getInstance();
        mechanicRepository = MechanicRepository.getInstance();
        bicycleRepository = BicycleRepository.getInstance();
        appointmentRepository = AppointmentRepository.getInstance();

        datePicker.setValue(LocalDate.now());
        setupTimeSlots();

        cmbClient.setItems(FXCollections.observableArrayList(clientRepository.getClients()));
        cmbBicycle.setItems(FXCollections.observableArrayList(bicycleRepository.getBicycles()));
        cmbMechanic.setItems(FXCollections.observableArrayList(mechanicRepository.getMechanics()));
        cmbTime.setItems(timeSlots);

        setupComboBoxDisplay();
        setupTableColumns();
        loadAppointments();
        configureEvents();
    }

    private void setupTimeSlots() {
        timeSlots = FXCollections.observableArrayList();
        for (int hour = 8; hour <= 18; hour++) {
            timeSlots.add(LocalTime.of(hour, 0));
            if (hour != 18) {
                timeSlots.add(LocalTime.of(hour, 30));
            }
        }
    }

    private void setupComboBoxDisplay() {
        cmbClient.setCellFactory(lv -> new ListCell<Client>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                setText(empty || client == null ? null : client.getName() + " (" + client.getDocument() + ")");
            }
        });
        cmbClient.setButtonCell(new ListCell<Client>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                setText(empty || client == null ? null : client.getName() + " (" + client.getDocument() + ")");
            }
        });

        cmbBicycle.setCellFactory(lv -> new ListCell<Bicycle>() {
            @Override
            protected void updateItem(Bicycle bicycle, boolean empty) {
                super.updateItem(bicycle, empty);
                setText(empty || bicycle == null ? null :
                        bicycle.getBrand() + " " + bicycle.getType() + " (" + bicycle.getColor() + ")");
            }
        });
        cmbBicycle.setButtonCell(new ListCell<Bicycle>() {
            @Override
            protected void updateItem(Bicycle bicycle, boolean empty) {
                super.updateItem(bicycle, empty);
                setText(empty || bicycle == null ? null :
                        bicycle.getBrand() + " " + bicycle.getType() + " (" + bicycle.getColor() + ")");
            }
        });


        cmbTime.setConverter(new StringConverter<LocalTime>() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            @Override
            public String toString(LocalTime time) {
                return time == null ? null : formatter.format(time);
            }
            @Override
            public LocalTime fromString(String string) {
                return string == null ? null : LocalTime.parse(string, formatter);
            }
        });
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        colClient.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClient().getName()));
        colBicycle.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getBicycle().getBrand() + " " +
                        cellData.getValue().getBicycle().getType() + " (" +
                        cellData.getValue().getBicycle().getColor() + ")"
        ));
        colMechanic.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMechanic().getName()));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEntryDate().format(dateFormatter)));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        colTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEntryTime().format(timeFormatter)));

        colServiceReason.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getServiceReason()));
        colDiagnosis.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDiagnosis()));
        colWorkDone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWorkDone()));
        colTotalCost.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("$%,.0f", cellData.getValue().getTotalCost())));
    }

    private void loadAppointments() {
        listAppointments = FXCollections.observableArrayList(appointmentRepository.getAppointments());
        tableAppointments.setItems(listAppointments);
    }

    private void configureEvents() {
        cmbMechanic.setOnAction(event -> calculateTotal());


        cmbClient.setOnAction(event -> filterBicyclesByClient());

        tableAppointments.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadAppointmentToForm(newSelection);
            }
        });
    }

    private void filterBicyclesByClient() {
        Client selectedClient = cmbClient.getValue();
        if (selectedClient != null) {
            List<Bicycle> clientBicycles = bicycleRepository.getBicyclesByClient(selectedClient.getDocument());
            cmbBicycle.setItems(FXCollections.observableArrayList(clientBicycles));
        } else {
            cmbBicycle.setItems(FXCollections.observableArrayList(bicycleRepository.getBicycles()));
        }
        cmbBicycle.setValue(null);
    }

    private void calculateTotal() {
        Mechanic mechanic = cmbMechanic.getValue();
        double basePrice = 50000.0;

        if (mechanic != null) {
            switch (mechanic.getSpecialty()) {
                case "Frenos": basePrice = 150000.0; break;
                case "Transmisión": basePrice = 180000.0; break;
                case "Suspensión": basePrice = 120000.0; break;
                case "Eléctrica": basePrice = 120000.0; break;
                default: basePrice = 100000.0;
            }
        }

        String serviceReason = txtServiceReason.getText().toLowerCase();
        if (serviceReason.contains("completo") || serviceReason.contains("general")) {
            basePrice += 50000;
        } else if (serviceReason.contains("emergencia") || serviceReason.contains("urgente")) {
            basePrice += 30000;
        }

        txtTotalCost.setText(String.format("$%,.0f", basePrice));
    }

    private void loadAppointmentToForm(Appointment appointment) {
        cmbClient.setValue(appointment.getClient());
        filterBicyclesByClient();
        cmbBicycle.setValue(appointment.getBicycle());
        cmbMechanic.setValue(appointment.getMechanic());
        datePicker.setValue(appointment.getEntryDate());
        cmbTime.setValue(appointment.getEntryTime());
        txtServiceReason.setText(appointment.getServiceReason());
        txtDiagnosis.setText(appointment.getDiagnosis());
        txtWorkDone.setText(appointment.getWorkDone());
        txtTotalCost.setText(String.format("$%,.0f", appointment.getTotalCost()));
    }

    @FXML
    private void onRegister() {
        if (!validFields()) return;

        try {
            Client client = cmbClient.getValue();
            Bicycle bicycle = cmbBicycle.getValue();
            Mechanic mechanic = cmbMechanic.getValue();
            LocalDate date = datePicker.getValue();
            LocalTime time = cmbTime.getValue();
            String serviceReason = txtServiceReason.getText();
            String diagnosis = txtDiagnosis.getText();
            String workDone = txtWorkDone.getText();

            String appointmentId = appointmentRepository.generateNewId();
            Appointment newAppointment = new Appointment(
                    appointmentId, client, mechanic, bicycle,
                    date, time, serviceReason, diagnosis, workDone
            );

            appointmentRepository.addAppointment(newAppointment);

            showAlert("Éxito", "Servicio registrado correctamente\nTotal: " + txtTotalCost.getText(),
                    Alert.AlertType.INFORMATION);
            cleanForm();
            loadAppointments();

        } catch (Exception e) {
            showAlert("Error", "Error al registrar servicio: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onClean() {
        cleanForm();
    }

    private void cleanForm() {
        cmbClient.setValue(null);
        cmbBicycle.setItems(FXCollections.observableArrayList(bicycleRepository.getBicycles()));
        cmbBicycle.setValue(null);
        cmbMechanic.setValue(null);
        datePicker.setValue(LocalDate.now());
        cmbTime.setValue(null);
        txtServiceReason.clear();
        txtDiagnosis.clear();
        txtWorkDone.clear();
        txtTotalCost.clear();
        tableAppointments.getSelectionModel().clearSelection();
    }

    private boolean validFields() {
        if (cmbClient.getValue() == null) {
            showAlert("Error", "Seleccione un Cliente", Alert.AlertType.WARNING);
            cmbClient.requestFocus();
            return false;
        }
        if (cmbBicycle.getValue() == null) {
            showAlert("Error", "Seleccione una Bicicleta", Alert.AlertType.WARNING);
            cmbBicycle.requestFocus();
            return false;
        }
        if (cmbMechanic.getValue() == null) {
            showAlert("Error", "Seleccione un Mecánico", Alert.AlertType.WARNING);
            cmbMechanic.requestFocus();
            return false;
        }
        if (datePicker.getValue() == null) {
            showAlert("Error", "Seleccione una fecha", Alert.AlertType.WARNING);
            datePicker.requestFocus();
            return false;
        }
        if (cmbTime.getValue() == null) {
            showAlert("Error", "Seleccione una hora", Alert.AlertType.WARNING);
            cmbTime.requestFocus();
            return false;
        }
        if (txtServiceReason.getText().trim().isEmpty()) {
            showAlert("Error", "Ingrese el motivo del servicio", Alert.AlertType.WARNING);
            txtServiceReason.requestFocus();
            return false;
        }
        if (txtDiagnosis.getText().trim().isEmpty()) {
            showAlert("Error", "Ingrese el diagnóstico", Alert.AlertType.WARNING);
            txtDiagnosis.requestFocus();
            return false;
        }
        if (txtWorkDone.getText().trim().isEmpty()) {
            showAlert("Error", "Ingrese los trabajos realizados", Alert.AlertType.WARNING);
            txtWorkDone.requestFocus();
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}