package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.CRUD.UsuariosManager;
import org.example.DataAccess.XML;
import org.example.Model.Usuario;

import java.net.URL;

import static javafx.application.Application.launch;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {


        UsuariosManager um = UsuariosManager.getInstance();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/teamlink/LoginView.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}


