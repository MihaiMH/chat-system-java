module chatSystem {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;
  requires com.google.gson;

  opens chatSystem.client to javafx.fxml;
  opens chatSystem.client.view to javafx.fxml;
  opens chatSystem.client.model to com.google.gson;
  opens chatSystem.client.communication to com.google.gson;
  opens chatSystem.server to com.google.gson;
  exports chatSystem.client;
  exports chatSystem.server;

}