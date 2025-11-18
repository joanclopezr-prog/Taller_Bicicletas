package com.example.parcial3;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DoctorController {
    @FXML private TextField txtDocument;
    @FXML private TextField txtName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtAge;
    @FXML private TextField txtSpecialty;
    @FXML private TableView<Doctor> tableDoctors;
    @FXML private TableColumn<Doctor, String> colDocument;
    @FXML private TableColumn<Doctor, String> colName;
    @FXML private TableColumn<Doctor, String> colPhone;
    @FXML private TableColumn<Doctor, String> colEmail;
    @FXML private TableColumn<Doctor, String> colAge;
    @FXML private TableColumn<Doctor, String> colSpecialty;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClean;

    private DoctorRepository doctorRepository;
    private ObservableList<Doctor> listDoctors;
    private DashboardController dashboardController;
    private Doctor doctorSelect;

    @FXML
    public void initialize() {
        doctorRepository = DoctorRepository.getInstance();


        colDocument.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDocument()));
        colName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colPhone.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPhone()));
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        colAge.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getAge())));
        colSpecialty.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSpecialty()));

        loadDoctors();
        configureSelectTable();
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    private void loadDoctors() {
        listDoctors = FXCollections.observableArrayList(doctorRepository.getDoctors());
        tableDoctors.setItems(listDoctors);
    }

    private void configureSelectTable() {
        tableDoctors.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectDoctor(newValue);
                    }
                });
    }

    private void selectDoctor(Doctor doctor) {
        doctorSelect = doctor;
        txtDocument.setText(doctor.getDocument());
        txtName.setText(doctor.getName());
        txtPhone.setText(doctor.getPhone());
        txtEmail.setText(doctor.getEmail());
        txtAge.setText(String.valueOf(doctor.getAge()));
        txtSpecialty.setText(doctor.getSpecialty());

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
            String specialty = txtSpecialty.getText().trim();

            if (doctorRepository.searchDoctorPerDocument(document) != null) {
                showAlert("Error", "Ya existe un doctor con este documento", Alert.AlertType.ERROR);
                return;
            }

            Doctor newDoctor = new Doctor(document, name, phone, email, age, specialty);
            doctorRepository.addDoctor(newDoctor);

            showAlert("Éxito", "Doctor registrado correctamente", Alert.AlertType.INFORMATION);
            cleanForm();
            loadDoctors();

        } catch (NumberFormatException e) {
            showAlert("Error", "La edad debe ser un número válido", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Error al registrar doctor: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onUpdate() {
        if (doctorSelect == null || !validFields()) return;

        try {
            String document = txtDocument.getText().trim();
            String name = txtName.getText().trim();
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();
            int age = Integer.parseInt(txtAge.getText().trim());
            String specialty = txtSpecialty.getText().trim();

            Doctor doctorUpdate = new Doctor(document, name, phone, email, age, specialty);
            doctorRepository.updateDoctor(doctorUpdate);

            showAlert("Éxito", "Doctor actualizado correctamente", Alert.AlertType.INFORMATION);
            cleanForm();
            loadDoctors();

        } catch (NumberFormatException e) {
            showAlert("Error", "La edad debe ser un número válido", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Error al actualizar doctor: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onDelete() {
        if (doctorSelect == null) return;

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmar eliminación");
        confirmation.setHeaderText("¿Está seguro de eliminar este doctor?");
        confirmation.setContentText("Doctor: " + doctorSelect.getName() + " - " + doctorSelect.getDocument());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                doctorRepository.deleteDoctor(doctorSelect.getDocument());
                showAlert("Éxito", "Doctor eliminado correctamente", Alert.AlertType.INFORMATION);
                cleanForm();
                loadDoctors();
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
        txtSpecialty.clear();
        doctorSelect = null;
        tableDoctors.getSelectionModel().clearSelection();

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
        if (txtSpecialty.getText().trim().isEmpty()) {
            showAlert("Error", "La especialidad es obligatoria", Alert.AlertType.WARNING);
            txtSpecialty.requestFocus();
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