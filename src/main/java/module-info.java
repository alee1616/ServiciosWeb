module com.example.serviciosweb {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.serviciosweb to javafx.fxml;
    exports com.example.serviciosweb;
}