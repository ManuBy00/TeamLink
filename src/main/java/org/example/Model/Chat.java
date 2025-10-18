package org.example.Model;



import org.example.CRUD.ChatsManager;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;


@XmlSeeAlso({ChatPrivado.class, ChatGrupal.class})
@XmlRootElement(name = "Chat")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Chat {
    private int chatID;
    @XmlElementWrapper(name = "ListaDeMensajes")
    @XmlElement(name = "Mensaje")
    private ArrayList<Mensaje> mensajes;

    /**
     * Constructor para JAXB (Deserialización).
     * No genera un ID nuevo, usa el ID del XML.
     */
    public Chat() {
        this.mensajes = new ArrayList<>();
        // El campo 'id' será rellenado por JAXB.
    }

    /**
     * Constructor para crear un NUEVO chat (Llamado por las clases hijas).
     * Obtiene un ID único y secuencial desde el ChatsManager.
     * @param nuevo True si se debe generar un ID. Para diferenciarlo del constructor vacío.
     */
    public Chat(boolean nuevo) {
        this(); // Inicializa la lista de mensajes
        if (nuevo) {
            // Lógica clave: Obtiene el siguiente ID persistente y lo incrementa en el gestor.
            this.chatID = ChatsManager.getInstance().getNextIdAndIncrement();
        }
    }

    public void addMensaje(Mensaje mensaje) {
        this.mensajes.add(mensaje);
    }

    public int getChatID() {
        return chatID;
    }

    public ArrayList<Mensaje> getMensajes() {
        return mensajes;
    }
}