package org.example;

import org.example.CRUD.UsuariosManager;
import org.example.DataAccess.XML;
import org.example.Exceptions.ElementoRepetido;
import org.example.Model.Empleado;
import org.example.Model.Empresa;
import org.example.Model.Usuario;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        Usuario empresa = new Empresa("pepe", "pepe", "pepe", "pepe", "pepe", "pepe", "pepe");

        Usuario empleado = new Empleado("Empleado@admin.com", "empleado1", "empleado", "admin@gmail.com", "Marketing", true, "juan");




        UsuariosManager userManager = XML.readXML(UsuariosManager.class, "Usuarios.XML");
        for (Usuario u : userManager.getUsuariosList()){
            System.out.println(u);
        }



       /*
        try {
            userManager.add(empresa);
        }catch (ElementoRepetido e){
            System.out.println(e.getMessage());
        }

        System.out.println(userManager.getUsuariosList().size());

        for (Usuario u : userManager.getUsuariosList()){
            System.out.println(u);
        }

       Usuario empresa2 = new Empresa("juan", "juan", "juan", "juan", "juan", "juan", "juan");

        try {
            userManager.add(empresa2);
        }catch (ElementoRepetido e){
            System.out.println(e.getMessage());
        }

        try {
            userManager.add(empleado);
        }catch (ElementoRepetido e){
            System.out.println(e.getMessage());
        }



        XML.writeXML(userManager, "Usuarios.XML");

        */
    }
}
