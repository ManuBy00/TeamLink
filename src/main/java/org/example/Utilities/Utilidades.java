package org.example.Utilities;

import javafx.scene.control.ButtonType;
import org.example.Exceptions.DatoNoValido;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.util.Optional;

public class Utilidades {


    /**
     * Válida que el campo fecha no esté vacío
     * @param fecha
     * @return fecha validada
     */
    public static LocalDate validarFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new DatoNoValido("La fecha introducida no es válida.");
        }
        return fecha;
    }

    /**
     * valida el correo con una regex
     * @param correo
     * @return true si el formato es correcto
     */
    public static boolean validarCorreo(String correo){
        String regex = "[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";
        return correo.matches(regex);
    }

    /**
     * Muestra una ventana de alerta para mostrar información
     * @param titulo
     * @param mensaje
     */
    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra una ventana de confirmación y espera la respuesta del usuario.
     *
     * @param titulo El texto del encabezado de la alerta (ej. "Confirmar Eliminación").
     * @param mensaje El mensaje que se muestra al usuario (ej. "¿Está seguro?").
     * @return true si el usuario hace clic en Aceptar (OK), false si hace clic en Cancelar.
     */
    public static boolean confirmarAccion(String titulo, String mensaje) {
        // 1. Crear el objeto Alert de tipo CONFIRMATION
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        // 2. Configurar el texto del diálogo
        alert.setTitle("Confirmación");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);

        // 3. Mostrar el diálogo y esperar la respuesta
        Optional<ButtonType> result = alert.showAndWait();

        // 4. Devolver true si la respuesta es OK
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}


