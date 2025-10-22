package org.example.Model;

import org.example.DataAccess.LocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
public class Mensaje {
    @XmlElement(name = "Contenido")
    private String contenido;

    @XmlElement(name = "RemitenteEmail")
    private String remitenteEmail; // Usamos el email para identificar al remitente

    @XmlElement(name = "Adjunto")
    private Adjunto adjunto;

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


    // Constructor para mensajes con adjunto
    public Mensaje(String contenido, String remitenteEmail, Adjunto adjunto) {
        this.contenido = contenido;
        this.remitenteEmail = remitenteEmail;
        this.fechaHora = LocalDateTime.now();
        this.adjunto = adjunto;
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

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setRemitenteEmail(String remitenteEmail) {
        this.remitenteEmail = remitenteEmail;
    }

    public Adjunto getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(Adjunto adjunto) {
        this.adjunto = adjunto;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
