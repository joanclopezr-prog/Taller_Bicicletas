package com.example.parcial3;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MechanicController {
    @FXML private TextField txtDocument;
    @FXML private TextField txtName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtAge;
    @FXML private TextField txtSpecialty;
    @FXML private TableView<Mechanic> tableMechanics;
    @FXML private TableColumn<Mechanic, String> colDocument;
    @FXML private TableColumn<Mechanic, String> colName;
    @FXML private TableColumn<Mechanic, String> colPhone;
    @FXML private TableColumn<Mechanic, String> colEmail;
    @FXML private TableColumn<Mechanic, String> colAge;
    @FXML private TableColumn<Mechanic, String> colSpecialty;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClean;

    private MechanicRepository mechanicRepository;
    private ObservableList<Mechanic> listMechanics;
    private DashboardController dashboardController;
    private Mechanic mechanicSelect;

    @FXML
    public void initialize() {
        mechanicRepository = MechanicRepository.getInstance();


        colDocument.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDocument()));
        colName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colPhone.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPhone()));
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        colAge.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getAge())));
        colSpecialty.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSpecialty()));

        loadMechanics();
        configureSelectTable();
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    private void loadMechanics() {
        listMechanics = FXCollections.observableArrayList(mechanicRepository.getMechanics());
        tableMechanics.setItems(listMechanics);
    }

    private void configureSelectTable() {
        tableMechanics.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectMechanic(newValue);
                    }
                });
    }

    private void selectMechanic(Mechanic mechanic) {
        mechanicSelect = mechanic;
        txtDocument.setText(mechanic.getDocument());
        txtName.setText(mechanic.getName());
        txtPhone.setText(mechanic.getPhone());
        txtEmail.setText(mechanic.getEmail());
        txtAge.setText(String.valueOf(mechanic.getAge()));
        txtSpecialty.setText(mechanic.getSpecialty());

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

            if (mechanicRepository.searchMechanicPerDocument(document) != null) {
                showAlert("Error", "Ya existe un doctor con este documento", Alert.AlertType.ERROR);
                return;
            }

            Mechanic newMechanic = new Mechanic(document, name, phone, email, age, specialty);
            mechanicRepository.addMechanic(newMechanic);

            showAlert("Éxito", "Mechanic registrado correctamente", Alert.AlertType.INFORMATION);
            cleanForm();
            loadMechanics();

        } catch (NumberFormatException e) {
            showAlert("Error", "La edad debe ser un número válido", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Error al registrar doctor: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onUpdate() {
        if (mechanicSelect == null || !validFields()) return;

        try {
            String document = txtDocument.getText().trim();
            String name = txtName.getText().trim();
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();
            int age = Integer.parseInt(txtAge.getText().trim());
            String specialty = txtSpecialty.getText().trim();

            Mechanic mechanicUpdate = new Mechanic(document, name, phone, email, age, specialty);
            mechanicRepository.updateMechanic(mechanicUpdate);

            showAlert("Éxito", "Mechanic actualizado correctamente", Alert.AlertType.INFORMATION);
            cleanForm();
            loadMechanics();

        } catch (NumberFormatException e) {
            showAlert("Error", "La edad debe ser un número válido", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Error al actualizar doctor: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onDelete() {
        if (mechanicSelect == null) return;

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmar eliminación");
        confirmation.setHeaderText("¿Está seguro de eliminar este doctor?");
        confirmation.setContentText("Mechanic: " + mechanicSelect.getName() + " - " + mechanicSelect.getDocument());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                mechanicRepository.deleteMechanic(mechanicSelect.getDocument());
                showAlert("Éxito", "Mechanic eliminado correctamente", Alert.AlertType.INFORMATION);
                cleanForm();
                loadMechanics();
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
        mechanicSelect = null;
        tableMechanics.getSelectionModel().clearSelection();

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