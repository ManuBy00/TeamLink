module org.example.teamlink {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires jbcrypt;
    requires java.xml.bind;
    requires java.base;
    requires java.desktop; // Buena práctica para asegurar acceso a módulos básicos de Java

    // JavaFX necesita acceso a controladores y vistas
    opens org.example.teamlink to javafx.fxml;
    opens org.example.Controller to javafx.fxml;

    // JAXB necesita acceso total a estos paquetes (Runtime Reflection)
    opens org.example.CRUD;
    opens org.example.Model;

    // Exportar lo necesario (Acceso en tiempo de compilación/ejecución normal)

    // 1. Exporta el paquete principal de la aplicación
    exports org.example;

    // 2. Exporta el paquete de Controladores
    exports org.example.Controller;

    // 3. ¡AÑADIDO CRUCIAL! Exporta el paquete MODEL para que otras clases puedan acceder a ChatGrupal, Chat, Usuario, etc.
    exports org.example.Model;

    // 4. ¡AÑADIDO CRUCIAL! Exporta el paquete CRUD para que otras clases puedan acceder a Managers y excepciones.
    exports org.example.CRUD;

    opens org.example.DataAccess;
}
