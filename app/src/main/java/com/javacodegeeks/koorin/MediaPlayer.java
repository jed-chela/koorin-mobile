package com.javacodegeeks.koorin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MediaPlayer extends Activity {

	private android.media.MediaPlayer mediaPlayer;
	public TextView songName, duration;
	private double timeElapsed = 0, finalTime = 0;
	private int forwardTime = 2000, backwardTime = 2000;
	private Handler durationHandler = new Handler();
	private SeekBar seekbar;
    public String song_name = "L'abe Igi Orombo";
    public String song_file_name = "labe_igi.mp3";

    Runnable longRunningTask;
    Future longRunningTaskFuture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//set the layout of the Activity
		setContentView(R.layout.activity_main);

        if (getIntent().getStringExtra("song_file_name") != null ) {
            song_file_name = getIntent().getStringExtra("song_file_name");
        }

        if (getIntent().getStringExtra("song_name") != null ) {
            song_name = getIntent().getStringExtra("song_name");
        }

		//initialize views
		initializeViews();
	}
	
	public void initializeViews(){
        int resid = 0;

        if (song_file_name.equals("labe_igi.mp3")){
            resid = R.raw.labe_igi;
        }else if(song_file_name.equals("omo_pupa.mp3")){
            resid = R.raw.omo_pupa;
        }

		songName = (TextView) findViewById(R.id.songName);
        stopPlaying();
		mediaPlayer = android.media.MediaPlayer.create(this, resid);
		finalTime = mediaPlayer.getDuration();
		duration = (TextView) findViewById(R.id.songDuration);
		seekbar = (SeekBar) findViewById(R.id.seekBar);
		songName.setText(song_name);
		seekbar.setMax((int) finalTime);
		seekbar.setClickable(false);
	}

    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

	// play mp3 Song
	public void play(View view) {
		mediaPlayer.start();
		timeElapsed = mediaPlayer.getCurrentPosition();
		seekbar.setProgress((int) timeElapsed);
	//	durationHandler.postDelayed(updateSeekBarTime, 100);

        ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();
        longRunningTask = new Runnable() {
            @Override
            public void run() {
                //get current position
                timeElapsed = mediaPlayer.getCurrentPosition();
                //set seekbar progress
                seekbar.setProgress((int) timeElapsed);
                //set time remaing
                double timeRemaining = finalTime - timeElapsed;
                duration.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));

                //repeat yourself that again in 100 miliseconds
                durationHandler.postDelayed(this, 100);
            }
        };

        // submit task to threadpool:
        longRunningTaskFuture = threadPoolExecutor.submit(longRunningTask);

	}

	//handler to change seekBarTime
	private Runnable updateSeekBarTime = new Runnable() {
		public void run() {

		}
	};

	// pause mp3 Song
	public void pause(View view) {
		mediaPlayer.pause();
	}

	// go forward at forwardTime seconds
	public void forward(View view) {
		//check if we can go forward at forwardTime seconds before Song endes
		if ((timeElapsed + forwardTime) <= finalTime) {
			timeElapsed = timeElapsed + forwardTime;

			//seek to the exact second of the track
			mediaPlayer.seekTo((int) timeElapsed);
		}
	}

	// go backwards at backwardTime seconds
	public void rewind(View view) {
		//check if we can go back at backwardTime seconds after Song starts
		if ((timeElapsed - backwardTime) > 0) {
			timeElapsed = timeElapsed - backwardTime;
			
			//seek to the exact second of the track
			mediaPlayer.seekTo((int) timeElapsed);
		}
	}


    @Override
    public void onBackPressed() {
        stopPlaying();
        Intent i = new Intent(this, SongsList.class);
    //    i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        // At some point in the future, if you want to kill the task:
        if(longRunningTaskFuture != null) {
            longRunningTaskFuture.cancel(true);
        }
        startActivity(i);
    //    finish();
    }

}