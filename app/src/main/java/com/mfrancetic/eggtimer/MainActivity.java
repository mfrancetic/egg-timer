package com.mfrancetic.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SeekBar timerSeekBar;

    private TextView timerTextView;

    private Button stopButton;

    private long minTime = 1000;

    private int startingPosition = 5;

    private String defaultTime = "0:30";

    private CountDownTimer timer;

    private MediaPlayer mediaPlayer;

    private boolean timerIsRunning;

    private int maxTimerInterval = 60;

    private int minTimerInterval = 5;

    private int timerInterval = 5;

    private int timerIntervalMilliseconds = 5000;

    private int timerIntervalMinimum = 5100;

    private String noTimeRemaining = "0:00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        timerIsRunning = false;

        timerSeekBar = findViewById(R.id.timerSeekBar);
        timerTextView = findViewById(R.id.timerTextView);
        stopButton = findViewById(R.id.stop_button);

        timerSeekBar.setProgress(startingPosition);
        timerSeekBar.setMax((maxTimerInterval-minTimerInterval)/timerInterval);
        timerTextView.setText(defaultTime);

        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (timerIsRunning) {
                    timer.cancel();
                }
                updateAlarm(i * timerIntervalMilliseconds + timerIntervalMinimum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void stopTimer(View view) {
        timerTextView.setText(defaultTime);
        timerSeekBar.setProgress(startingPosition);
        mediaPlayer.stop();
        timer.cancel();
    }

    public void updateAlarm(long progress) {
        timer = new CountDownTimer(progress, minTime) {
            @Override
            public void onTick(long millisecondsUntilDone) {
                timerIsRunning = true;
                int seconds = (int) (millisecondsUntilDone / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                String timeRemaining = String.format(Locale.GERMANY, "%02d", minutes)
                        + ":" + String.format(Locale.GERMANY, "%02d", seconds);
                timerTextView.setText(timeRemaining);
            }

            @Override
            public void onFinish() {
                timerTextView.setText(noTimeRemaining);
                timerSeekBar.setProgress(startingPosition);
                timer.cancel();
                timerIsRunning = false;
                mediaPlayer.start();
            }
        }.start();
    }
}