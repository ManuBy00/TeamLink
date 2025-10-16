package org.example.Model;

import org.example.CRUD.UsuariosManager;
import org.example.Exceptions.ElementoNoEncontrado;
import org.example.Exceptions.ElementoRepetido;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashSet;

@XmlRootElement(name="Empresa")
@XmlAccessorType(XmlAccessType.FIELD)
public class Empresa extends Usuario{
    private  String direccion;
    private String telefono;
    private String descripcion;
    private String sector;
    private HashSet<String> empleados;
    private ArrayList<Chat> chats;

    public Empresa(String email, String nombre, String password, String direccion, String telefono, String descripcion, String sector) {
        super(email, nombre, password);
        this.direccion = direccion;
        this.telefono = telefono;
        this.sector = sector;
        this.descripcion = descripcion;

        this.empleados = new HashSet<>();
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

    public HashSet<String> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(HashSet<String> empleados) {
        this.empleados = empleados;
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
    }

    public void addEmpleado(Empleado empleado) throws ElementoRepetido {
        // HashSet usa el método equals() (basado en el email) para verificar duplicados.
        if (this.empleados.contains(empleado)) {
            throw new ElementoRepetido("Ya existe un empleado con el email: " + empleado.getEmail());
        }
        this.empleados.add(empleado.getEmail());
    }

    /**
     * R - READ: Busca y devuelve un empleado por su email.
     * @param emailBuscado El email del empleado a buscar.
     * @return El objeto Empleado encontrado, o null si no existe.
     */
    public Empleado buscarEmpleadoEnEmpresa(String emailBuscado) {
        if (this.empleados.contains(emailBuscado)) {
            Usuario em = UsuariosManager.getInstance().buscarUsuario(emailBuscado);
            if (em != null && em instanceof Empleado) {
                return (Empleado) em;
            }
        }
        return null;
    }

        /**
         * U - UPDATE: Actualiza la información de un empleado.
         * 1. Verifica que la referencia del empleado exista en la empresa.
         * 2. Delega la actualización del objeto completo al UsuariosManager.
         * @param empleadoActualizado El objeto Empleado con la nueva información.
         * @throws ElementoNoEncontrado si la referencia del empleado no se encuentra en la empresa o en el gestor central.
         */
        public void updateEmpleado(Empleado empleadoActualizado) throws ElementoNoEncontrado {
            String emailBuscado = empleadoActualizado.getEmail();

            // 1. Verificar la referencia local (solo el email)
            if (!this.empleados.contains(emailBuscado)) {
                // Si el email no está en nuestra lista de referencias, el empleado no pertenece a la empresa
                throw new ElementoNoEncontrado("No se puede actualizar. El email " + emailBuscado +
                        " no está registrado como empleado de esta empresa.");
            }

            // 2. Si la referencia existe, actualizamos el objeto completo en el gestor central
            try {
                UsuariosManager.getInstance().update(empleadoActualizado);
            } catch (ElementoNoEncontrado e) {
                // Capturamos la excepción del gestor central y la relanzamos
                throw new ElementoNoEncontrado("Error al actualizar: El objeto empleado no fue encontrado en el sistema central.");
            }
        }

    /**
     * D - DELETE: Elimina un empleado por su email.
     * @param email El email del empleado a eliminar.
     * @throws ElementoNoEncontrado si el empleado no se encuentra.
     */
    public void removeEmpleado(String email) throws ElementoNoEncontrado {
        Empleado empleadoAEliminar = buscarEmpleadoEnEmpresa(email);

        if (empleadoAEliminar == null) {
            throw new ElementoNoEncontrado("No se puede eliminar. Empleado con email " + email + " no encontrado.");
        }

        this.empleados.remove(empleadoAEliminar);
    }
}
