module com.example.vomegleclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.vomegleclient to javafx.fxml;
    exports com.example.vomegleclient;
}