package org.example.CRUD;

import org.example.DataAccess.XML;
import org.example.Exceptions.ElementoNoEncontrado;
import org.example.Exceptions.ElementoRepetido;
import org.example.Model.Chat;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name = "ListaConversaciones")
public class ChatsManager implements CRUD<Chat> {

    @XmlElement(name = "Chat")
    private ArrayList<Chat> chatsList;

    // Campo para asignar id al chat. se guarda en el xml y lo recibe la clase chat cuando crea un nuevo chat con el método getNextIdAndIncrement()
    @XmlElement(name = "NextChatID")
    private int nextChatId = 1;

    private static ChatsManager instance;
    private static final String FILE_NAME = "Chats.XML"; // Nombre del archivo XML

    // Constructor privado para el patrón Singleton
    private ChatsManager() {
        this.chatsList = new ArrayList<>();
    }

    /**
     * Obtiene la única instancia de ChatsManager (Singleton),
     * leyendo el estado persistente desde Chats.XML si existe.
     */
    public static ChatsManager getInstance(){
        if (instance == null){
            // 1. Intenta leer el XML y asigna el resultado a 'instance'.
            instance = XML.readXML(ChatsManager.class, FILE_NAME);
            // 2. Si la lectura falla (instance es null), crea una nueva instancia.
            if (instance == null){
                System.out.println("ADVERTENCIA: Archivo " + FILE_NAME + " no encontrado o vacío. Creando nuevo ChatsManager.");
                instance = new ChatsManager(); // Crea la instancia limpia
            }
        }
        return instance;
    }

    /**
     * Devuelve el ID actual y lo incrementa para el próximo chat.
     * Método clave para la persistencia del ID secuencial.
     */
    public int getNextIdAndIncrement() {
        return nextChatId++;
    }

    // --- Implementación de la Interfaz CRUD ---

    @Override
    public void add(Chat chat) throws ElementoRepetido {
        if (buscarChatPorId(chat.getChatID()) != null) {
            throw new ElementoRepetido("Ya existe un chat con ID: " + chat.getChatID());
        }
        this.chatsList.add(chat);
        // NOTA: Después de añadir/modificar, se debe llamar a XML.writeXML(instance, FILE_NAME);
    }

    @Override
    public void update(Chat chatActualizado) throws ElementoNoEncontrado {
        Chat chatAntiguo = buscarChatPorId(chatActualizado.getChatID());

        if (chatAntiguo == null) {
            throw new ElementoNoEncontrado("Chat con ID " + chatActualizado.getChatID() + " no encontrado para actualizar.");
        }

        // La actualización de un chat es compleja, ya que la lista de mensajes es una de sus propiedades.
        // El enfoque más simple es:
        this.chatsList.remove(chatAntiguo);
        this.chatsList.add(chatActualizado);

        // NOTA: En la práctica, si el mensaje se añade al objeto 'chatAntiguo' y ese objeto es el mismo
        // que está en la lista (si no haces deep copy), la lista se actualiza automáticamente.
        // Pero esta remoción/adición asegura que cualquier cambio en la metadata se refleje.
    }

    @Override
    public void remove(String chatIdString) throws ElementoNoEncontrado {
        int chatId = Integer.parseInt(chatIdString);
        Chat chatAEliminar = buscarChatPorId(chatId);

        if (chatAEliminar == null) {
            throw new ElementoNoEncontrado("Chat con ID " + chatId + " no encontrado para eliminar.");
        }

        this.chatsList.remove(chatAEliminar);
    }


    public Chat buscarChatPorId(int chatId) {
        return this.chatsList.stream()
                .filter(c -> c.getChatID() == chatId)
                .findFirst()
                .orElse(null);
    }


    public Chat buscar(String idString) {
        try {
            return buscarChatPorId(Integer.parseInt(idString));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Getter para la lista completa de chats
    public List<Chat> getChatsList() {
        return chatsList;
    }

    // --- Métodos obligatorios de la interfaz (dejados vacíos por ahora) ---

    @Override
    public void mostrar(Chat elemento) { /* ... */ }

    @Override
    public void cargarXML(String fileName) { /* La lógica está en getInstance() */ }
}