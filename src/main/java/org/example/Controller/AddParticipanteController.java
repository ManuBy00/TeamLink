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
import org.example.Model.ChatGrupal;
import org.example.Model.Empleado;
import org.example.Model.Empresa;
import org.example.Model.Usuario;
import org.example.Utilities.Sesion;
import org.example.Utilities.Utilidades;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AddParticipanteController {
    @FXML
    public TextField filtroEmpleadoTextField;
    @FXML
    private Label tituloLabel;
    @FXML
    private ListView<Usuario> empleadosListView;

    private ChatGrupal chatActual;

    private List<Usuario> fuenteOriginalEmpleados;

    private Usuario usuarioIniciado = Sesion.getInstance().getUsuarioIniciado();

    // Obtenemos el email de la empresa (o el propio si es Empresa)
    private final String emailEmpresa = (usuarioIniciado instanceof Empresa) ? usuarioIniciado.getEmail() : ((Empleado)usuarioIniciado).getEmpresa();


    // Metodo para recibir el chat a modificar (se llama desde MainController)
    public void setChatGrupal(ChatGrupal chat) {
        this.chatActual = chat;
        tituloLabel.setText("Añadir a Grupo: " + chat.getNombreGrupo());
        cargarEmpleadosDisponibles();
    }

    // Método de inicialización estándar de JavaFX
    @FXML
    public void initialize() {
        // La inicialización de chatActual se hace en setChatGrupal(), por lo que la lista se configura más tarde.
        empleadosListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // 1. Añadir el Listener de Búsqueda (Lambda)
        filtroEmpleadoTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            aplicarFiltroEmpleados(newValue);
        });
    }

    /**
     * Carga la lista de empleados de la empresa actual que están disponibles para ser añadidos
     * al grupo seleccionado (filta los que ya están dentro del grupo). Este metodo se ejecuta antes
     * de lanzar la pantalla, ya que se llama de setChatGrupal
     */
    private void cargarEmpleadosDisponibles() {
        // Obtener todos los empleados de la empresa
        List<Empleado> todosEmpleados = UsuariosManager.getInstance().buscarEmpleadosPorEmpresa(emailEmpresa);

        //  Obtener la lista actual de miembros del grupo (emails)
        Set<String> miembrosActualesEmails = chatActual.getMiembrosEmails().stream().collect(Collectors.toSet());

        //  Filtrar: Excluir a los miembros que YA están en el grupo y a uno mismo (si es que no se excluyó antes)
        List<Usuario> empleadosDisponibles = todosEmpleados.stream()
                // Filtramos aquellos cuyo email NO esté en la lista de miembros actuales
                .filter(empleado -> !miembrosActualesEmails.contains(empleado.getEmail()))
                // Nos aseguramos de que no se intente añadir a sí mismo (si es Empleado)
                .filter(empleado -> !empleado.equals(usuarioIniciado))
                .collect(Collectors.toList());

        //Almacenar la lista original (sin filtrar por nombre)
        this.fuenteOriginalEmpleados = empleadosDisponibles;

        //  Llenar el ListView con la lista completa inicial
        empleadosListView.setItems(FXCollections.observableArrayList(fuenteOriginalEmpleados));

        //  CONFIGURACIÓN DEL CELL FACTORY para mostrar el nombre
        empleadosListView.setCellFactory(new Callback<ListView<Usuario>, ListCell<Usuario>>() {
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
     * Aplica un filtro de búsqueda por nombre sobre la lista de empleados disponibles.
     * @param textoBusqueda La cadena introducida en el TextField.
     */
    private void aplicarFiltroEmpleados(String textoBusqueda) {
        //normalizamos el texto de entrada
        String busquedaNormalizada = textoBusqueda.trim().toLowerCase();

        // Si la lista original no existe o está vacía, o si la búsqueda está vacía
        if (fuenteOriginalEmpleados == null || fuenteOriginalEmpleados.isEmpty() || busquedaNormalizada.isEmpty()) {
            empleadosListView.setItems(FXCollections.observableArrayList(fuenteOriginalEmpleados));
            return;
        }

        // 2. Aplicar el filtro por nombre sobre la fuente original
        List<Usuario> empleadosFiltrados = fuenteOriginalEmpleados.stream()
                .filter(usuario ->
                        // 2A. Evitar NullPointer y asegurar que el nombre no es null
                        usuario.getNombre() != null &&
                                // 2B. Filtrar: Convertir a minúsculas y buscar coincidencia parcial (contains)
                                usuario.getNombre().toLowerCase().contains(busquedaNormalizada)
                )
                .collect(Collectors.toList());

        // 3. Actualizar el ListView con los resultados filtrados
        empleadosListView.setItems(FXCollections.observableArrayList(empleadosFiltrados));
    }


    /**
     * Procesa la acción de añadir nuevos participantes al Chat Grupal actualmente seleccionado.
     */
    @FXML
    private void addParticipants(ActionEvent event) {
        //obtenemos los usuarios seleccionados del listview
        List<Usuario> nuevosParticipantes = empleadosListView.getSelectionModel().getSelectedItems();

        if (nuevosParticipantes.isEmpty()) {
            Utilidades.mostrarAlerta("Advertencia", "Selecciona al menos un usuario para añadir.");
            return;
        }

        int chatID = chatActual.getChatID();

        //  Añadir los emails de los nuevos usuarios al objeto ChatGrupal en memoria y actualizar UsuariosManager
        for (Usuario u : nuevosParticipantes) {
            chatActual.getMiembrosEmails().add(u.getEmail());
            //  Añadir el ID de chat al objeto Usuario en UsuariosManager
            u.getChatsID().add(chatID);
        }

        // 3. guardar los cambios en ambos XML
        XML.writeXML(ChatsManager.getInstance(), "Chats.XML");
        XML.writeXML(UsuariosManager.getInstance(), "Usuarios.XML");

        Utilidades.mostrarAlerta("Éxito", "Se añadieron " + nuevosParticipantes.size() + " participantes al grupo.");

        // 4. Cerrar la ventana
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}