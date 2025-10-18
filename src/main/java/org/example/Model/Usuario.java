package org.example.Model;

import org.example.Utilities.Seguridad;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Usuario")
@XmlSeeAlso({Empresa.class, Empleado.class})

public abstract class Usuario {
    private String email;
    private String nombre;
    private String password;
    @XmlElementWrapper(name = "ListaDeChatIDs")
    @XmlElement(name = "ChatID")
    private HashSet<Integer> chatsID;


    public Usuario(String email, String nombre, String password) {
        this.email = email;
        this.nombre = nombre;
        this.password = Seguridad.hashPassword(password);
        this.chatsID = new HashSet<>();
    }

    public Usuario() {
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPassword() {
        return password;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HashSet<Integer> getChatsID() {
        return chatsID;
    }

    public void setChatsID(HashSet<Integer> chatsID) {
        this.chatsID = chatsID;
    }

    public boolean verificarPassword(String password){
        return Seguridad.checkPassword(password, this.password);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Usuario usuario)) return false;
        return Objects.equals(email, usuario.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                ", email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
