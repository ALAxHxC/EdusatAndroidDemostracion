package dcs.edusat20.conectividad;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by daniel on 05/02/2016.
 */
public class LecturaXML {
    //procesa el web service
    String respuesta;

    public LecturaXML(String respuesta) {
        this.respuesta = respuesta;
    }

    public String resultado() throws ParserConfigurationException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new ByteArrayInputStream(respuesta.getBytes("UTF-8")));

            //Respuesta
            NodeList list = doc.getElementsByTagName("return");
            if (list.getLength() > 0) {
                return list.item(0).getTextContent();
            } else {
                return "ERROR";
            }
        } catch (SAXException e) {

            return "ERROR:"+e.getMessage();
        } catch (IOException e) {
            return "ERROR:"+e.getMessage();

        }
    }
}
