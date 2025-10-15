package org.example.Model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.PrimitiveIterator;
@XmlRootElement(name="Empleado")
@XmlAccessorType(XmlAccessType.FIELD)
public class Empleado extends Usuario{
    private String empresa;
    private String departamento;
    private Boolean manager;
    private String puesto;

    public Empleado(String email, String nombre, String password, String empresa, String departamento, Boolean manager, String puesto) {
        super(email, nombre, password);
        this.empresa = empresa;
        this.departamento = departamento;
        this.manager = manager;
        this.puesto = puesto;
    }

    public Empleado(){

    }
}
