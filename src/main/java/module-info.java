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
    exports com.museum.client.exhibitions;
    opens com.museum.client.exhibitions to javafx.fxml;
    exports com.museum.client.exhibits;
    opens com.museum.client.exhibits to javafx.fxml;
    exports com.museum.utils;
    opens com.museum.utils to javafx.fxml;
    exports com.museum.client.overview;
    opens com.museum.client.overview to javafx.fxml;
    exports com.museum.client.tours;
    opens com.museum.client.tours to javafx.fxml;
    exports com.museum.client.workers;
    opens com.museum.client.workers to javafx.fxml;
}