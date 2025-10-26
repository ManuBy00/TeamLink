package org.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import org.example.Model.Empleado;
import org.example.Model.Empresa;
import org.example.Model.Usuario;
import org.example.Utilities.Utilidades;
import org.example.CRUD.UsuariosManager; // Necesario para buscar la empresa

public class UserProfileController {

    @FXML private Label tituloLabel;
    @FXML private ImageView profileImageView;
    @FXML private GridPane infoGrid;
    @FXML private Circle clipCircle; // Nuevo campo para el clip

    private int rowIndex = 0; // Fila donde empieza la información


    /**
     * Método principal para cargar la información del usuario en la vista.
     */
    public void setUsuario(Usuario usuario) {
        if (usuario == null) {
            Utilidades.mostrarAlerta("Error", "No se proporcionó información de usuario.");
            return;
        }

        // Limpiamos el grid y reseteamos el índice para la carga dinámica
        infoGrid.getChildren().clear();
        rowIndex = 0;

        // 1. Mostrar información común a todos (Usuario)
        tituloLabel.setText("Perfil de " + usuario.getNombre());
        addGridRow("Nombre:", usuario.getNombre());
        addGridRow("Email:", usuario.getEmail());

        // 2. Mostrar información específica
        if (usuario instanceof Empleado empleado) {
            mostrarInfoEmpleado(empleado);
        } else if (usuario instanceof Empresa empresa) {
            mostrarInfoEmpresa(empresa);
        }
    }

    private void mostrarInfoEmpleado(Empleado empleado) {
        // Atributos de Empleado
        addGridRow("Puesto:", empleado.getPuesto());
        addGridRow("Departamento:", empleado.getDepartamento());
        addGridRow("Es Mánager:", empleado.getManager() ? "Sí" : "No");

        // La empresa empleadora se busca para mostrar un nombre más legible
        Usuario empresa = UsuariosManager.getInstance().buscarUsuario(empleado.getEmpresa());
        String nombreEmpresa = (empresa != null) ? empresa.getNombre() : empleado.getEmpresa();
        addGridRow("Empresa:", nombreEmpresa);
    }

    private void mostrarInfoEmpresa(Empresa empresa) {
        // Atributos de Empresa
        addGridRow("Dirección:", empresa.getDireccion());
        addGridRow("Teléfono:", empresa.getTelefono());
        addGridRow("Sector:", empresa.getSector());
        addGridRow("Total Empleados:", String.valueOf(empresa.getEmpleados().size()));
        addGridRow("Descripción:", empresa.getDescripcion());
    }

    /**
     * Método auxiliar para añadir filas al GridPane de forma dinámica.
     */
    private void addGridRow(String labelText, String valueText) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-weight: bold;");
        GridPane.setConstraints(label, 0, rowIndex);

        Label value = new Label(valueText);
        value.setWrapText(true);
        GridPane.setConstraints(value, 1, rowIndex);

        infoGrid.getChildren().addAll(label, value);
        rowIndex++; // Avanzar a la siguiente fila para el próximo atributo
    }
}