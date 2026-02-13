module com.example.taller1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.taller1 to javafx.fxml;
    exports com.example.taller1;
}