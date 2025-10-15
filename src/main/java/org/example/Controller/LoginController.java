package org.example.Controller;

import org.example.Exceptions.DatoNoValido;
import org.example.Model.Usuario;
import org.example.Utilities.Utilidades;

import java.util.ArrayList;

public class LoginController {

    public void registrarUsuario(Usuario usuarioNuevo) throws DatoNoValido {

        if (!Utilidades.validarCorreo(usuarioNuevo.getEmail())){
            throw new DatoNoValido("Email no válido.");
        }



    }
}
