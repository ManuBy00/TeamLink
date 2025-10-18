package org.example.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.CRUD.ChatsManager;
import org.example.CRUD.UsuariosManager;
import org.example.Model.*;
import org.example.Utilities.Sesion;

import java.io.IOException;
import java.lang.classfile.Label;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainController {
    @FXML
    private javafx.scene.control.Label nombreEmpersaLabel;

    @FXML
    private Button addChatButton;

    @FXML
    private Button newEmpleadoButton;

    @FXML
    private ListView<Chat> chatsListView;


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
        try {
            cargarChats(); // Llama a tu método
        } catch (Exception e) {
            // MUY IMPORTANTE: Imprime la causa del fallo real
            System.err.println("FATAL: Error al cargar los chats en MainView.");
            e.printStackTrace();

            // Muestra una alerta al usuario y quizás cierra la aplicación
            // Utilidades.mostrarAlerta("Error Crítico", "No se pudieron cargar los chats. Consulte la consola.");
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/teamlink/empleadoForm.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void cargarChats() {
        // Obtenemos los IDs de chat del usuario logueado (que hereda de Usuario)
        Set<Integer> chatIds = usuarioIniciado.getChatsID();

        // Creamos una lista para almacenar los objetos Chat completos
        List<Chat> chatsCompletos = new ArrayList<>();

        // Recorremos los IDs y buscamos el objeto Chat en el gestor central
        for (int chatId : chatIds) {
            Chat chatEncontrado = ChatsManager.getInstance().buscarChatPorId(chatId);

            if (chatEncontrado != null) {
                chatsCompletos.add(chatEncontrado);
            }
        }

        // Llenamos el ListView
        ObservableList<Chat> chatsObservableList;
        chatsObservableList = FXCollections.observableArrayList(chatsCompletos);
        chatsListView.setItems(chatsObservableList);

        // Configuramos la apariencia de las celdas (Cell Factory)
        configurarChatCellFactory();
    }

    private void configurarChatCellFactory() {
        chatsListView.setCellFactory(lv -> new javafx.scene.control.ListCell<Chat>() {
            @Override
            protected void updateItem(Chat chat, boolean empty) {
                super.updateItem(chat, empty);
                if (empty || chat == null) {
                    setText(null);
                } else {
                    // Lógica para mostrar el nombre:
                    // Si es grupal, muestra el nombre del grupo.
                    if (chat instanceof ChatGrupal) {
                        setText(((ChatGrupal) chat).getNombreGrupo());
                    } else {
                        // Si es privado, muestra el nombre del otro participante
                        // (Esto requiere lógica adicional para buscar el nombre por email en UsuariosManager)
                        setText("Chat Privado: ID " + chat.getChatID());
                    }
                }
            }
        });
    }

}
