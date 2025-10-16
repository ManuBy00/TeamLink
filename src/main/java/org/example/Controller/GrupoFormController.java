package org.example.Controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import org.example.Model.*;
import org.example.Utilities.Sesion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GrupoFormController {
    @FXML
    private TextField nombreGrupo;

    @FXML
    private ListView<Usuario> miembrosGrupalListView;

    private Empresa empresa = (Empresa)Sesion.getInstance().getUsuarioIniciado();


    @FXML
    public void initialize() {
        miembrosGrupalListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //llenamos la listview con los empleados de la empresa
        if (empresa.getEmpleados() == null){

        }else {
            miembrosGrupalListView.setItems(FXCollections.observableArrayList(empresa.getEmpleados()));
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
        List<Usuario> usuariosGrupo = miembrosGrupalListView.getSelectionModel().getSelectedItems();
        String nombre = nombreGrupo.getText();

        List<String> correosUsuarios = usuariosGrupo.stream().map(Usuario::getEmail).toList();

        ChatGrupal grupo = new ChatGrupal(nombre, correosUsuarios);

        return grupo;
    }
}
