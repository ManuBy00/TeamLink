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
import org.example.Exceptions.ElementoRepetido;
import org.example.Model.*;
import org.example.Utilities.Sesion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GrupoFormController {
    @FXML
    public Button crearGrupoButton;

    @FXML
    private TextField nombreGrupoTextField;

    @FXML
    private ListView<Usuario> miembrosGrupalListView;

    private Empresa empresa = (Empresa)Sesion.getInstance().getUsuarioIniciado();


    @FXML
    public void initialize() {
        miembrosGrupalListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //llenamos la listview con los empleados de la empresa
        if (empresa.getEmpleados() == null){

        }else {
            List empleados = UsuariosManager.getInstance().buscarEmpleadosPorEmpresa(empresa.getEmail());
            miembrosGrupalListView.setItems(FXCollections.observableArrayList(empleados));
        }

        //no se como va esto jaja
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
                            // Muestra el nombre del usuario
                            setText(usuario.getNombre());
                            // Podrías poner también: setText(usuario.getNombre() + " (" + usuario.getEmail() + ")");
                        }
                    }
                };
            }
        });


    }

    public ChatGrupal crearGrupo(ActionEvent actionEvent) {
        ChatsManager cm = ChatsManager.getInstance();

        List<Usuario> usuariosGrupo = miembrosGrupalListView.getSelectionModel().getSelectedItems();
        String nombre = nombreGrupoTextField.getText();

        List<String> correosUsuarios = usuariosGrupo.stream().map(Usuario::getEmail).toList();


        ChatGrupal grupo = new ChatGrupal(nombre, correosUsuarios, empresa.getEmail());
        try {
            cm.add(grupo);
        } catch (ElementoRepetido e) {
            throw new RuntimeException(e);
        }

        // Añadir el ID a todos los miembros (usando el UsuariosManager para buscarlos)
        empresa.getChatsID().add(grupo.getChatID());
        for (String email : correosUsuarios) {
            Usuario u = UsuariosManager.getInstance().buscarUsuario(email);
            if (u != null) {
                u.getChatsID().add(grupo.getChatID());
            }
        }


        XML.writeXML(cm, "Chats.XML");
        XML.writeXML(UsuariosManager.getInstance(), "Usuarios.XML");


        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();

        return grupo;
    }
}
