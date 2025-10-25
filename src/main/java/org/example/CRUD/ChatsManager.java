package org.example.CRUD;

import org.example.Controller.MainController;
import org.example.DataAccess.XML;
import org.example.Exceptions.ElementoNoEncontrado;
import org.example.Exceptions.ElementoRepetido;
import org.example.Model.Chat;
import org.example.Model.ChatGrupal;
import org.example.Model.ChatPrivado;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;
import java.util.stream.Collectors;


@XmlRootElement(name = "ListaConversaciones")
public class ChatsManager implements CRUD<Chat> {

    @XmlElement(name = "Chat")
    private ArrayList<Chat> chatsList;

    // Campo para asignar id al chat. se guarda en el xml y lo recibe la clase chat cuando crea un nuevo chat con el método getNextIdAndIncrement()
    @XmlElement(name = "NextChatID")
    private int nextChatId = 1;

    private static ChatsManager instance;
    private static final String FILE_NAME = "Chats.XML"; // Nombre del archivo XML


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

    // Getter para la lista completa de chats
    public List<Chat> getChatsList() {
        return chatsList;
    }

    /**
     * Devuelve el ID actual y lo incrementa para el próximo chat.
     * Método clave para la persistencia del ID secuencial.
     */
    public int getNextIdAndIncrement() {
        return nextChatId++;
    }

    // --- Implementación de la Interfaz CRUD ---

    /**
     * Añade una chat a la lista
     * @param chat el chat a añadir
     * @throws ElementoRepetido si ya hay un chat con el mismo id
     */
    @Override
    public void add(Chat chat) throws ElementoRepetido {
        if (buscarChatPorId(chat.getChatID()) != null) {
            throw new ElementoRepetido("Ya existe un chat con ID: " + chat.getChatID());
        }
        this.chatsList.add(chat);
    }

    /**
     * Acualiza un chat borrando el antiguo y añadiendo el nuevo.
     * @param chatActualizado
     * @throws ElementoNoEncontrado
     */
    @Override
    public void update(Chat chatActualizado) throws ElementoNoEncontrado {
        Chat chatAntiguo = buscarChatPorId(chatActualizado.getChatID());

        if (chatAntiguo == null) {
            throw new ElementoNoEncontrado("Chat con ID " + chatActualizado.getChatID() + " no encontrado para actualizar.");
        }
        this.chatsList.remove(chatAntiguo);
        this.chatsList.add(chatActualizado);


    }

    /**
     * Elimina el chat de la lista de chatManager
     * @param chatIdString
     * @throws ElementoNoEncontrado si el chat no existe
     */
    @Override
    public void remove(String chatIdString) throws ElementoNoEncontrado {
        int chatId = Integer.parseInt(chatIdString);
        Chat chatAEliminar = buscarChatPorId(chatId);

        if (chatAEliminar == null) {
            throw new ElementoNoEncontrado("Chat con ID " + chatId + " no encontrado para eliminar.");
        }

        this.chatsList.remove(chatAEliminar);
    }

    /**
     * Busca y recupera un objeto Chat de la lista central utilizando su ID único.
     * Este método utiliza Streams para iterar de manera eficiente sobre la lista de chats,
     * comparando el chatId proporcionado.
     * @param chatId El ID entero del chat a buscar.
     * @return El objeto Chat encontrado, o null si no se encuentra ninguna coincidencia.
     */
    public Chat buscarChatPorId(int chatId) {

        Chat chatEncontrado = this.chatsList.stream().filter(c -> c.getChatID() == chatId).findFirst().orElse(null);
        return chatEncontrado;
    }

    /**
     * Busca un ChatPrivado que contenga exactamente a los dos usuarios dados.
     * Implementación usando bucle for-each y Set.
     * @param email1 Email del primer usuario.
     * @param email2 Email del segundo usuario.
     * @return El objeto ChatPrivado si se encuentra, o null.
     */
    public ChatPrivado buscarChatPrivadoPorParticipantes(String email1, String email2) {

        // 1. Crear el conjunto 'target' (objetivo) de los dos emails que buscamos.
        Set<String> targetEmails = new HashSet<>();
        targetEmails.add(email1);
        targetEmails.add(email2);

        // 2. Iterar sobre todos los chats en la lista central.
        for (Chat chat : this.chatsList) {
            // 3. Verificar si el chat actual es de tipo ChatPrivado.
            if (chat instanceof ChatPrivado chatPrivado) {
                // 4. Obtener los emails del chat que estamos revisando.
                Set<String> currentChatEmails = new HashSet<>();
                        currentChatEmails.add(chatPrivado.getUsuario1Email());
                        currentChatEmails.add(chatPrivado.getUsuario2Email());

                // 5. Verificar si los dos conjuntos son iguales.
                if (currentChatEmails.equals(targetEmails)) {
                    // Si los sets son iguales (mismos emails), hemos encontrado el chat.
                    return chatPrivado;
                }
            }
        }
        // 6. Si el bucle termina, el chat no existe.
        return null;
    }


    /**
     * Filtra una lista de chats basándose en el tipo de filtro especificado.
     * Este método es estático para que pueda ser llamado sin necesidad de la instancia Singleton,
     * aunque puedes mantenerlo de instancia si lo prefieres.
     * * @param chatsCompletos La lista de todos los objetos Chat.
     * @param filtro El estado de filtro deseado (ALL, PRIVADOS, GRUPALES).
     * @return Una lista de chats que cumplen con el criterio de filtro.
     */
    public static List<Chat> filtrarChats(List<Chat> chatsCompletos, MainController.ChatFilter filtro) {
        if (chatsCompletos == null) {
            return List.of(); // Devuelve una lista vacía si la entrada es nula
        }

        return chatsCompletos.stream()
                .filter(chat -> {
                    if (filtro == MainController.ChatFilter.PRIVADOS) {
                        return chat instanceof ChatPrivado;
                    } else if (filtro == MainController.ChatFilter.GRUPALES) {
                        return chat instanceof ChatGrupal;
                    }
                    return true; // ChatFilter.ALL
                })
                .collect(Collectors.toList());
    }

}