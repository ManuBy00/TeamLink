package org.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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


    /**
     * Lanza la ventana para el formulario de registro de empresas.
     * Carga el FXML 'ResgistroEmpresas.fxml' y lo muestra en un nuevo Stage.
     * @param actionEvent El evento disparado al hacer clic en el enlace de registro.
     */
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

    /**
     * Procesa la autenticación del usuario.
     *
     * 1. Busca el Usuario en el UsuariosManager y verifica la contraseña.
     * 2. Si el login es exitoso, inicia la sesión (Sesion.logIn()).
     * 3. Carga el 'mainView.fxml' en un nuevo Stage y cierra la ventana de Login actual.
     * 4. Si la autenticación falla, muestra una alerta de error.
     */
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
                    Stage stage = (Stage) emailField.getScene().getWindow();
                    stage.close();
                }catch (IOException e){
                    e.printStackTrace();
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
