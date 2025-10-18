package org.example.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.CRUD.ChatsManager;
import org.example.CRUD.UsuariosManager;
import org.example.DataAccess.XML;
import org.example.Model.*;
import org.example.Utilities.Sesion;
import org.example.Utilities.Utilidades;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainController {
    @FXML
    public Button sendMensajeButton;
    @FXML
    public TextArea mensajeTextArea;
    @FXML
    public Label chatNameLabel;
    @FXML
    private javafx.scene.control.Label nombreEmpersaLabel;
    @FXML
    private ListView<Mensaje> mensajesListView;

    @FXML
    private ListView<Chat> chatsListView;

    @FXML
    private Button newEmpleadoButton;


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

        cargarChats(); // Llama a tu metodo


        chatsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // 'oldValue' es el chat que estaba seleccionado antes.
            // 'newValue' es el chat que se acaba de seleccionar (el "click").
            if (newValue != null) {
                // Aquí es donde llamas a la función que se ejecuta al hacer clic
                System.out.println("Chat seleccionado: ID " + newValue.getChatID() + ". Cargando mensajes...");
                cargarMensajes(newValue);
            }
        });

        if (usuarioIniciado instanceof Empleado){
            newEmpleadoButton.setVisible(false);
        }
    }



    public void addChat(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/teamlink/NewChatForm.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
            cargarChats();

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
                    if (chat instanceof ChatGrupal) {
                        setText(((ChatGrupal) chat).getNombreGrupo());
                    } else {
                        UsuariosManager um = UsuariosManager.getInstance();
                        Usuario usuario1 = um.buscarUsuario(((ChatPrivado)chat).getUsuario1Email());
                        Usuario usuario2 = um.buscarUsuario(((ChatPrivado)chat).getUsuario2Email());
                        if (usuarioIniciado.getEmail().equals(usuario1.getEmail())){
                            setText(usuario2.getNombre());
                        }else{
                            setText(usuario1.getNombre());
                        }
                    }
                }
            }
        });
    }

    private void cargarMensajes(Chat chatSeleccionado) {
        // 1. Limpiar cualquier mensaje antiguo
        mensajesListView.getItems().clear();

        // 2. Cargar la lista de mensajes del chat seleccionado
        ObservableList<Mensaje> mensajes = FXCollections.observableArrayList(chatSeleccionado.getMensajes());
        if (chatSeleccionado instanceof ChatGrupal){
            chatNameLabel.setText(((ChatGrupal) chatSeleccionado).getNombreGrupo());
        }else {
            UsuariosManager um = UsuariosManager.getInstance();
            Usuario usuario1 = um.buscarUsuario(((ChatPrivado)chatSeleccionado).getUsuario1Email());
            Usuario usuario2 = um.buscarUsuario(((ChatPrivado)chatSeleccionado).getUsuario2Email());
            if (usuarioIniciado.getEmail().equals(usuario1.getEmail())){
                chatNameLabel.setText(usuario2.getNombre());
            }else{
                chatNameLabel.setText(usuario2.getNombre());
            }
        }
        mensajesListView.setItems(mensajes);

        // Opcional: Desplazarse al último mensaje
        if (!mensajes.isEmpty()) {
            mensajesListView.scrollTo(mensajes.size() - 1);
        }
        configurarMensajeCellFactory();
    }


    private void configurarMensajeCellFactory() {
        String usuarioEmail = Sesion.getInstance().getUsuarioIniciado().getEmail();

        mensajesListView.setCellFactory(new javafx.util.Callback<ListView<Mensaje>, ListCell<Mensaje>>() {
            @Override
            public ListCell<Mensaje> call(ListView<Mensaje> param) {
                return new ListCell<Mensaje>() {
                    @Override
                    protected void updateItem(Mensaje mensaje, boolean empty) {
                        super.updateItem(mensaje, empty);

                        if (empty || mensaje == null) {
                            setText(null);
                            setGraphic(null);
                            setStyle(null);
                        } else {
                            // 1. Determinar si el mensaje es del usuario logueado
                            boolean isMine = mensaje.getRemitenteEmail().equals(usuarioEmail);

                            // 2. Crear el contenedor HBox
                            HBox container = new HBox();
                            container.setSpacing(5);

                            // 3. Crear la etiqueta del mensaje
                            Label contentLabel = new Label(mensaje.getContenido());
                            contentLabel.setWrapText(true); // Permite saltos de línea

                            // Opcional: Mostrar la hora del mensaje
                            String hora = mensaje.getFechaHora().toLocalTime().toString().substring(0, 5);
                            Label timeLabel = new Label(hora);
                            timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

                            // 4. Aplicar estilos y alineación
                            if (isMine) {
                                // Estilo para mensajes propios (azul/derecha)
                                contentLabel.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-padding: 8px; -fx-background-radius: 10px;");

                                // Alinea el contenido a la DERECHA
                                container.setAlignment(Pos.CENTER_RIGHT);
                                // Añadimos un separador vacío para empujar la burbuja
                                container.getChildren().addAll(timeLabel, contentLabel);
                            } else {
                                // Estilo para mensajes de otros (gris/izquierda)
                                contentLabel.setStyle("-fx-background-color: #BDC3C7; -fx-text-fill: black; -fx-padding: 8px; -fx-background-radius: 10px;");

                                // Alinea el contenido a la IZQUIERDA
                                container.setAlignment(Pos.CENTER_LEFT);
                                // Opcional: Añadir el nombre del remitente si es un chat grupal
                                Label remitenteLabel = new Label(mensaje.getRemitenteEmail()); // Idealmente busca el nombre aquí
                                remitenteLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 10px;");

                                container.getChildren().addAll(remitenteLabel, contentLabel, timeLabel);
                            }

                            // 5. Establecer el HBox como el gráfico de la celda
                            setGraphic(container);
                            setText(null); // No queremos texto simple, solo el gráfico (HBox)
                        }
                    }
                };
            }
        });
    }

    public void sendMensaje(ActionEvent actionEvent) {
        Chat chatSeleccionado = chatsListView.getSelectionModel().getSelectedItem();
        String contenido = mensajeTextArea.getText();
        if (contenido.isEmpty()){
            Utilidades.mostrarAlerta("Mensaje", "Escribe algo para enviar un mensaje");
            return;
        }
        Mensaje msg = new Mensaje(contenido, usuarioIniciado.getEmail());

        if (chatSeleccionado == null) {
            Utilidades.mostrarAlerta("Error", "Debes seleccionar un chat para enviar un mensaje.");
            return;
        }
        chatSeleccionado.addMensaje(msg);

        XML.writeXML(ChatsManager.getInstance(), "Chats.XML");

        mensajesListView.getItems().add(msg);

        // B. Limpiar el área de texto
        mensajeTextArea.clear();

        // C. Desplazarse al último mensaje para que sea visible
        mensajesListView.scrollTo(mensajesListView.getItems().size() - 1);
    }
}
