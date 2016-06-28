package dcs.edusat20;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideosPremiun extends AppCompatActivity {
    private VideoView videos;
    private String ruta;
    private boolean reproduciendo = true;
    private AssetFileDescriptor afd;
   private  MediaController mc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos_premiun);
        videos = (VideoView) findViewById(R.id.videoView_video);
      mc  = new MediaController(this);
        videos.setMediaController(mc);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ruta = getIntent().getExtras().getString("rutas");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       // getSupportActionBar().setTitle(getIntent().getExtras().getString("nombre"));
        getSupportActionBar().setTitle(getResources().getString(R.string.videos_trailers));
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        // String path = "android.resource://" + getPackageName() + "/" + R.raw.afrodita;

        videos.setVideoURI(Uri.parse(ruta));

        if (savedInstanceState != null) {
       //      mensaje("ENTRO");
            final int tiempo = savedInstanceState.getInt("TEMP_AC");
            videos.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.seekTo(tiempo+1);

                }
            });

        }
        videos.start();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //       mensaje("GRABANDO");
      //  mensaje("En el tiempo" + videos.getCurrentPosition());
        // Make sure to call the super method so that the states of our views are saved
        super.onSaveInstanceState(outState);
        outState.putInt("TEMP_AC", videos.getCurrentPosition());
        // Save our own state now
        //outState.putSerializable(STATE_ITEMS, mItems);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.playlist, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        String filePath = "";
        //= "file:///android_asset/PaginaWeb/daniel-ortiz2.webnode.cl/index.html";
        int id = item.getItemId();
        String path = "android.resource://" + getPackageName() + "/";// +// R.raw.afrodita;

/* getSupportActionBar().setTitle(getResources().getString(R.string.video_afrodita));
        switch (id) {
            case R.id.videos_afrodita:
                getSupportActionBar().setTitle(getResources().getString(R.string.video_afrodita));
                //     path = "android.resource://" + getPackageName() + "/" + R.raw.afrodita;
                break;
            case R.id.videos_hotlatino:
                getSupportActionBar().setTitle(getResources().getString(R.string.video_hotlatino));
                //       path = "android.resource://" + getPackageName() + "/" + R.raw.hotlatino;
                break;
            case R.id.videos_sexychannel:
                getSupportActionBar().setTitle(getResources().getString(R.string.video_sexychannel));
                //    path = "android.resource://" + getPackageName() + "/" + R.raw.sexychannel;
                break;
        }*/
   //     videos.setVideoURI(Uri.parse(path));
   //     videos.start();

        // Uri path = Uri.parse(filePath);
        //  videos.setVideoURI(path);
        // videos.start();


        return super.onOptionsItemSelected(item);
    }

    private void mensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}
