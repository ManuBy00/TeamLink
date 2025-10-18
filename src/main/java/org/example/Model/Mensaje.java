package org.example.Model;

import org.example.DataAccess.LocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

public class Mensaje {
    @XmlElement(name = "Contenido")
    private String contenido;

    @XmlElement(name = "RemitenteEmail")
    private String remitenteEmail; // Usamos el email para identificar al remitente

    @XmlElement(name = "FechaHora")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime fechaHora;


    // Constructor para JAXB (necesario para la deserialización)
    public Mensaje() {
    }

    public Mensaje(String contenido, String remitenteEmail) {
        this.contenido = contenido;
        this.remitenteEmail = remitenteEmail;
        this.fechaHora = LocalDateTime.now(); // Establece la hora actual al crearse
    }

    // Getters y Setters
    public String getContenido() {
        return contenido;
    }

    public String getRemitenteEmail() {
        return remitenteEmail;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }


}
