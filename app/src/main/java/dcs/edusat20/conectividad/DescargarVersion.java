package dcs.edusat20.conectividad;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by daniel on 01/04/2016.
 */
public class DescargarVersion extends AsyncTask<String, String, Void> {
    private String url;
    private Handler mHandler;
    Message msg;
    Bundle bundle;


    public DescargarVersion(String url, Handler mHandler, ProgressDialog progress) {
        this.url = url;
        this.mHandler = mHandler;
        //  this.progress

    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            URL pagina = new URL(url);
            msg = mHandler.obtainMessage(1);
            bundle = new Bundle();
            bundle.putString("result", "recibe url:" + url);
            msg.setData(bundle);
            mHandler.sendMessage(msg);
            URLConnection conection = pagina.openConnection();

            conection.connect();
            int lenghtOfFile = conection.getContentLength();
            msg = mHandler.obtainMessage(1);
            bundle = new Bundle();
            bundle.putString("result", "abrio conexion: " + lenghtOfFile);
            msg.setData(bundle);
            mHandler.sendMessage(msg);
            // input stream to read file - with 8k buffer

            InputStream input = new BufferedInputStream(pagina.openStream(), 8192);
            // Output stream to write file
            OutputStream output = new FileOutputStream("/sdcard/downloadedfile.apk");
            byte data[] = new byte[1024];

            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                msg = mHandler.obtainMessage(1);
                bundle = new Bundle();
                bundle.putString("result", "descargando");
                msg.setData(bundle);
                mHandler.sendMessage(msg);
                // writing data to file
                output.write(data, 0, count);
            }
            output.flush();

            output.close();
            input.close();
            // flushing output
            output.flush();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            msg = mHandler.obtainMessage(1);
            bundle = new Bundle();
            bundle.putString("result", e.toString());
            msg.setData(bundle);
            mHandler.sendMessage(msg);

        } finally {
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        msg = mHandler.obtainMessage(1);
        bundle = new Bundle();
        bundle.putString("result", "Enviando Datos");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

    }

    protected void onProgressUpdate(Void... values) {

        msg = mHandler.obtainMessage(1);
        bundle = new Bundle();
        bundle.putString("result", "Recorriendo");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        //salida.setText("Uptte");
    }

    @Override
    protected void onPostExecute(Void result) {
        msg = mHandler.obtainMessage(1);
        bundle = new Bundle();
        bundle.putString("result", "Termino");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    private void push_mensaje(String mensaje) {

        msg = mHandler.obtainMessage(1);
        bundle = new Bundle();
        bundle.putString("result", mensaje);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }


}
