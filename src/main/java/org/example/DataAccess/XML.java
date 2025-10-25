
package org.example.DataAccess;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XML {

    /**
     * Serializa un objeto Java genérico a un archivo XML utilizando JAXB (Marshalling).
     *
     * Este método es fundamental para la persistencia offline del proyecto.
     * Configura la salida del XML para ser formateada y utiliza codificación UTF-8.
     *
     * @param objeto El objeto Java (Manager) a serializar.
     * @param fileName El nombre del archivo XML de destino.
     * @return true si la serialización fue exitosa.
     * @throws RuntimeException Si ocurre un JAXBException durante la serialización.
     */
    public static <T> boolean writeXML(T objeto, String fileName) {
        boolean result = false;
        try {
            //Paso 1: Crear el contexto de JaxB para la clase que queremos serializar
            JAXBContext context = JAXBContext.newInstance(objeto.getClass());

            //Paso 2: proceso Marshalling: convertir objeto en XML
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(objeto,new File(fileName));
            result = true;

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * Deserializa un archivo XML en una instancia de la clase especificada (Unmarshalling).
     *
     * Este método se utiliza en los Managers (Singleton) para cargar el estado persistente
     * de la aplicación desde el disco.
     *
     * @param clazz La clase de tipo T a la que se desea deserializar el XML.
     * @param fileName El nombre del archivo XML de origen.
     * @return El objeto deserializado de tipo T, o null si ocurre un JAXBException.
     */
    public static <T> T readXML(Class<T> clazz, String fileName) {
        T objeto = null;
        try {
            // Creamos un contexto JAXB para la clase que queremos deserializar
            JAXBContext context = JAXBContext.newInstance(clazz);

            // Creamos un Unmarshaller
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Deserializamos el XML en el objeto
            objeto = (T) unmarshaller.unmarshal(new File(fileName));

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return objeto;
    }
}

