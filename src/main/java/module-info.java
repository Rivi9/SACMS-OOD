module com.sacms.sacmsood {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;

    opens com.sacms.sacmsood to javafx.fxml;
    exports com.sacms.sacmsood;
}