package dcs.edusat20;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import dcs.edusat20.conectividad.TaskVersion;

public class Inicio extends AppCompatActivity {
    private ProgressDialog progress;
    private WebView visor;
    private double Actualversion;
    private TaskVersion version;
    private String ruta_local;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inicio);

        visor = (WebView) findViewById(R.id.webViewVisor);
        progress = new ProgressDialog(this);

        ajustes();

        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            getSupportActionBar().setIcon(R.drawable.ic_launcher);
            getSupportActionBar().setTitle(getSupportActionBar().getTitle() + " V" + getResources().getString(R.string.version));
            //accede al html principal
            String filePath = "file:///android_asset/index.html";
            ruta_local = getApplicationContext().getResources().getString(R.string.ruta_local);
            file = new File(ruta_local);
            if (savedInstanceState != null) {
                //en caso que el activity se recargara , guarda la url y la obtiene
                filePath = savedInstanceState.getString("URL_ACTUAL");

            } else {
                //valida la version de la app
                validarVersion();
            }
            //carga segun el archui
            visor.loadUrl(filePath);

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            Toast.makeText(this, e1.toString(), Toast.LENGTH_SHORT).show();
            e1.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // Make sure to call the super method so that the states of our views are saved
        super.onSaveInstanceState(outState);
        //guarda la pagina actual en caso de que el activity se recargue
        outState.putString("URL_ACTUAL", visor.getUrl());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_videos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String ruta = "";
        String nombre = " ";
        switch (id) {

            case R.id.video_edusat:
                //envia la ruta del video
                ruta = "android.resource://" + getPackageName() + "/" + R.raw.caso_exito_edu;
                nombre = getResources().getString(R.string.Caso_de_exito);
                break;
            case R.id.inicio:
                visor.loadUrl("file:///android_asset/index.html");
                return super.onOptionsItemSelected(item);

        }
        Intent intent = new Intent(this, VideosPremiun.class);
        intent.putExtra("rutas", ruta);
        intent.putExtra("nombre", nombre);
        startActivity(intent);


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (visor.canGoBack()) {
            visor.goBack();
        } else if (!visor.getUrl().endsWith("index.html")) {
            visor.loadUrl("file:///android_asset/index.html");

        } else
            super.onBackPressed();
    }


    private void ajustes() {
        visor.addJavascriptInterface(new WebAppInterface(this), "Android");
        visor.setWebViewClient(new MyClienteWeb(this));
        visor.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = visor.getSettings();
        webSettings.setJavaScriptEnabled(true);
        visor.getSettings().setBuiltInZoomControls(true);
        visor.getSettings().setAllowFileAccess(true);
        visor.getSettings().setLoadWithOverviewMode(true);
        visor.getSettings().setUseWideViewPort(true);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        visor.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);


    }

    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast + " cosas", Toast.LENGTH_SHORT).show();


            visor.post(new Runnable() {
                @Override
                public void run() {
                    visor.setEnabled(false);

                }
            });
        }

    }


    class MyClienteWeb extends WebViewClient {

        Context context;

        public MyClienteWeb(Context context) {
            this.context = context;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //  view.loadUrl("javascript:window.android.onUrlChange(window.location.href);");
            //   Toast.makeText(context, "entro finish", Toast.LENGTH_SHORT).show();
            if (url.contains("pdf")) {
                mensaje("Cargando ....");
            }
        }

        ;


        class MyJavaScriptInterface {
            @JavascriptInterface
            public void onUrlChange(String url) {
                Log.d("hydrated", "onUrlChange" + url);
            }

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub

            super.onPageStarted(view, url, favicon);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.contains("edusat.co")) {
                // mensaje(url);
                return false;
            }

            return false;


        }
    }

    private final Handler mHandlerWS = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //       mostrarcasoderrror("llego", msg.getData().getString("result"));
            if (msg.getData().getString("result").startsWith("ERROR")) {
                mostrarcasoderrror("NO SE ENCUENTRA SERVIDOR", "LA APLICACIÓN SEGUIRA EN FUNCIONAMIENTO PERO SIN ACTUALIZACIÓN");
            }
            // mensaje(msg.getData().getString("result"));
            if (msg.getData().getString("result").equalsIgnoreCase("Termino")) {
                try {

                    Actualversion = msg.getData().getDouble("respuesta");
                    comprobarServsion();
                } catch (Exception ex) {
                    mostrarcasoderrror("NO SE ENCUENTRA SERVIDOR", "LA APLICACIÓN SEGUIRA EN FUNCIONAMIENTO PERO SIN ACTUALIZACIÓN");
                }
            }


        }
    };

    private void validarVersion() {
        try {
            version = new TaskVersion("cedula", "nombre", mHandlerWS);
            version.execute();
        } catch (Exception ex) {
            mostrarcasoderrror("ERROR", ex.toString());
        }
    }

    private void comprobarServsion() {
//comprueba version
        if (Actualversion > Double.parseDouble(getResources().getString(R.string.version))) {
            mensaje(getApplicationContext().getResources().getString(R.string.iniciando_download));
            DescargarVersion();
        } else {
            if (file.exists()) {
                file.delete();
            }
            mostrarcasoderrror("INFORMACIÓN", "Esta actualizado");

        }


    }

    private void DescargarVersion() {

        realizarDescarga();

    }

    private void instalar(File file) {
        try {
//instala el nuevo apk
            mensaje(Uri.fromFile(file).getEncodedPath());//File existis
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String ext = file.getName().substring(file.getName().indexOf(".") + 1);
            String type = mime.getMimeTypeFromExtension(ext);
            intent.setDataAndType(Uri.fromFile(file), type);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarcasoderrror(e.toString(), e.toString());
        }

    }

    public void mostrarcasoderrror(String titulo, String cuerpo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo);
        builder.setMessage(cuerpo);
        builder.setPositiveButton("OK", null);
        builder.create();
        builder.show();

    }

    public void mensaje(String men) {
        Toast.makeText(this, men, Toast.LENGTH_SHORT).show();
    }
