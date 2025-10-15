module org.example.teamlink {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires jbcrypt;
    requires java.xml.bind;

    // JavaFX necesita acceso a controladores y vistas
    opens org.example.teamlink to javafx.fxml;
    opens org.example.Controller to javafx.fxml;

    // JAXB necesita acceso total a estos paquetes
    opens org.example.CRUD;   // para UsuariosManager y similares
    opens org.example.Model;  // para Usuario, Empresa, Empleado, etc.

    // Exportar lo necesario
    exports org.example;
    exports org.example.Controller;
}
