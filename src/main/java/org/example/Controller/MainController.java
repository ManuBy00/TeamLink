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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.CRUD.ChatsManager;
import org.example.CRUD.UsuariosManager;
import org.example.DataAccess.XML;
import org.example.Model.*;
import org.example.Utilities.Sesion;
import org.example.Utilities.Utilidades;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MainController {
    @FXML
    public Button sendMensajeButton;
    @FXML
    public TextArea mensajeTextArea;
    @FXML
    public Label chatNameLabel;
    @FXML
    public Button adjuntarButton;
    @FXML
    public TextField filtroNombreChatField;
    @FXML
    private javafx.scene.control.Label nombreEmpersaLabel;
    @FXML
    private ListView<Mensaje> mensajesListView;
    @FXML
    private ListView<Chat> chatsListView;
    @FXML
    private Button newEmpleadoButton;

    private Adjunto adjuntoTemporal;

    private static final String MEDIA_DIR = "MEDIA_DIR/";

    Usuario usuarioIniciado = Sesion.getInstance().getUsuarioIniciado();


    public enum ChatFilter {
        ALL, PRIVADOS, GRUPALES
    }

    private ChatFilter currentFilter = ChatFilter.ALL;



    @FXML
    public void initialize(){
        if (usuarioIniciado instanceof Empleado){
            Empleado empleadoIniciado = (Empleado) usuarioIniciado;
            nombreEmpersaLabel.setText(UsuariosManager.getInstance().buscarUsuario(empleadoIniciado.getEmpresa()).getNombre());

        }else {
            Empresa empresaIniciada = (Empresa) usuarioIniciado;
            nombreEmpersaLabel.setText(empresaIniciada.getNombre());
        }

        cargarChats();

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

        filtroNombreChatField.textProperty().addListener((observable, oldValue, newValue) -> {
            aplicarFiltroBusqueda(newValue);
        });
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

        List<Chat> chatsFiltrados = ChatsManager.filtrarChats(chatsCompletos, currentFilter);

        // Llenamos el ListView
        ObservableList<Chat> chatsObservableList;
        if (currentFilter == ChatFilter.ALL){
            chatsObservableList = FXCollections.observableArrayList(chatsCompletos);
        }else {
            chatsObservableList = FXCollections.observableArrayList(chatsFiltrados);
        }

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

        //Desplazarse al último mensaje
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
                            container.setAlignment(isMine ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

                            // Contenedor interno para texto y adjunto
                            VBox contentBox = new VBox(2);
                            contentBox.setAlignment(isMine ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

                            // 3. Crear el Label del Mensaje
                            Label contentLabel = new Label(mensaje.getContenido());
                            contentLabel.setWrapText(true);

                            // 4. Aplicar estilos y añadir la LÓGICA DE ADJUNTO
                            String bubbleStyle = isMine ? "-fx-background-color: #3498DB; -fx-text-fill: white; -fx-padding: 8px; -fx-background-radius: 10px;"
                                    : "-fx-background-color: #BDC3C7; -fx-text-fill: black; -fx-padding: 8px; -fx-background-radius: 10px;";

                            // 4.1. Si hay texto, lo añadimos al contentBox
                            if (!mensaje.getContenido().trim().isEmpty()) {
                                contentLabel.setStyle(bubbleStyle); // Aplicamos el estilo de burbuja
                                contentBox.getChildren().add(contentLabel);
                            }

                            // 4.2. Lógica del Adjunto
                            if (mensaje.getAdjunto() != null) {
                                Adjunto adjunto = mensaje.getAdjunto();
                                Hyperlink linkAdjunto = new Hyperlink("📎 " + adjunto.getNombreOriginal());
                                linkAdjunto.setMaxWidth(300);
                                linkAdjunto.setWrapText(true);

                                // Asignar el método para abrir el archivo
                                linkAdjunto.setOnAction(e -> abrirAdjunto(adjunto));

                                // Si solo hay adjunto y no texto, le damos un estilo de burbuja
                                if (mensaje.getContenido().trim().isEmpty()) {
                                    linkAdjunto.setStyle(bubbleStyle);
                                }

                                contentBox.getChildren().add(linkAdjunto);
                            }

                            // 5. Mostrar la hora
                            String hora = mensaje.getFechaHora().toLocalTime().toString().substring(0, 5);
                            Label timeLabel = new Label(hora);
                            timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

                            // 6. Añadir los elementos al contenedor principal (HBox)
                            if (isMine) {
                                // Alineación de derecha a izquierda: [Hora] [Burbuja]
                                container.getChildren().addAll(timeLabel, contentBox);
                            } else {
                                // Opcional: Mostrar el remitente (mejor si buscas el nombre en vez del email)
                                Label remitenteLabel = new Label(mensaje.getRemitenteEmail());
                                remitenteLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 10px;");

                                // Alineación de izquierda a derecha: [Nombre] [Burbuja] [Hora]
                                container.getChildren().addAll(remitenteLabel, contentBox, timeLabel);
                            }

                            // 7. Establecer el HBox como el gráfico de la celda
                            setGraphic(container);
                            setText(null);
                        }
                    }
                };
            }

        });
    }

    public void sendMensaje(ActionEvent actionEvent) {
        Chat chatSeleccionado = chatsListView.getSelectionModel().getSelectedItem();
        String contenido = mensajeTextArea.getText();
        Adjunto adjunto = adjuntoTemporal;
        Mensaje msg;

        if (contenido.isEmpty()){
            Utilidades.mostrarAlerta("Mensaje", "Escribe algo para enviar un mensaje");
            return;
        }
        if (adjunto==null){
            msg = new Mensaje(contenido, usuarioIniciado.getEmail());
        }else{
            msg = new Mensaje(contenido,usuarioIniciado.getEmail(), adjunto);
        }

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

        adjuntoTemporal = null;
    }

    public void adjuntar(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo Adjunto");

        Stage st = (Stage) sendMensajeButton.getScene().getWindow();
        File archivoSeleccionado = fileChooser.showOpenDialog(st);

        if (archivoSeleccionado != null) {
            // 3. Crear el directorio de media/ si no existe
            File mediaDirectory = new File("MEDIA_DIR");
            if (!mediaDirectory.exists()) {
                mediaDirectory.mkdirs();
            }
        }

        String nombreUnico = "_" + archivoSeleccionado.getName();
        File archivoDestino = new File(MEDIA_DIR + nombreUnico);

        // Declaramos los streams fuera del try para que el bloque 'finally' pueda acceder a ellos.
        InputStream is = null;
        OutputStream os = null;

        try {
            // Inicialización de los Streams (Dentro del try)
            is = new BufferedInputStream(new FileInputStream(archivoSeleccionado));
            os = new BufferedOutputStream(new FileOutputStream(archivoDestino));

            //Copia Manual
            byte[] buffer = new byte[1024]; // Búfer de 1 KB
            int bytesRead;

            // Leer del InputStream y escribir al OutputStream hasta que no queden más bytes
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            // 3. Validación de Tamaño (Debe ocurrir después de la copia exitosa)
            long maxSize = 10 * 1024 * 1024; // 10 Megabytes

            if (Files.size(archivoDestino.toPath()) > maxSize) {
                Files.delete(archivoDestino.toPath()); // Borrar el archivo si es muy grande
                Utilidades.mostrarAlerta("Error", "El archivo excede el tamaño máximo permitido (10 MB).");
            }

            // 5. Creación del Objeto Adjunto
            String tipo = Files.probeContentType(archivoDestino.toPath()); //determina el tipo de archivo
            if (tipo == null){
                tipo = "application/octet-stream"; //si es un tipo desconocido, le asigna un valor genérico
            }

            Adjunto adjunto = new Adjunto(nombreUnico, archivoDestino.getPath(), Files.size(archivoDestino.toPath()), tipo);
            adjuntoTemporal = adjunto;
            // 6. Feedback en la UI
            Utilidades.mostrarAlerta("Archivo adjuntado", "[ADJUNTO] " + nombreUnico + " listo para enviar. Escriba texto si lo desea.");

        } catch (IOException e) {
            // Si ocurre un error de I/O durante la inicialización, la lectura o la escritura
            Utilidades.mostrarAlerta("Error I/O", "Hubo un error al copiar el archivo.");
            e.printStackTrace();

        } finally {
            // El bloque 'finally' se ejecuta SIEMPRE, garantizando el cierre.
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void abrirAdjunto(Adjunto adjunto) {
        File archivo = new File(adjunto.getRutaGuardada());

        if (!archivo.exists()) {
            Utilidades.mostrarAlerta("Error", "El archivo adjunto no se encuentra.");
            return;
        }

        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(archivo);
            } catch (IOException e) {
                Utilidades.mostrarAlerta("Error", "No se pudo abrir el archivo.");
                e.printStackTrace();
            }
        } else {
            Utilidades.mostrarAlerta("Error", "Apertura de archivos no soportada.");
        }
    }

   public void exportarChat(ActionEvent actionEvent) {
        Chat chatSeleccionado = chatsListView.getSelectionModel().getSelectedItem();
        if (chatSeleccionado == null){
            Utilidades.mostrarAlerta("Elemento vacío", "Debes seleccionar un chat");
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona una ruta para el informe");
       fileChooser.setInitialFileName("chat_exportacion_" + chatSeleccionado.getChatID());

       fileChooser.getExtensionFilters().add(
               new FileChooser.ExtensionFilter("Archivo ZIP (*.zip)", "*.zip")
       );

        Stage st = (Stage) sendMensajeButton.getScene().getWindow();
        File archivoDestino = fileChooser.showSaveDialog(st);


        if (archivoDestino!=null){
            try {
                // 1. Generar el contenido del informe en una String
                String contenidoInforme = generarContenidoInforme(chatSeleccionado);

                // 2. Ejecutar la lógica de empaquetado (creando el ZIP y añadiendo archivos)
                empaquetarConversacionEnZip(chatSeleccionado, contenidoInforme, archivoDestino);

                Utilidades.mostrarAlerta("Éxito", "Conversación y adjuntos empaquetados correctamente en: " + archivoDestino.getAbsolutePath());

            } catch (IOException e) {
                Utilidades.mostrarAlerta("Error I/O", "Ocurrió un error al generar o empaquetar los archivos.");
                e.printStackTrace();
            }
        }
    }

    // Método auxiliar para generar el texto (reutilizado de tu código)
    private String generarContenidoInforme(Chat chat) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- INFORME DE CONVERSACIÓN (ID: ").append(chat.getChatID()).append(") ---\n\n");

        for (Mensaje msg : chat.getMensajes()){
            String mensaje = String.format("[%s] %s: %s\n",
                    msg.getFechaHora().toLocalTime().toString().substring(0, 5),
                    msg.getRemitenteEmail(),
                    msg.getContenido());
            sb.append(mensaje);
        }
        return sb.toString();
    }

    private void empaquetarConversacionEnZip(Chat chatSeleccionado, String contenidoInforme, File archivoDestino) throws IOException {
        // 1. Inicializar ZipOutputStream (usando try-with-resources para el cierre automático)
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(archivoDestino))) {

            // --- A. Añadir el Informe de Conversación (Texto) ---

            // Crear una entrada ZIP para el archivo de texto
            ZipEntry informeEntry = new ZipEntry("chat_informe_" + chatSeleccionado.getChatID() + ".txt");
            zos.putNextEntry(informeEntry);

            // Escribir el contenido del informe (String) como bytes en el ZIP
            zos.write(contenidoInforme.getBytes(StandardCharsets.UTF_8));

            // Cerrar la entrada del archivo de texto
            zos.closeEntry();

            // --- B. Añadir los Archivos Adjuntos (Binarios) ---

            for (Mensaje msg : chatSeleccionado.getMensajes()) {
                if (msg.getAdjunto() != null) {

                    // Obtener el archivo físico que se guardó en media/
                    File adjuntoFile = new File(msg.getAdjunto().getRutaGuardada());

                    if (adjuntoFile.exists()) {
                        // Crear una entrada ZIP con el nombre original del archivo
                        ZipEntry adjuntoEntry = new ZipEntry("adjuntos/" + msg.getAdjunto().getNombreOriginal());
                        zos.putNextEntry(adjuntoEntry);

                        // Copiar el contenido binario del adjunto al ZIP
                        try (FileInputStream fis = new FileInputStream(adjuntoFile)) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = fis.read(buffer)) > 0) {
                                zos.write(buffer, 0, bytesRead);
                            }
                        } // fis se cierra automáticamente

                        zos.closeEntry();
                    } else {
                        // Opcional: Registrar que el archivo físico no fue encontrado
                        System.err.println("Advertencia: Archivo adjunto no encontrado en la ruta: " + adjuntoFile.getPath());
                    }
                }
            }
        }
    }    // zos (ZipOutputStream) se cierra automáticamente aquí, finalizando el archivo ZIP


    public void generarResumen(ActionEvent actionEvent) {
        Chat chatSeleccionado = chatsListView.getSelectionModel().getSelectedItem();

        if (chatSeleccionado == null) {
            Utilidades.mostrarAlerta("Error", "Selecciona un chat para generar el resumen.");
            return;
        }

        List<Mensaje> listaMensajes = chatSeleccionado.getMensajes();
        if (listaMensajes.isEmpty()) {
            Utilidades.mostrarAlerta("Advertencia", "No hay mensajes en este chat para analizar.");
            return;
        }

        try {
            // 1. Generar el Objeto Resumen
            ResumenChat resumen = analizarChatConStreams(listaMensajes, chatSeleccionado);

            // 2. Crear la Nueva Ventana (Stage)
            Stage resumenStage = new Stage();
            resumenStage.setTitle("Informe Estadístico Chat ID: " + resumen.getChat().getChatID());

            // 3. Crear el Área de Texto y el Contenido
            TextArea resumenTextArea = new TextArea(resumen.toString());
            resumenTextArea.setEditable(false);
            resumenTextArea.setPrefSize(600, 450);
            resumenTextArea.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 13;"); // Para mejor formato

            // 4. Configurar la escena
            VBox root = new VBox(resumenTextArea);
            root.setStyle("-fx-padding: 10;");
            Scene scene = new Scene(root);

            resumenStage.setScene(scene);

            // 5. Mostrar la ventana
            resumenStage.show();

        } catch (Exception e) {
            Utilidades.mostrarAlerta("Error de Análisis", "Ocurrió un error al procesar el chat.");
            e.printStackTrace();
        }
    }

    private ResumenChat analizarChatConStreams(List<Mensaje> listaMensajes, Chat chat) {
        // A. Conteo Total
        long totalMensajes = listaMensajes.stream().count();

        // B. Conteo por Usuario (Map<Email, Count>)
        Map<String, Long> mensajesPorUsuario = listaMensajes.stream()
                .collect(Collectors.groupingBy(
                        Mensaje::getRemitenteEmail,
                        Collectors.counting()
                ));

        // C. Palabras Más Comunes (TOP 5)
        Map<String, Long> frecuenciaPalabras = listaMensajes.stream()
                .map(Mensaje::getContenido)
                // Divide el contenido por cualquier carácter que NO sea una letra/número (mejor para limpieza)
                .flatMap(contenido -> List.of(contenido.toLowerCase().split("[^a-zñáéíóúüA-ZÑÁÉÍÓÚÜ0-9]+")).stream())
                .filter(palabra -> palabra.length() > 3) // Filtra palabras irrelevantes
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        //Mensajes con adjuntos
        long mensajesConAdjunto = listaMensajes.stream()
                .filter(mensaje -> mensaje.getAdjunto() != null)
                .count();

        // Ordenar y limitar el resultado
        List<Map.Entry<String, Long>> topPalabras = frecuenciaPalabras.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toList());

        // 4. Devolver el Objeto ResumenChat
        return new ResumenChat(chat, totalMensajes, mensajesPorUsuario, topPalabras,mensajesConAdjunto);
    }


    public void filtrarGrupos(MouseEvent mouseEvent) {
        currentFilter = ChatFilter.GRUPALES;
        cargarChats();
    }

    public void filtrarPrivados(MouseEvent mouseEvent) {
        currentFilter = ChatFilter.PRIVADOS;
        cargarChats();
    }

    public void allChats(MouseEvent mouseEvent) {
        currentFilter = ChatFilter.ALL;
        cargarChats();
    }

    /**
     * Aplica un filtro de búsqueda por nombre o email al ListView de chats.
     * * Si la cadena de búsqueda contiene texto, se filtran todos los chats:
     * 1. Chats Grupales: Se genera una lista solo de chat grupales, y se filtra por el campo 'nombreGrupo'.
     * 2. Chats Privados: Se genera una lista solo de chat privados y se buscan por el email del otro participante.
     * actualiza el chatsListView.
     * * @param nombreABuscar La cadena de texto introducida por el usuario en el campo de búsqueda.
     */
    private void aplicarFiltroBusqueda(String nombreABuscar) {
        ChatsManager cm = ChatsManager.getInstance();
        ArrayList<Chat> chatList = (ArrayList<Chat>) cm.getChatsList();

        //obtenemos lista de chat grupales
        List<ChatGrupal> chatGrupales = chatList.stream()
                .filter(chat -> chat instanceof ChatGrupal) //filtramos grupos
                .map(chat -> (ChatGrupal) chat) //los casteamos
                .collect((Collectors.toList())); //guardamos en lista

        //obtenemos lista de chat privados
        List<ChatPrivado> chatPrivados = chatList.stream()
                .filter(chat -> chat instanceof ChatPrivado) //filtramos grupos
                .map(chat -> (ChatPrivado) chat) //los casteamos
                .collect((Collectors.toList())); //guardamos en lista

        //filtramos los grupos por el nombre del grupo
        List<ChatGrupal> gruposFiltrados = chatGrupales.stream()
                .filter(grupo -> grupo.getNombreGrupo().toLowerCase().contains(nombreABuscar.toLowerCase()))
                .toList();

        //filtramos los chats privados por el email (primero debemos diferenciar cual nuestro email,
        // ya que cada chat tiene 2 participantes)
        List<ChatPrivado> privadosFiltrados = chatPrivados.stream()
                .filter(privado -> {
                    String email1 = privado.getUsuario1Email().toLowerCase();
                    String email2 = privado.getUsuario2Email().toLowerCase();

                    String emailDelOtro;
                    if (email1.equals(usuarioIniciado.getEmail())) {
                        emailDelOtro = email2;
                    }else {
                        emailDelOtro=email1;
                    }
                      return emailDelOtro.toLowerCase().contains(nombreABuscar.toLowerCase());
                        }).toList();

        //creamos arrayList para unir todos los chats filtrados
        ArrayList<Chat> chatsFiltradosNombre = new ArrayList<>();
        chatsFiltradosNombre.addAll(privadosFiltrados);
        chatsFiltradosNombre.addAll(gruposFiltrados);

        //actualizamos la lista de chats con el filtro aplicado
        chatsListView.setItems(FXCollections.observableArrayList(chatsFiltradosNombre));

    }

}
