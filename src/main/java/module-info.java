module com.sacms.sacmsood {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;

    opens com.sacms.sacmsood to javafx.fxml;
    exports com.sacms.sacmsood;

    requires org.kordamp.ikonli.antdesignicons;
    requires org.kordamp.ikonli.fontawesome5;
    requires java.sql;
    requires mysql.connector.j;
    requires jasperreports;

    exports com.sacms.sacmsood.Controllers;
    opens com.sacms.sacmsood.Controllers to javafx.fxml;
    exports com.sacms.sacmsood.Models;
    opens com.sacms.sacmsood.Models to javafx.fxml;
}