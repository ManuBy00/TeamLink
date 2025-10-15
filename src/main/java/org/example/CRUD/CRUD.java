package org.example.CRUD;

import org.example.Exceptions.ElementoNoEncontrado;
import org.example.Exceptions.ElementoRepetido;

public interface CRUD <T>{

    void add(T elemento) throws ElementoRepetido;

    void update(T elemento) throws ElementoNoEncontrado;

    void remove(String id) throws ElementoNoEncontrado;

    void mostrar(T elemento);

    void cargarXML(String fileName);
}
