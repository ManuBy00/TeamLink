package org.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.CRUD.UsuariosManager;
import org.example.Exceptions.DatoNoValido;
import org.example.Model.Usuario;
import org.example.Utilities.Sesion;
import org.example.Utilities.Utilidades;

import java.io.IOException;
import java.util.ArrayList;
public class LoginController {


    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;


    public void lanzarRegForm(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/teamlink/ResgistroEmpresas.fxml"));
        try {
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Registro");
            stage.show();


        }catch (IOException e){
            e.getMessage();
        }
    }


    public void login(ActionEvent actionEvent) {
        String email = emailField.getText();
        String password = passwordField.getText();

        Usuario u = UsuariosManager.getInstance().buscarUsuario(email);
        if (u!=null){
            if (u.verificarPassword(password)){
                Sesion.getInstance().logIn(u);

                try{
                    FXMLLoader mainView = new FXMLLoader(getClass().getResource("/org/example/teamlink/mainView.fxml"));
                    Parent root = mainView.load();
                    Scene scene = new Scene(root);
                    Stage st = new Stage();
                    st.setScene(scene);
                    st.show();
                }catch (IOException e){
                    e.getMessage();
                }

                Utilidades.mostrarAlerta("OK", "LOGIN CORRECTO");
            }else {
                Utilidades.mostrarAlerta("Contraseña incorrecta", "Las credenciales introducidas no coinciden");
            }

        }else {
            Utilidades.mostrarAlerta("Usuario no encontrado", "El email introducido no está registrado.");
        }

    }
}
