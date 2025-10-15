package org.example.Controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.CRUD.UsuariosManager;
import org.example.DataAccess.XML;
import org.example.Exceptions.ElementoRepetido;
import org.example.Model.Empresa;
import org.example.Model.Usuario;
import org.example.Utilities.Utilidades;

public class FormularioEmpresas {

    @FXML
    private TextField nombreEmpresaField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField telefonoField;

    @FXML
    private TextField direccionField;

    @FXML
    private ComboBox<String> sectorField;

    @FXML
    private TextArea descripcionField;

    @FXML
    private Button registrarButton;

    @FXML
    public void initialize() {
        // Llenar comboBox con roles
    }


    public void registrarEmpresa(ActionEvent actionEvent) {
        String nombre = nombreEmpresaField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String telefono = telefonoField.getText();
        String direccion = direccionField.getText();
        String sector = sectorField.getValue();
        String descripcion = descripcionField.getText();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || sector.isEmpty() || descripcion.isEmpty()) {
            Utilidades.mostrarAlerta("Error", "Por favor, rellena todos los campos.");
            return;
        }

        if (!Utilidades.validarCorreo(email)){
            Utilidades.mostrarAlerta("Error", "Por favor, introduce un correo válido.");
            return;
        }

        Usuario nuevaEmpresa = new Empresa(email, nombre, password, direccion, telefono, descripcion, sector);

        UsuariosManager um = UsuariosManager.getInstance();

        try {
            um.add(nuevaEmpresa);
            XML.writeXML(um.getUsuariosList(), "Usuarios.XML");
        }catch (ElementoRepetido e){
            Utilidades.mostrarAlerta("Empresa ya registrada", "Ya existe una empresa registrada con ese nombre o correo");
        }
    }
}
