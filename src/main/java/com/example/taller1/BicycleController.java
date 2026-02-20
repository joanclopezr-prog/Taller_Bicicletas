package com.example.taller1;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;

public class BicycleController {
    @FXML private TextField txtSerialNumber;
    @FXML private TextField txtBrand;
    @FXML private ComboBox<String> cmbType;
    @FXML private TextField txtColor;
    @FXML private TextField txtYear;
    @FXML private ComboBox<Client> cmbOwner;
    @FXML private TableView<Bicycle> tableBicycles;
    @FXML private TableColumn<Bicycle, String> colSerialNumber;
    @FXML private TableColumn<Bicycle, String> colBrand;
    @FXML private TableColumn<Bicycle, String> colType;
    @FXML private TableColumn<Bicycle, String> colColor;
    @FXML private TableColumn<Bicycle, String> colYear;
    @FXML private TableColumn<Bicycle, String> colOwner;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClean;

    private BicycleRepository bicycleRepository;
    private ClientRepository clientRepository;
    private ObservableList<Bicycle> listBicycles;
    private DashboardController dashboardController;
    private Bicycle bicycleSelect;

    @FXML
    public void initialize() {
        bicycleRepository = BicycleRepository.getInstance();
        clientRepository = ClientRepository.getInstance();

        cmbType.setItems(FXCollections.observableArrayList(
                "Ruta", "MTB", "Urbana", "Eléctrica", "Híbrida", "Ciclocross", "BMX", "Gravel"
        ));

        cmbOwner.setItems(FXCollections.observableArrayList(clientRepository.getClients()));
        cmbOwner.setCellFactory(lv -> new ListCell<Client>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                setText(empty || client == null ? null : client.getName() + " (" + client.getDocument() + ")");
            }
        });
        cmbOwner.setButtonCell(new ListCell<Client>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                setText(empty || client == null ? null : client.getName() + " (" + client.getDocument() + ")");
            }
        });

        colSerialNumber.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSerialNumber()));
        colBrand.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBrand()));
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        colColor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getColor()));
        colYear.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getYear())));
        colOwner.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOwnerName()));

        loadBicycles();
        configureSelectTable();
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    private void loadBicycles() {
        listBicycles = FXCollections.observableArrayList(bicycleRepository.getBicycles());
        tableBicycles.setItems(listBicycles);
    }

    private void configureSelectTable() {
        tableBicycles.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectBicycle(newValue);
                    }
                });
    }

    private void selectBicycle(Bicycle bicycle) {
        bicycleSelect = bicycle;
        txtSerialNumber.setText(bicycle.getSerialNumber());
        txtBrand.setText(bicycle.getBrand());
        cmbType.setValue(bicycle.getType());
        txtColor.setText(bicycle.getColor());
        txtYear.setText(String.valueOf(bicycle.getYear()));
        cmbOwner.setValue(bicycle.getOwner());

        btnSave.setDisable(true);
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
    }

    @FXML
    private void onSave() {
        if (!validFields()) return;

        try {
            String serialNumber = txtSerialNumber.getText().trim();
            String brand = txtBrand.getText().trim();
            String type = cmbType.getValue();
            String color = txtColor.getText().trim();
            int year = Integer.parseInt(txtYear.getText().trim());
            Client owner = cmbOwner.getValue();

            if (bicycleRepository.searchBicyclePerSerial(serialNumber) != null) {
                showAlert("Error", "Ya existe una bicicleta con este número de serie", Alert.AlertType.ERROR);
                return;
            }

            Bicycle newBicycle = new Bicycle(serialNumber, brand, type, color, year, owner);
            bicycleRepository.addBicycle(newBicycle);

            showAlert("Éxito", "Bicicleta registrada correctamente", Alert.AlertType.INFORMATION);
            cleanForm();
            loadBicycles();

        } catch (NumberFormatException e) {
            showAlert("Error", "El año debe ser un número válido", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Error al registrar bicicleta: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onUpdate() {
        if (bicycleSelect == null || !validFields()) return;

        try {
            String serialNumber = txtSerialNumber.getText().trim();
            String brand = txtBrand.getText().trim();
            String type = cmbType.getValue();
            String color = txtColor.getText().trim();
            int year = Integer.parseInt(txtYear.getText().trim());
            Client owner = cmbOwner.getValue();

            Bicycle bicycleUpdate = new Bicycle(serialNumber, brand, type, color, year, owner);
            bicycleRepository.updateBicycle(bicycleUpdate);

            showAlert("Éxito", "Bicicleta actualizada correctamente", Alert.AlertType.INFORMATION);
            cleanForm();
            loadBicycles();

        } catch (NumberFormatException e) {
            showAlert("Error", "El año debe ser un número válido", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Error al actualizar bicicleta: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onDelete() {
        if (bicycleSelect == null) return;

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmar eliminación");
        confirmation.setHeaderText("¿Está seguro de eliminar esta bicicleta?");
        confirmation.setContentText("Bicicleta: " + bicycleSelect.getBrand() + " - Serial: " + bicycleSelect.getSerialNumber());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                bicycleRepository.deleteBicycle(bicycleSelect.getSerialNumber());
                showAlert("Éxito", "Bicicleta eliminada correctamente", Alert.AlertType.INFORMATION);
                cleanForm();
                loadBicycles();
            }
        });
    }

    @FXML
    private void onClean() {
        cleanForm();
    }

    private void cleanForm() {
        txtSerialNumber.clear();
        txtBrand.clear();
        cmbType.setValue(null);
        txtColor.clear();
        txtYear.clear();
        cmbOwner.setValue(null);
        bicycleSelect = null;
        tableBicycles.getSelectionModel().clearSelection();

        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }

    private boolean validFields() {
        if (txtSerialNumber.getText().trim().isEmpty()) {
            showAlert("Error", "El número de serie es obligatorio", Alert.AlertType.WARNING);
            txtSerialNumber.requestFocus();
            return false;
        }
        if (txtBrand.getText().trim().isEmpty()) {
            showAlert("Error", "La marca es obligatoria", Alert.AlertType.WARNING);
            txtBrand.requestFocus();
            return false;
        }
        if (cmbType.getValue() == null) {
            showAlert("Error", "Seleccione un tipo de bicicleta", Alert.AlertType.WARNING);
            cmbType.requestFocus();
            return false;
        }
        if (txtColor.getText().trim().isEmpty()) {
            showAlert("Error", "El color es obligatorio", Alert.AlertType.WARNING);
            txtColor.requestFocus();
            return false;
        }
        if (txtYear.getText().trim().isEmpty()) {
            showAlert("Error", "El año es obligatorio", Alert.AlertType.WARNING);
            txtYear.requestFocus();
            return false;
        }
        if (cmbOwner.getValue() == null) {
            showAlert("Error", "Seleccione el propietario", Alert.AlertType.WARNING);
            cmbOwner.requestFocus();
            return false;
        }
        try {
            int year = Integer.parseInt(txtYear.getText().trim());
            if (year < 1900 || year > 2025) {
                showAlert("Error", "El año debe estar entre 1900 y 2025", Alert.AlertType.WARNING);
                txtYear.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "El año debe ser un número válido", Alert.AlertType.WARNING);
            txtYear.requestFocus();
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