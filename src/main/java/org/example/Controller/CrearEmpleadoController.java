package org.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.CRUD.UsuariosManager;
import org.example.Exceptions.ElementoRepetido;
import org.example.Model.Empleado;
import org.example.Model.Empresa;
import org.example.Utilities.Sesion;
import org.example.Utilities.Utilidades;

public class CrearEmpleadoController {

    @FXML
    private TextField emailField;
    @FXML
    private TextField nombreField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField departamentoField;
    @FXML
    private TextField puestoField;
    @FXML
    private CheckBox managerCheckBox;

    private Empresa empresaActual;

    @FXML
    public void initialize() {
        // Obtenemos la empresa que está logueada (asumimos que solo la Empresa puede crear empleados)
        if (Sesion.getInstance().getUsuarioIniciado() instanceof Empresa) {
            empresaActual = (Empresa) Sesion.getInstance().getUsuarioIniciado();
        } else {
            // Manejar caso donde un Empleado intenta abrir este formulario
            Utilidades.mostrarAlerta("Acceso Denegado", "Solo las cuentas de Empresa pueden registrar nuevos empleados.");
            // Cierra la ventana si el usuario no es una Empresa
            // ((Stage) emailField.getScene().getWindow()).close();
        }
    }

    @FXML
    private void handleRegistrarEmpleado(ActionEvent event) {
        if (empresaActual == null) {
            Utilidades.mostrarAlerta("Error de Sesión", "No se pudo obtener la información de la empresa logueada.");
            return;
        }

        // 1. Recoger datos
        String email = emailField.getText().trim();
        String nombre = nombreField.getText().trim();
        String password = passwordField.getText(); // Contraseña se hashea en el constructor de Empleado
        String departamento = departamentoField.getText().trim();
        String puesto = puestoField.getText().trim();
        Boolean manager = managerCheckBox.isSelected();

        // El nombre de la empresa se toma del usuario logueado
        String empresa = empresaActual.getEmail();

        // 2. Validación simple
        if (email.isEmpty() || nombre.isEmpty() || password.isEmpty() || departamento.isEmpty() || puesto.isEmpty()) {
            Utilidades.mostrarAlerta("Datos Incompletos", "Todos los campos son obligatorios.");
            return;
        }

        // 3. Crear y agregar el Empleado
        try {
            // Creamos la instancia del nuevo Empleado
            Empleado nuevoEmpleado = new Empleado(email, nombre, password, empresa, departamento, manager, puesto);

            // Agregamos al UsuariosManager
            UsuariosManager.getInstance().add(nuevoEmpleado);

            // Agregamos el empleado a la lista de la empresa (si Empresa tiene ese método)
            empresaActual.addEmpleado(nuevoEmpleado);

            // 4. Serializar (Guardar los cambios en el XML)
            // Llama aquí a tu método para escribir en el XML (ej. XML.writeXML(UsuariosManager.getInstance(), "Usuarios.XML");)

            Utilidades.mostrarAlerta("Éxito", "Empleado " + nombre + " registrado con éxito en " + nombreEmpresa + ".");

            // 5. Cerrar el formulario
            // ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

        } catch (ElementoRepetido e) {
            Utilidades.mostrarAlerta("Error de Registro", e.getMessage());
        } catch (Exception e) {
            Utilidades.mostrarAlerta("Error de Sistema", "Ocurrió un error al guardar los datos.");
        }
    }
}