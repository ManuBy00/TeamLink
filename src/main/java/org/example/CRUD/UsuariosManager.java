package org.example.CRUD;

import org.example.DataAccess.XML;
import org.example.Exceptions.ElementoNoEncontrado;
import org.example.Exceptions.ElementoRepetido;
import org.example.Model.Empleado;
import org.example.Model.Usuario;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@XmlRootElement(name = "ListaUsuarios")
public class UsuariosManager implements CRUD<Usuario>{
    @XmlElement(name="Usuario")
    private HashSet<Usuario> usuariosList;
    static UsuariosManager instance;
    private static final String FILE_NAME = "Usuarios.XML"; // Nombre del archivo XML


    private UsuariosManager() {
        this.usuariosList = new HashSet<>();
    }

    public static UsuariosManager getInstance(){
        if (instance==null){
            instance = XML.readXML(UsuariosManager.class, FILE_NAME);
            //si la lectura falla o el archivo está vacío, crea una instancia desde cero.
            if (instance == null){
                System.out.println("ADVERTENCIA: Archivo " + FILE_NAME + " no encontrado o vacío. Creando nuevo UsuariosManager.");
                instance = new UsuariosManager();
            }
        }
        return instance;
    }

    public HashSet<Usuario> getUsuariosList() {
        return usuariosList;
    }


    @Override
    public void add(Usuario usuario) throws ElementoRepetido{
        if (!usuariosList.add(usuario)){
            throw new ElementoRepetido("El correo introducido ya está registrado");
        }
    }

    @Override
    public void update(Usuario usuarioNuevo) throws ElementoNoEncontrado{
        Usuario usuarioAntiguo = buscarUsuario(usuarioNuevo.getEmail());
        if (usuarioAntiguo == null){
            throw new ElementoNoEncontrado("Usuario no encontrado");
        }else {
            usuariosList.remove(usuarioAntiguo);
            usuariosList.add(usuarioNuevo);
        }
    }

    @Override
    public void remove(String email) throws ElementoNoEncontrado{
        Usuario usuario = buscarUsuario(email);
        if (!usuariosList.remove(usuario)){
            throw new ElementoNoEncontrado("El usuario introducido no está registrado");
        }

    }

    @Override
    public void mostrar(Usuario elemento) {

    }

    @Override
    public void cargarXML(String fileName) {

    }

    public Usuario buscarUsuario(String email){
        Usuario usuarioEncontrado = null;
        for (Usuario usuario : usuariosList){
            if (email.equals(usuario.getEmail())){
                usuarioEncontrado = usuario;
            }
        }
        return usuarioEncontrado;
    }

    /**
     * Busca y devuelve una lista de objetos Empleado que pertenecen a una empresa específica.
     * @param emailEmpresa El nombre de la empresa a la que deben pertenecer los empleados.
     * @return Una List<Empleado> con los empleados encontrados. Nunca devuelve null.
     */
    public List<Empleado> buscarEmpleadosPorEmpresa(String emailEmpresa) {

        // 1. Verificamos si la lista de usuarios está inicializada antes de usar el stream.
        if (this.usuariosList == null) {
            return List.of(); // Devuelve una lista inmutable vacía si la lista principal es null
        }else{
            List empleados = this.usuariosList.stream()
                    .filter(u -> u instanceof Empleado) // Filtra solo objetos de tipo Empleado
                    .map(u -> (Empleado) u)           // Hace el casting seguro a Empleado
                    .filter(e -> e.getEmpresa().equals(emailEmpresa)) // Filtra por el nombre de la empresa
                    .collect(Collectors.toList()); // Recolecta el resultado en una List<Empleado>
            return empleados;
        }
    }
}
