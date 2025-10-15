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
}
