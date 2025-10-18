package org.example.Model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "ChatPrivado")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChatPrivado extends Chat {

    // Identificador del primer participante (Email)
    @XmlElement(name = "Usuario1Email")
    private String usuario1Email;

    // Identificador del segundo participante (Email)
    @XmlElement(name = "Usuario2Email")
    private String usuario2Email;

    // Constructor para JAXB
    public ChatPrivado() {
    }

    public ChatPrivado(String usuario1Email, String usuario2Email) {
        // Llama al constructor de la clase padre (Chat), que inicializa la lista de mensajes
        super(true);
        this.usuario1Email = usuario1Email;
        this.usuario2Email = usuario2Email;
    }


    public String getUsuario1Email() {
        return usuario1Email;
    }

    public String getUsuario2Email() {
        return usuario2Email;
    }
}
