package org.example.Model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Adjunto {

    @XmlElement(name = "NombreOriginal")
    private String nombreOriginal; // Nombre que le dio el usuario

    @XmlElement(name = "RutaGuardada")
    private String rutaGuardada;   // Ruta dentro de la carpeta media/

    @XmlElement(name = "TamanoBytes")
    private long tamanoBytes;

    @XmlElement(name = "TipoMime")
    private String tipo; // Ej: image/png, application/pdf

    // Constructor para JAXB
    public Adjunto() {}

    public Adjunto(String nombreOriginal, String rutaGuardada, long tamanoBytes, String tipoMime) {
        this.nombreOriginal = nombreOriginal;
        this.rutaGuardada = rutaGuardada;
        this.tamanoBytes = tamanoBytes;
        this.tipo = tipoMime;
    }


    public String getRutaGuardada() {
        return rutaGuardada;
    }

    public String getNombreOriginal() {
        return nombreOriginal;
    }

    public void setNombreOriginal(String nombreOriginal) {
        this.nombreOriginal = nombreOriginal;
    }

    public void setRutaGuardada(String rutaGuardada) {
        this.rutaGuardada = rutaGuardada;
    }

    public long getTamanoBytes() {
        return tamanoBytes;
    }

    public void setTamanoBytes(long tamanoBytes) {
        this.tamanoBytes = tamanoBytes;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipoMime(String tipoMime) {
        this.tipo = tipoMime;
    }
}