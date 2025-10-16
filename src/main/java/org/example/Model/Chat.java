package org.example.Model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement(name = "Chat")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Chat {
    @XmlElement(name = "Mensajes")
    private ArrayList<Mensaje> mensajes;

    public Chat() {
        this.mensajes = new ArrayList<>();
    }


    public void addMensaje(Mensaje mensaje) {
        this.mensajes.add(mensaje);
    }


    public ArrayList<Mensaje> getMensajes() {
        return mensajes;
    }
}
