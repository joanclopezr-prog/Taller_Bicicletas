package com.example.taller1;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientController {
    @FXML private TextField txtDocument;
    @FXML private TextField txtName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtAge;
    @FXML private TableView<Client> tableClients;
    @FXML private TableColumn<Client, String> colDocument;
    @FXML private TableColumn<Client, String> colName;
    @FXML private TableColumn<Client, String> colPhone;
    @FXML private TableColumn<Client, String> colEmail;
    @FXML private TableColumn<Client, String> colAge;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClean;

    private ClientRepository clientRepository;
    private ObservableList<Client> listClients;
    private DashboardController dashboardController;
    private Client clientSelect;

    @FXML
    public void initialize() {
        clientRepository = ClientRepository.getInstance();

        colDocument.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDocument()));
        colName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colPhone.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPhone()));
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        colAge.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getAge())));

        loadClients();
        configureSelectTable();
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    private void loadClients() {
        listClients = FXCollections.observableArrayList(clientRepository.getClients());
        tableClients.setItems(listClients);
    }

    private void configureSelectTable() {
        tableClients.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectCliente(newValue);
                    }
                });
    }

    private void selectCliente(Client client) {
        clientSelect = client;
        txtDocument.setText(client.getDocument());
        txtName.setText(client.getName());
        txtPhone.setText(client.getPhone());
        txtEmail.setText(client.getEmail());
        txtAge.setText(String.valueOf(client.getAge()));


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


            if (clientRepository.searchClientPerDocument(document) != null) {
                showAlert("Error", "Ya existe un cliente con este documento", Alert.AlertType.ERROR);
                return;
            }

            Client newClient = new Client(document, name, phone, email, age);
            clientRepository.addClient(newClient);

            showAlert("Éxito", "Cliente registrado correctamente", Alert.AlertType.INFORMATION);
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
        if (clientSelect == null || !validFields()) return;

        try {
            String document = txtDocument.getText().trim();
            String name = txtName.getText().trim();
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();
            int age = Integer.parseInt(txtAge.getText().trim());

            Client clientUpdate = new Client(document, name, phone, email, age);
            clientRepository.updateClient(clientUpdate);

            showAlert("Éxito", "Cliente actualizado correctamente", Alert.AlertType.INFORMATION);
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
        if (clientSelect == null) return;

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmar eliminación");
        confirmation.setHeaderText("¿Está seguro de eliminar este Cliente?");
        confirmation.setContentText("Cliente: " + clientSelect.getName() + " - " + clientSelect.getDocument());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                clientRepository.deleteClient(clientSelect.getDocument());
                showAlert("Éxito", "Cliente eliminado correctamente", Alert.AlertType.INFORMATION);
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
        clientSelect = null;
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