package org.example.Controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.CRUD.ChatsManager;
import org.example.CRUD.UsuariosManager;
import org.example.DataAccess.XML;
import org.example.Exceptions.DatoNoValido;
import org.example.Exceptions.ElementoRepetido;
import org.example.Model.*;
import org.example.Utilities.Sesion;
import org.example.Utilities.Utilidades;

import java.util.List;

public class ChatFormController {
    @FXML
    public Button crearGrupoButton;

    @FXML
    private TextField nombreGrupoTextField;

    @FXML
    private ListView<Usuario> miembrosGrupalListView;

    private Usuario usuarioIniciado = Sesion.getInstance().getUsuarioIniciado();



    @FXML
    public void initialize() {
        miembrosGrupalListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        if (usuarioIniciado instanceof Empresa){
            if (((Empresa) usuarioIniciado).getEmpleados() == null){
                Utilidades.mostrarAlerta("Alerta", "No hay usuarios disponibles");
            }else {
                List empleados = UsuariosManager.getInstance().buscarEmpleadosPorEmpresa(usuarioIniciado.getEmail());
                miembrosGrupalListView.setItems(FXCollections.observableArrayList(empleados));
            }
        }else{
            List empleados = UsuariosManager.getInstance().buscarEmpleadosPorEmpresa(((Empleado)usuarioIniciado).getEmpresa());
            empleados.remove(usuarioIniciado);
            miembrosGrupalListView.setItems(FXCollections.observableArrayList(empleados));
        }


        miembrosGrupalListView.setCellFactory(new Callback<ListView<Usuario>, ListCell<Usuario>>() {
            @Override
            public ListCell<Usuario> call(ListView<Usuario> param) {
                return new ListCell<Usuario>() {
                    @Override
                    protected void updateItem(Usuario usuario, boolean empty) {
                        super.updateItem(usuario, empty);
                        if (empty || usuario == null) {
                            setText(null);
                        } else {
                            setText(usuario.getNombre());
                        }
                    }
                };
            }
        });
    }

    /**
     * Procesa la creación de un nuevo Chat Grupal y guarda los cambios en el xml.
     * @return El objeto ChatGrupal recién creado.
     */
    public ChatGrupal crearGrupo(ActionEvent actionEvent) {
        ChatsManager cm = ChatsManager.getInstance();

        List<Usuario> usuariosGrupo = miembrosGrupalListView.getSelectionModel().getSelectedItems();
        String nombre = nombreGrupoTextField.getText();

        if (nombre.isEmpty()){
            Utilidades.mostrarAlerta("Campo vacío", "El campo nombre no puede estar vacío");
            return null;
        }

        //Convierte la lista de Usuarios seleccionados en una lista de emails (referencias).
        List<String> correosUsuarios = usuariosGrupo.stream().map(Usuario::getEmail).toList();
        //crea el objeto grupo
        ChatGrupal grupo = new ChatGrupal(nombre, correosUsuarios, usuarioIniciado.getEmail());
        try {
            //lo añade a la lista
            cm.add(grupo);
        } catch (ElementoRepetido e) {
            throw new RuntimeException(e);
        }

        // Añade el ID del chat a todos los miembros (usando el UsuariosManager para buscarlos)
        usuarioIniciado.getChatsID().add(grupo.getChatID());
        for (String email : correosUsuarios) {
            Usuario u = UsuariosManager.getInstance().buscarUsuario(email);
            if (u != null) {
                u.getChatsID().add(grupo.getChatID());
            }
        }
        //guarda los cambios en ambos XML
        XML.writeXML(cm, "Chats.XML");
        XML.writeXML(UsuariosManager.getInstance(), "Usuarios.XML");

        Stage stage = (Stage) nombreGrupoTextField.getScene().getWindow();
        stage.close();

        return grupo;
    }

    /**
     * Procesa la creación de un nuevo Chat Privado y guarda los cambios en el xml.
     * @param actionEvent El evento disparado al hacer clic en el botón "Crear chat privado".
     */
    public void crearChatPrivado(ActionEvent actionEvent) {
        ChatsManager cm = ChatsManager.getInstance();
        Usuario usuarioActual = Sesion.getInstance().getUsuarioIniciado();
        Usuario usuario2 = miembrosGrupalListView.getSelectionModel().getSelectedItem();

        // Valida que el chat no exista previamente entre los dos participantes utilizando ChatsManager.buscarChatPrivadoPorParticipantes().
        if (cm.buscarChatPrivadoPorParticipantes(usuarioActual.getEmail(), usuario2.getEmail()) !=null){
            Utilidades.mostrarAlerta("Error", "Ya existe un chat con este usuario");
            return;
        }
        // Crea un nuevo objeto ChatPrivado
        ChatPrivado nuevoChat = new ChatPrivado(usuarioActual.getEmail(), usuario2.getEmail());
        try {
            // Se añade al chatManager
            cm.add(nuevoChat);
        } catch (ElementoRepetido e) {
            throw new RuntimeException(e);
        }
        //se asigna el id del chat a los participantes
        usuarioActual.getChatsID().add(nuevoChat.getChatID());
        usuario2.getChatsID().add(nuevoChat.getChatID());

        //se guardan los xml
        XML.writeXML(cm, "Chats.XML");
        XML.writeXML(UsuariosManager.getInstance(), "Usuarios.XML");

        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
