package com.example.apssdc.exoplayerexample;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import java.net.URI;

public class MainActivity extends AppCompatActivity {

    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer exoPlayer;
    long currentposition;
    boolean playwhenready;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState==null) {
            startPlayer();
        }
        else{
            currentposition=savedInstanceState.getLong("currentposition");
            playwhenready=savedInstanceState.getBoolean("playwhenready");
            //exoPlayer.seekTo(currentposition);
            //exoPlayer.setPlayWhenReady(playwhenready);
        }

    }
    public void startPlayer(){

        simpleExoPlayerView=findViewById(R.id.simpleexoplayerview);
        exoPlayer=ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this),
                new DefaultTrackSelector(),new DefaultLoadControl());
        Uri videouri=Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        RenderersFactory renderersFactory=new DefaultRenderersFactory(this);
        TrackSelector selector=new DefaultTrackSelector();
        LoadControl control=new DefaultLoadControl();
        String user_agent=Util.getUserAgent(this,"ExoPlayerExample");
        MediaSource mediaSource=new ExtractorMediaSource(videouri,
                new DefaultDataSourceFactory(this, user_agent),
                new DefaultExtractorsFactory(),null,null);
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);
        simpleExoPlayerView.setPlayer(exoPlayer);

    }

    public void stopPlayer(){
        if (exoPlayer!=null){
            currentposition=exoPlayer.getCurrentPosition();
            exoPlayer.release();
            exoPlayer.stop();
            exoPlayer=null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Util.SDK_INT<=23){
            startPlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(Util.SDK_INT<=23) {
            stopPlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(Util.SDK_INT>23) {
            stopPlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Util.SDK_INT<=23) {
            startPlayer();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlayer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
            outState.putLong("currentposition", exoPlayer.getCurrentPosition());
            outState.putBoolean("playwhenready", exoPlayer.getPlayWhenReady());
    }

  /*  @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            savedInstanceState.putLong("currentposition", exoPlayer.getCurrentPosition());
            savedInstanceState.putBoolean("playwhenready", exoPlayer.getPlayWhenReady());
        }
    }*/
}
