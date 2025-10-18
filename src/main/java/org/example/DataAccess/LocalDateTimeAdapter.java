package org.example.DataAccess;


import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    // Define el formato que usaremos en el XML (ej. 2025-10-18T17:41:18.123456789)
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime unmarshal(String v) throws Exception {
        // Convierte la String del XML a un objeto LocalDateTime
        return LocalDateTime.parse(v, formatter);
    }

    @Override
    public String marshal(LocalDateTime v) throws Exception {
        // Convierte el objeto LocalDateTime a una String para guardarla en el XML
        return v.format(formatter);
    }
}
