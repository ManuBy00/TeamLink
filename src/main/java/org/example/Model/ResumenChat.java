package org.example.Model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResumenChat {

    private  Chat chat; // Almacena el objeto Chat completo
    private  long totalMensajes;
    private  Map<String, Long> mensajesPorUsuario; // Usuario, número de mensajes
    private  List<Map.Entry<String, Long>> topPalabras; //mesaje, numero de veces que se repite
    private long mensajesAdjuntos;

    /**
     * Constructor que recibe el objeto Chat y los resultados del análisis.
     */
    public ResumenChat(Chat chat, long totalMensajes, Map<String, Long> mensajesPorUsuario, List<Map.Entry<String, Long>> topPalabras, long mensajesAdjuntos) {
        this.chat = chat;
        this.totalMensajes = totalMensajes;
        this.mensajesPorUsuario = mensajesPorUsuario;
        this.topPalabras = topPalabras;
        this.mensajesAdjuntos = mensajesAdjuntos;

    }

    // --- Getters ---
    public Chat getChat() {
        return chat;
    }

    public long getTotalMensajes() {
        return totalMensajes;
    }

    public Map<String, Long> getMensajesPorUsuario() {
        return mensajesPorUsuario;
    }

    public List<Map.Entry<String, Long>> getTopPalabras() {
        return topPalabras;
    }

    public long getMensajesAdjuntos() {
        return mensajesAdjuntos;
    }

    public void setMensajesAdjuntos(int mensajesAdjuntos) {
        this.mensajesAdjuntos = mensajesAdjuntos;
    }

    /**
     * Método para formatear el objeto ResumenChat en una cadena de texto (String).
     */
    @Override
    public String toString() {
        StringBuilder informe = new StringBuilder();

        // Usamos el ID del objeto Chat para el encabezado
        informe.append("--- INFORME ESTADÍSTICO CHAT ID: ").append(chat.getChatID()).append(" ---\n\n");

        // 1. Total de Mensajes
        informe.append("1. TOTAL DE MENSAJES: ").append(totalMensajes).append("\n\n");

        // 2. Conteo por Usuario
        informe.append("2. MENSAJES ENVIADOS POR USUARIO:\n");
        mensajesPorUsuario.forEach((email, count) ->
                informe.append("   - ").append(email).append(": ").append(count).append(" mensajes\n")
        );
        informe.append("\n");

        // 3. Top Palabras
        informe.append("3. TOP ").append(topPalabras.size()).append(" PALABRAS MÁS COMUNES:\n");
        topPalabras.forEach(entry ->
                informe.append("   - ").append(entry.getKey()).append(" (").append(entry.getValue()).append(" veces)\n")
        );

        informe.append("4. MENSAJES CON ARCHIVO ADJUNTO: ").append(mensajesAdjuntos).append("\n");

        return informe.toString();
    }
}