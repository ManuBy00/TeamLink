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

    public String getEmpresa() {
        return empresa;
    }

    public String getDepartamento() {
        return departamento;
    }

    public Boolean getManager() {
        return manager;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public void setManager(Boolean manager) {
        this.manager = manager;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }
}
