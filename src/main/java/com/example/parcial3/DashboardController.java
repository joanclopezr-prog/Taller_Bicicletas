package com.example.parcial3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class DashboardController {
    @FXML
    private VBox menu;
    @FXML
    private AnchorPane mainContent;
    @FXML
    private Button btnClients;
    @FXML
    private Button btnProducts;
    @FXML
    private Button btnSales;

    @FXML
    protected void onClientsClick() {
        loadClients();
    }

    @FXML
    protected void onProductsClick() {
        loadProducts();
    }

    @FXML
    protected void onSalesClick() {
        loadSales();
    }

    public void loadClients() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("patient.fxml"));
            Parent clientes = loader.load();

            PatientController controller = loader.getController();
            controller.setDashboardController(this);

            mainContent.getChildren().clear();
            mainContent.getChildren().add(clientes);

        } catch (IOException e) {
            showAlert("Error", "No se pudo cargar el módulo de pacientes", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void loadProducts() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor.fxml"));
            Parent productos = loader.load();

            DoctorController controller = loader.getController();
            controller.setDashboardController(this);

            mainContent.getChildren().clear();
            mainContent.getChildren().add(productos);

        } catch (IOException e) {
            showAlert("Error", "No se pudo cargar el módulo de doctores", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void loadSales() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("appointment.fxml"));
            Parent sales = loader.load();

            AppointmentController controller = loader.getController();
            controller.setDashboardController(this);

            mainContent.getChildren().clear();
            mainContent.getChildren().add(sales);

        } catch (IOException e) {
            showAlert("Error", "No se pudo cargar el módulo de citas", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public AnchorPane getMainContent() {
        return mainContent;
    }

    public void setMainContent(AnchorPane mainContent) {
        this.mainContent = mainContent;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}