
package org.example.DataAccess;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XML {
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

