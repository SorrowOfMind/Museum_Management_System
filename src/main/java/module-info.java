module com.museum.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.apache.commons.lang3;
    requires java.desktop;

    opens com.museum.client to javafx.fxml;
    exports com.museum.client;
    opens com.museum.models to javafx.base;
    exports com.museum.client.Exhibitions;
    opens com.museum.client.Exhibitions to javafx.fxml;
    exports com.museum.client.exhibits;
    opens com.museum.client.exhibits to javafx.fxml;
    exports com.museum.Utils;
    opens com.museum.Utils to javafx.fxml;

    exports com.museum.client.Tours;
    opens com.museum.client.Tours to javafx.fxml;
}