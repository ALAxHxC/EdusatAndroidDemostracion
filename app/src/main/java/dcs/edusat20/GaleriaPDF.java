package dcs.edusat20;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;

public class GaleriaPDF extends AppCompatActivity {
    ImageSwitcher imageSwitcher;
    ImageView vista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_pdf);
        vista = (ImageView) findViewById(R.id.imageView);

    }

    //mensajes
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

}
