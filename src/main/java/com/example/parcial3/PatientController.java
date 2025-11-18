package com.example.parcial3;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PatientController {
    @FXML private TextField txtDocument;
    @FXML private TextField txtName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtAge;
    @FXML private TextField txtEps;
    @FXML private TableView<Patient> tableClients;
    @FXML private TableColumn<Patient, String> colDocument;
    @FXML private TableColumn<Patient, String> colName;
    @FXML private TableColumn<Patient, String> colPhone;
    @FXML private TableColumn<Patient, String> colEmail;
    @FXML private TableColumn<Patient, String> colAge;
    @FXML private TableColumn<Patient, String> colEps;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClean;

    private PatientRepository patientRepository;
    private ObservableList<Patient> listPatients;
    private DashboardController dashboardController;
    private Patient patientSelect;

    @FXML
    public void initialize() {
        patientRepository = PatientRepository.getInstance();

        colDocument.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDocument()));
        colName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colPhone.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPhone()));
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        colAge.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getAge())));
        colEps.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEps()));

        loadClients();
        configureSelectTable();
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    private void loadClients() {
        listPatients = FXCollections.observableArrayList(patientRepository.getPatients());
        tableClients.setItems(listPatients);
    }

    private void configureSelectTable() {
        tableClients.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectCliente(newValue);
                    }
                });
    }

    private void selectCliente(Patient patient) {
        patientSelect = patient;
        txtDocument.setText(patient.getDocument());
        txtName.setText(patient.getName());
        txtPhone.setText(patient.getPhone());
        txtEmail.setText(patient.getEmail());
        txtAge.setText(String.valueOf(patient.getAge()));
        txtEps.setText(patient.getEps());

        btnSave.setDisable(true);
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
    }

    @FXML
    private void onSave() {
        if (!validFields()) return;

        try {
            String document = txtDocument.getText().trim();
            String name = txtName.getText().trim();
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();
            int age = Integer.parseInt(txtAge.getText().trim());
            String eps = txtEps.getText().trim();

            if (patientRepository.searchPatientPerDocument(document) != null) {
                showAlert("Error", "Ya existe un paciente con este documento", Alert.AlertType.ERROR);
                return;
            }

            Patient newPatient = new Patient(document, name, phone, email, age, eps);
            patientRepository.addPatient(newPatient);

            showAlert("Éxito", "Paciente registrado correctamente", Alert.AlertType.INFORMATION);
            cleanForm();
            loadClients();

        } catch (NumberFormatException e) {
            showAlert("Error", "La edad debe ser un número válido", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Error al registrar paciente: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onUpdate() {
        if (patientSelect == null || !validFields()) return;

        try {
            String document = txtDocument.getText().trim();
            String name = txtName.getText().trim();
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();
            int age = Integer.parseInt(txtAge.getText().trim());
            String eps = txtEps.getText().trim();

            Patient patientUpdate = new Patient(document, name, phone, email, age, eps);
            patientRepository.updatePatient(patientUpdate);

            showAlert("Éxito", "Paciente actualizado correctamente", Alert.AlertType.INFORMATION);
            cleanForm();
            loadClients();

        } catch (NumberFormatException e) {
            showAlert("Error", "La edad debe ser un número válido", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Error al actualizar paciente: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onDelete() {
        if (patientSelect == null) return;

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmar eliminación");
        confirmation.setHeaderText("¿Está seguro de eliminar este paciente?");
        confirmation.setContentText("Paciente: " + patientSelect.getName() + " - " + patientSelect.getDocument());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                patientRepository.deletePatient(patientSelect.getDocument());
                showAlert("Éxito", "Paciente eliminado correctamente", Alert.AlertType.INFORMATION);
                cleanForm();
                loadClients();
            }
        });
    }

    @FXML
    private void onClean() {
        cleanForm();
    }

    private void cleanForm() {
        txtDocument.clear();
        txtName.clear();
        txtPhone.clear();
        txtEmail.clear();
        txtAge.clear();
        txtEps.clear();
        patientSelect = null;
        tableClients.getSelectionModel().clearSelection();

        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }

    private boolean validFields() {
        if (txtDocument.getText().trim().isEmpty()) {
            showAlert("Error", "El documento es obligatorio", Alert.AlertType.WARNING);
            txtDocument.requestFocus();
            return false;
        }
        if (txtName.getText().trim().isEmpty()) {
            showAlert("Error", "El nombre es obligatorio", Alert.AlertType.WARNING);
            txtName.requestFocus();
            return false;
        }
        if (txtAge.getText().trim().isEmpty()) {
            showAlert("Error", "La edad es obligatoria", Alert.AlertType.WARNING);
            txtAge.requestFocus();
            return false;
        }
        try {
            Integer.parseInt(txtAge.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Error", "La edad debe ser un número válido", Alert.AlertType.WARNING);
            txtAge.requestFocus();
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