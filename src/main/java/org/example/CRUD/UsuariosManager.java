package org.example.CRUD;

import org.example.Exceptions.ElementoNoEncontrado;
import org.example.Exceptions.ElementoRepetido;
import org.example.Model.Usuario;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;


@XmlRootElement(name = "ListaUsuarios")
public class UsuariosManager implements CRUD<Usuario>{
    @XmlElement(name="Usuario")
    private HashSet<Usuario> usuariosList;
    static UsuariosManager instance;


    private UsuariosManager() {
        this.usuariosList = new HashSet<>();
    }

    public static UsuariosManager getInstance(){
        if (instance==null){
            instance = new UsuariosManager();
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
}
