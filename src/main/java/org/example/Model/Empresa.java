package org.example.Model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.reflect.Array;
import java.util.ArrayList;

@XmlRootElement(name="Empresa")
@XmlAccessorType(XmlAccessType.FIELD)
public class Empresa extends Usuario{
    private  String direccion;
    private String telefono;
    private String descripcion;
    private String sector;
    private ArrayList<Usuario> empleados;
    private ArrayList<Chat> chats;

    public Empresa(String email, String nombre, String password, String direccion, String telefono, String descripcion, String sector) {
        super(email, nombre, password);
        this.direccion = direccion;
        this.telefono = telefono;
        this.sector = sector;
        this.descripcion = descripcion;
        this.empleados = new ArrayList<>();
        this.chats = new ArrayList<>();
    }

    public Empresa(){
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public ArrayList<Usuario> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(ArrayList<Usuario> empleados) {
        this.empleados = empleados;
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
    }
}
