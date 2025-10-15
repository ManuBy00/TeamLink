package org.example.Utilities;

import org.example.Exceptions.DatoNoValido;
import javafx.scene.control.Alert;

import java.time.LocalDate;

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


}
