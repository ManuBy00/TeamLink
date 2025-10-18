package org.example.Model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ChatGrupal")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChatGrupal extends Chat{

    private String nombreGrupo;

    @XmlElementWrapper(name = "MiembrosDelGrupo")
    @XmlElement(name = "Email")
    private ArrayList<String> miembrosEmails;

    private String emailEmpresa;

    // Constructor para JAXB
   public ChatGrupal(){

   }

    public ChatGrupal(String nombreGrupo, List<String> miembrosEmails, String emailEmpresa) {
        // Llama al constructor de la clase padre (Chat)
        super(true);
        this.nombreGrupo = nombreGrupo;
        this.miembrosEmails = new ArrayList<>(miembrosEmails);
        this.emailEmpresa = emailEmpresa;
    }

    // Getters
    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public ArrayList<String> getMiembrosEmails() {
        return miembrosEmails;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public void setMiembrosEmails(ArrayList<String> miembrosEmails) {
        this.miembrosEmails = miembrosEmails;
    }

    public String getEmailEmpresa() {
        return emailEmpresa;
    }

    public void setEmailEmpresa(String emailEmpresa) {
        this.emailEmpresa = emailEmpresa;
    }
}
