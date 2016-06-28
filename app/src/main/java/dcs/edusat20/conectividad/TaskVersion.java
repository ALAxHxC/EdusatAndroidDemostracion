package dcs.edusat20.conectividad;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by daniel on 05/04/2016.
 */
public class TaskVersion extends AsyncTask<String, Void, Void> {
    private String nombre, cedula;
    private LecturaXML lectura;
    private ObtenerVersion version;
    private Handler mHandler;
    private Message msg;
    private Bundle bundle;
    public final int MENSAJE = 1;
    public final int MENSAJE_PROGRESO = 2;
    public final int MENSAJE_ACTUALIZANDO = 3;
    public TaskVersion(String nombre, String cedula,Handler mHandler) {
        this.nombre = nombre;
        this.cedula = cedula;
        version=new ObtenerVersion();
        this.mHandler=mHandler;
    }

    @Override
    protected void onPreExecute() {
        msg = mHandler.obtainMessage(MENSAJE_PROGRESO);
        bundle = new Bundle();
        bundle.putString("result", "Comprobando version");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

    }

    @Override
    protected void onPostExecute(Void result) {
        msg = mHandler.obtainMessage(MENSAJE_PROGRESO);
        lectura =new LecturaXML(version.getRespuesta());
        bundle = new Bundle();
        try {
          bundle.putDouble("respuesta",Double.parseDouble(lectura.resultado()));
            bundle.putString("result","termino");
        } catch (Exception e) {
            bundle.putString("result", "ERROR:"+ e.toString());
        }
        msg.setData(bundle);
        mHandler.sendMessage(msg);

    }

    @Override
    protected Void doInBackground(String... params) {
        version.HttpRequestLectura(nombre,cedula);
        return null;
    }
}
