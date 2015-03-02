package com.qr.gym.hmunoz.qrgym;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


public class ActivityDetalle extends ActionBarActivity {

    private String param;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    VideoView vv;
    private int position = 0;
    static final String TAG = ActivityDetalle.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_detalle);
        //QrGym://com.qr.gym.hmunoz.qrgym?param=1
        if (getIntent().getData() != null) {
            param = getIntent().getData().getQueryParameter("param");
            Toast.makeText(this, param, Toast.LENGTH_SHORT).show();
            Log.d("Detalle", param);
        } else if (getIntent().getExtras() != null) {
            param = getIntent().getExtras().getString("param");

        }
        setTitle(param);

        if (mediaControls == null) {
            mediaControls = new MediaController(ActivityDetalle.this);
        }
        // Create a progressbar
        progressDialog = new ProgressDialog(ActivityDetalle.this);
        // Set progressbar title
        progressDialog.setTitle(param);
        // Set progressbar message
        progressDialog.setMessage("Loading...");

        progressDialog.setCancelable(false);
        // Show progressbar
        progressDialog.show();


        vv = (VideoView) this.findViewById(R.id.videoView);




        try {
            vv.setMediaController(mediaControls);
            String paramVideo = "video";
            String uri = "android.resource://" + getPackageName() + "/raw/" + paramVideo;
            vv.setVideoURI(Uri.parse(uri));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        vv.requestFocus();
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
                vv.seekTo(position);
                if (position == 0) {
                    vv.start();
                } else {
                    vv.pause();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_detalle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onShare(View v) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) +  "://" + getPackageName() + "?param=" + param);
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this site!");
            startActivity(Intent.createChooser(intent, "Share"));
            Log.d(TAG,getString(R.string.app_name) +  "://" + getPackageName() + "?param=" + param);
    }
}
