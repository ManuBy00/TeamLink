package org.example.Model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ChatGrupal")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChatGrupal extends Chat{
    // Nombre que se mostrará en la interfaz para identificar el grupo
    @XmlElement(name = "NombreGrupo")
    private String nombreGrupo;

    // Lista de emails de todos los miembros del grupo
    @XmlElement(name = "MiembrosEmails")
    private ArrayList<String> miembrosEmails;


    // Constructor para JAXB
   public ChatGrupal(){

   }

    public ChatGrupal(String nombreGrupo, List<String> miembrosEmails) {
        // Llama al constructor de la clase padre (Chat)
        super();
        this.nombreGrupo = nombreGrupo;
        this.miembrosEmails = new ArrayList<>(miembrosEmails);
    }

    // Getters
    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public ArrayList<String> getMiembrosEmails() {
        return miembrosEmails;
    }
}
