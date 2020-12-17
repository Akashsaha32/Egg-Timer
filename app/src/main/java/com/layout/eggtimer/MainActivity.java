package com.layout.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int activeCountDown = 0;
    SeekBar timerSeekbar;
    SeekBar soundSeekbar;
    TextView timerView;
    Button buttonChange;
    MediaPlayer mplayer;
    AudioManager sound;
    CountDownTimer timer;
    //*****************************************
    String makingTwoDigit(int num){
        //this function return a number in string and it has at least 2 digit
        String number = "";
        if(num == 0){
            number = "00";
        }
        else if(num != 0 && num <10){
            number = "0"+Integer.toString(num);
        }else {
            number = Integer.toString(num);
        }
        return number;
    }
    //***************************************************
    public void update(int secondLeft){
        int minutes = (int) secondLeft/60;
        int second = secondLeft - (minutes*60);
        timerView.setText(makingTwoDigit(minutes)+" : "+makingTwoDigit(second));
    }
    //***************************************************

    //***************************************************
    public void resetTimer(){
        timerSeekbar.setEnabled(true);
        timerSeekbar.setProgress(30);
        buttonChange.setText("Go!");
        timerView.setText("00 : 30");
        timer.cancel();
        activeCountDown = 0;
        if(mplayer != null){
            mplayer.stop();
        }
    }
    //***********************************************
    public void controlTimer(View view){
        if(activeCountDown == 0){
            activeCountDown = 1;
            timerSeekbar.setEnabled(false);
            buttonChange.setText("Stop");
            timer = new CountDownTimer(timerSeekbar.getProgress()*1000,1000){

                @Override
                public void onTick(long millisUntilFinished) {
                    update((int) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    mplayer = MediaPlayer.create(MainActivity.this, R.raw.sound);
                    mplayer.setLooping(true);
                    mplayer.start();
                }
            }.start();
        } else{
            resetTimer();
        }
    }
    //**************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerView = (TextView) findViewById(R.id.timerView);
        timerSeekbar = (SeekBar) findViewById(R.id.timerSeekBar);
        soundSeekbar = (SeekBar) findViewById(R.id.soundSeekbar);
        buttonChange = (Button) findViewById(R.id.buttonChange);
        timerSeekbar.setMax(600);
        timerSeekbar.setProgress(30);
        //adjust volumn with seekbar and initialize seekbar with sound
        sound = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxValue = sound.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currValue = sound.getStreamVolume(AudioManager.STREAM_MUSIC);
        //now set with soundSeekbar
        soundSeekbar.setMax(maxValue);
        soundSeekbar.setProgress(currValue);
        //now set soundSeekbar change leastener
        soundSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sound.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //complete


        timerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                update(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
