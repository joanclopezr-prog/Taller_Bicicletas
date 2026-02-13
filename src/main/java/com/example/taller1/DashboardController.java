package com.example.taller1;

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
    private Button btnMechanics;
    @FXML
    private Button btnAppointments;

    @FXML
    protected void onClientsClick() {
        loadClients();
    }

    @FXML
    protected void onMechanicsClick() {
        loadMechanics();
    }

    @FXML
    protected void onAppointmentsClick() {
        loadAppointments();
    }

    public void loadClients() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("client.fxml"));
            Parent clients = loader.load();

            ClientController controller = loader.getController();
            controller.setDashboardController(this);

            mainContent.getChildren().clear();
            mainContent.getChildren().add(clients);

        } catch (IOException e) {
            showAlert("Error", "No se pudo cargar el módulo de Clientes", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void loadMechanics() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mechanic.fxml"));
            Parent mechanics = loader.load();


            MechanicController controller = loader.getController();
            controller.setDashboardController(this);

            mainContent.getChildren().clear();
            mainContent.getChildren().add(mechanics);

        } catch (IOException e) {
            showAlert("Error", "No se pudo cargar el módulo de Mecánicos", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void loadAppointments() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("appointment.fxml"));
            Parent appointments = loader.load();

            AppointmentController controller = loader.getController();
            controller.setDashboardController(this);

            mainContent.getChildren().clear();
            mainContent.getChildren().add(appointments);

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