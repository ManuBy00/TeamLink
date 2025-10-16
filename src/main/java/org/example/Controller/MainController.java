package org.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.CRUD.UsuariosManager;
import org.example.Model.Chat;
import org.example.Model.Empleado;
import org.example.Model.Empresa;
import org.example.Model.Usuario;
import org.example.Utilities.Sesion;

import java.io.IOException;
import java.lang.classfile.Label;

public class MainController {
    @FXML
    private javafx.scene.control.Label nombreEmpersaLabel;

    @FXML
    private Button addChatButton;

    @FXML
    private ListView<Chat> ChatList;

    Usuario usuarioIniciado = Sesion.getInstance().getUsuarioIniciado();

    @FXML
    public void initialize(){

        if (usuarioIniciado instanceof Empleado){
            Empleado empleadoIniciado = (Empleado) usuarioIniciado;
            nombreEmpersaLabel.setText(UsuariosManager.getInstance().buscarUsuario(empleadoIniciado.getEmpresa()).getNombre());

        }else {
            Empresa empresaIniciada = (Empresa) usuarioIniciado;
            nombreEmpersaLabel.setText(empresaIniciada.getNombre());
        }
    }


    public void addChat(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/teamlink/NewChatForm.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void crearEmpleado(ActionEvent actionEvent) {

    }
}
