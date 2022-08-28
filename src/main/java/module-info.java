module com.projektgruppe11.tv2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;
    requires java.desktop;
    requires org.json;
    requires org.postgresql.jdbc;
    requires commons.collections;

    opens com.projektgruppe11.tv2.Presentation to javafx.fxml;
    exports com.projektgruppe11.tv2.Presentation;
}