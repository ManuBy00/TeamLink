package org.example.Model;

import org.example.CRUD.UsuariosManager;
import org.example.Exceptions.ElementoNoEncontrado;
import org.example.Exceptions.ElementoRepetido;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement(name="Empresa")
@XmlAccessorType(XmlAccessType.FIELD)
public class Empresa extends Usuario{

    // --- Atributos Propios de Empresa ---
    private String direccion;
    private String telefono;
    private String descripcion;
    private String sector;
    @XmlElementWrapper(name = "ListaEmpleadosEmails")
    @XmlElement(name = "EmailEmpleado")
    private HashSet<String> empleados;


    // Constructor completo
    public Empresa(String email, String nombre, String password, String direccion, String telefono, String descripcion, String sector) {
        super(email, nombre, password); // Llama al constructor de Usuario (que inicializa chatIds)
        this.direccion = direccion;
        this.telefono = telefono;
        this.sector = sector;
        this.descripcion = descripcion;
        this.empleados = new HashSet<>();
    }

    // Constructor para JAXB (inicializa las colecciones)
    public Empresa(){
        // Llama al constructor de Usuario (que inicializa chatIds)
        super();
        // Inicialización crucial para evitar NullPointerException
        this.empleados = new HashSet<>();
    }

    // --- Getters y Setters de Atributos Propios ---

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

    // --- Métodos de Gestión de Empleados ---

    public Set<String> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(HashSet<String> empleados) {
        this.empleados = empleados;
    }

    // NOTA: Se eliminan los getters/setters obsoletos de ArrayList<Chat>

    // C - CREATE
    public void addEmpleado(Empleado empleado) throws ElementoRepetido {
        // La comprobación de duplicados se hace con el email (que es la String en el HashSet)
        if (this.empleados.contains(empleado.getEmail())) {
            throw new ElementoRepetido("Ya existe un empleado con el email: " + empleado.getEmail());
        }
        // Agrega SOLO el email (la referencia)
        this.empleados.add(empleado.getEmail());
    }

    // R - READ (Referencia y Búsqueda en el gestor central)
    public Empleado buscarEmpleadoEnEmpresa(String emailBuscado) {
        if (this.empleados.contains(emailBuscado)) {
            Usuario usuarioEncontrado = UsuariosManager.getInstance().buscarUsuario(emailBuscado);
            if (usuarioEncontrado != null && usuarioEncontrado instanceof Empleado) {
                return (Empleado) usuarioEncontrado;
            }
        }
        return null;
    }

    // U - UPDATE (Delega la actualización al gestor central)
    public void updateEmpleado(Empleado empleadoActualizado) throws ElementoNoEncontrado {
        String emailBuscado = empleadoActualizado.getEmail();

        // 1. Verificar la referencia local (solo el email)
        if (!this.empleados.contains(emailBuscado)) {
            throw new ElementoNoEncontrado("No se puede actualizar. El email " + emailBuscado +
                    " no está registrado como empleado de esta empresa.");
        }

        // 2. Si la referencia existe, actualizamos el objeto completo en el gestor central
        try {
            UsuariosManager.getInstance().update(empleadoActualizado);
        } catch (ElementoNoEncontrado e) {
            // Se relanza si el gestor central no lo encuentra (inconsistencia de datos)
            throw new ElementoNoEncontrado("Error al actualizar: El objeto empleado no fue encontrado en el sistema central.");
        }
        // No se requiere modificar this.empleados ya que el email (referencia) no ha cambiado.
    }

    // D - DELETE
    public void removeEmpleado(String email) throws ElementoNoEncontrado {
        // Verificamos si la referencia existe localmente
        if (!this.empleados.contains(email)) {
            throw new ElementoNoEncontrado("No se puede eliminar. Empleado con email " + email + " no encontrado en la empresa.");
        }

        // El empleado debe ser eliminado del sistema central (UsuariosManager) también,
        // pero esa responsabilidad es mejor dejarla en el controlador o una capa de servicio.

        // 1. Elimina la referencia local
        this.empleados.remove(email);

        // 2. NOTA: Aquí se debería llamar a UsuariosManager.getInstance().remove(email)
        // para eliminar el objeto Usuario completo del sistema central.
        // Esto se hace fuera de la clase Empresa para mantener su responsabilidad limpia.
    }
}
