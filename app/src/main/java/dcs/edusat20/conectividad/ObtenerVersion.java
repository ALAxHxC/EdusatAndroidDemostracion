package dcs.edusat20.conectividad;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by daniel on 05/04/2016.
 */
public class ObtenerVersion {
    //envia la peticion HTTP al web service
    private final static String wdlsURL = "http://edusat.co/webservices/wsdl_test.php?wsdl";
    private final static String SOAPAction = "urn:Sourena.Version";
    public static String respuesta = "nada";
    private  String peticion(String nombre,String cedula) {
        String SOAP
                ="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn: \">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <urn:Version>\n" +
                "         <username>"+nombre+"</username>\n" +
                "         <age>"+cedula+"</age>\n" +
                "      </urn:Version>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        // System.out.println(SOAP);
        return SOAP;
    }
    public void HttpRequestLectura(String nombre,String cedula) {
        try {
            String request = peticion(nombre,cedula);

            URL url = new URL(wdlsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //CONTENIDO
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buffer = new byte[request.length()];
            buffer = request.getBytes();
            bout.write(buffer);
            byte[] b = bout.toByteArray();
            urlConnection.setRequestProperty("Content-Length",
                    String.valueOf(b.length));
            //
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            urlConnection.setRequestProperty("SOAPAction", SOAPAction);
//salida
            OutputStream out = urlConnection.getOutputStream();
//Write the content of the request to the outputstream of the HTTP Connection.
            out.write(b);
            out.close();
            //lectura

            InputStreamReader isr
                    = new InputStreamReader(urlConnection.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            StringBuilder salida = new StringBuilder("");
            String insalida;
            while ((insalida = in.readLine()) != null) {
                salida = salida.append(insalida);
            }
            bout.close();

            respuesta = salida.toString();
        } catch (Exception ex) {
            respuesta = "ERROR:"+ex.getLocalizedMessage();
        }
    }

    public  String getRespuesta() {
        return respuesta;
    }
}