//DOWNLOAD MODULE

    private ProgressDialog prgDialog;

    public static final int progress_bar_type = 0;

    private String file_url; //http://edusat.co/webservices/edu.apk"";

    public void realizarDescarga() {
        //descarga la apk y y la instala
        file_url = getApplicationContext().getResources().getString(R.string.url);

        // Check if the Music file already exists
        if (file.exists()) {

            file.delete();
            try {
                //guarda el archvio
                boolean bandera = file.createNewFile();
                Toast.makeText(getApplicationContext(), "PREPARANDO ACTUALIZACIÓN", Toast.LENGTH_SHORT).show();
                new DownloadMusicfromInternet().execute(file_url);
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "IMPOSIBLE CREAR", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Descargando", Toast.LENGTH_LONG).show();
            // Trigger Async Task (onPreExecute method)
            new DownloadMusicfromInternet().execute(file_url);
        }
    }

    // Show Dialog Box with Progress bar
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                prgDialog = new ProgressDialog(this);
                prgDialog.setMessage(getApplicationContext().getResources().getString(R.string.donwload_version));
                prgDialog.setIndeterminate(false);
                prgDialog.setMax(100);
                prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                prgDialog.setCancelable(false);
                prgDialog.show();
                return prgDialog;
            default:
                return null;
        }
    }

    // Async Task Class
    class DownloadMusicfromInternet extends AsyncTask<String, String, String> {

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            showDialog(progress_bar_type);
        }

        // Download Music File from Internet
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                //Realiza conectividad
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // Get Music file length
                int lenghtOfFile = conection.getContentLength();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 10 * 1024);
                // Output stream to write file in SD card
                OutputStream output = new FileOutputStream(ruta_local);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publish the progress which triggers onProgressUpdate method
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // Write data to file
                    output.write(data, 0, count);
                }
                // Flush output
                output.flush();
                // Close streams
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            prgDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            dismissDialog(progress_bar_type);
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.Descargo), Toast.LENGTH_LONG).show();

            verficacion();
        }
    }

    // Play Music
    protected void verficacion() {
        File file = new File(ruta_local);
        Toast.makeText(getApplicationContext(), file.exists() + "", Toast.LENGTH_SHORT).show();
        instalar(file);
    }

}
