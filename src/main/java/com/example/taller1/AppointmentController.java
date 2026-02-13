package com.example.parcial3;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;

public class AppointmentController {
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Client> cmbClient;
    @FXML private ComboBox<Mechanic> cmbMechanic;
    @FXML private TextField txtTotal;
    @FXML private TableView<Appointment> tableBills;
    @FXML private TableColumn<Appointment, String> colId;
    @FXML private TableColumn<Appointment, String> colClient;
    @FXML private TableColumn<Appointment, String> colMechanic;
    @FXML private TableColumn<Appointment, String> colTotal;
    @FXML private TableColumn<Appointment, String> colDate;
    @FXML private Button btnRegister;
    @FXML private Button btnClean;

    private ClientRepository clientRepository;
    private MechanicRepository mechanicRepository;
    private AppointmentRepository appointmentRepository;
    private ObservableList<Appointment> listAppointments;
    private DashboardController dashboardController;

    @FXML
    public void initialize() {
        clientRepository = ClientRepository.getInstance();
        mechanicRepository = MechanicRepository.getInstance();
        appointmentRepository = AppointmentRepository.getInstance();

        datePicker.setValue(LocalDate.now());


        cmbClient.setItems(FXCollections.observableArrayList(clientRepository.getClients()));
        cmbMechanic.setItems(FXCollections.observableArrayList(mechanicRepository.getMechanics()));

        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));
        colClient.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClient().getName()));
        colMechanic.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMechanic().getName()));
        colTotal.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.format("$%,.0f", cellData.getValue().getTotal())));
        colDate.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDate().toString()));

        loadBills();
        configureEvents();
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    private void loadBills() {
        listAppointments = FXCollections.observableArrayList(appointmentRepository.getBills());
        tableBills.setItems(listAppointments);
    }

    private void configureEvents() {

        cmbMechanic.setOnAction(event -> {
            calculateTotal();
        });

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            calculateTotal();
        });
    }

    private void calculateTotal() {
        Mechanic mechanic = cmbMechanic.getValue();
        if (mechanic != null) {

            double basePrice = 100000.0;


            switch (mechanic.getSpecialty()) {
                case "Cardiología":
                    basePrice = 150000.0;
                    break;
                case "Neurología":
                    basePrice = 180000.0;
                    break;
                case "Pediatría":
                    basePrice = 120000.0;
                    break;
                default:
                    basePrice = 100000.0;
            }

            txtTotal.setText(String.format("$%,.0f", basePrice));
        } else {
            txtTotal.clear();
        }
    }

    @FXML
    private void onRegister() {
        if (!validFields()) return;

        try {
            Client client = cmbClient.getValue();
            Mechanic mechanic = cmbMechanic.getValue();
            LocalDate date = datePicker.getValue();

            String billId = appointmentRepository.generateNewId();
            Appointment newAppointment = new Appointment(billId, client, mechanic, date);
            appointmentRepository.addBill(newAppointment);

            showAlert("Éxito", "Factura registrada correctamente\nTotal: " + txtTotal.getText(), Alert.AlertType.INFORMATION);
            cleanForm();
            loadBills();

        } catch (Exception e) {
            showAlert("Error", "Error al registrar factura: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onClean() {
        cleanForm();
    }

    private void cleanForm() {
        cmbClient.setValue(null);
        cmbMechanic.setValue(null);
        datePicker.setValue(LocalDate.now());
        txtTotal.clear();
    }

    private boolean validFields() {
        if (cmbClient.getValue() == null) {
            showAlert("Error", "Seleccione un paciente", Alert.AlertType.WARNING);
            cmbClient.requestFocus();
            return false;
        }
        if (cmbMechanic.getValue() == null) {
            showAlert("Error", "Seleccione un doctor", Alert.AlertType.WARNING);
            cmbMechanic.requestFocus();
            return false;
        }
        if (datePicker.getValue() == null) {
            showAlert("Error", "Seleccione una fecha", Alert.AlertType.WARNING);
            datePicker.requestFocus();
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