package com.mfrancetic.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SeekBar timerSeekBar;

    private TextView timerTextView;

    private Button startStopButton;

    private long minTime = 1000;

    private int startingPosition = 30;

    private String defaultTime = "0:30";

    private CountDownTimer timer;

    private MediaPlayer mediaPlayer;

    private boolean timerIsRunning;

    private int maxTimerInterval = 600;

    private String noTimeRemaining = "00:00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.setVolume(0, 0);
        timerIsRunning = false;

        timerSeekBar = findViewById(R.id.timerSeekBar);
        timerTextView = findViewById(R.id.timerTextView);
        startStopButton = findViewById(R.id.start_stop_button);

        timerSeekBar.setProgress(startingPosition);
        timerSeekBar.setMax(maxTimerInterval);
        timerSeekBar.setProgress(startingPosition);
        timerTextView.setText(defaultTime);
        startStopButton.setText(getString(R.string.start));

        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateTimer(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void startStopAlarm(View view) {
        if (timerIsRunning) {
            timerTextView.setText(defaultTime);
            timerSeekBar.setProgress(startingPosition);
            timerSeekBar.setEnabled(true);
            mediaPlayer.stop();
            startStopButton.setText(getString(R.string.start));
            timerIsRunning = false;
            timer.cancel();
        } else {
            timerSeekBar.setEnabled(false);
            startStopButton.setText(getString(R.string.stop));
            int progress = timerSeekBar.getProgress();

            timer = new CountDownTimer(progress * 1000 + 100, minTime) {
                @Override
                public void onTick(long millisecondsUntilDone) {
                    timerIsRunning = true;
                    long millisecondsTillDoneLong = millisecondsUntilDone / 1000;
                    updateTimer((int) millisecondsTillDoneLong);
                }

                @Override
                public void onFinish() {
                    timerTextView.setText(noTimeRemaining);
                    timerSeekBar.setProgress(startingPosition);
                    timer.cancel();
                    timerIsRunning = false;
                    startStopButton.setText(getString(R.string.start));
                    mediaPlayer.start();
                }
            }.start();
        }
    }

    public void updateTimer(int secondsLeft) {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - (minutes * 60);
        String secondString = Integer.toString(seconds);
        if (seconds <= 9) {
            secondString = "0" + secondString;
        }
        String timeRemaining = minutes + ":" + secondString;
        timerTextView.setText(timeRemaining);
    }
}