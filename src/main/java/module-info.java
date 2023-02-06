module com.example.batallanaval {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;




    opens com.example.batallanaval to javafx.fxml;
    exports com.example.batallanaval;
}