package com.example.kelompok4;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AudioActivity extends AppCompatActivity {
    TextView tvAudioName, tvAudioPosition, tvAudioDuration;
    SeekBar seekBarAudio;
    ImageButton btnPrevious, btnNext, btnPlay, btnPause, btnStop;
    Button btnBack;


    final MediaPlayer mediaPlayer = new MediaPlayer();
    final Handler handler = new Handler();
    Runnable runnable;

    ArrayList<Music> musicList = new ArrayList<>();

    int nowPlaying = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        btnBack =findViewById(R.id.btnBack);

        tvAudioName = findViewById(R.id.tvAudioName);
        tvAudioPosition = findViewById(R.id.tvAudioPosition);
        tvAudioDuration = findViewById(R.id.tvAudioDuration);
        seekBarAudio = findViewById(R.id.seekBarAudio);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);


        musicList.add(new Music(getRawUri(R.raw.anak_ayam), "Anak Ayam"));
        musicList.add(new Music(getRawUri(R.raw.desaku_yang_kucinta), "Desaku yang Kucinta"));
        musicList.add(new Music(getRawUri(R.raw.hari_libur), "Hari Libur"));
        musicList.add(new Music(getRawUri(R.raw.empatsehat_lima_sempurna), "4 Sehat 5 Sempurna"));
        musicList.add(new Music(getRawUri(R.raw.kupu_kupu), "Kupu-kupu yang Lucu"));
        musicList.add(new Music(getRawUri(R.raw.family_song),"Family Song"));
        musicList.add(new Music(getRawUri(R.raw.name), "Name"));
        musicList.add(new Music(getRawUri(R.raw.abc), "ABC"));
        musicList.add(new Music(getRawUri(R.raw.lets_count), "Lets Count"));
        musicList.add(new Music(getRawUri(R.raw.daysoftheweeksong), "Days of The Week Song"));

        btnBack.setOnClickListener(view -> {
            Intent home = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(home);
        });


        init();

        try {
            loadMusic(musicList.get(nowPlaying));
        } catch (IOException e) {
            e.printStackTrace();
        }


        btnPlay.setOnClickListener(view -> playMusic());
        btnPause.setOnClickListener(view -> pauseMusic());


        btnStop.setOnClickListener(view -> {
            stopMusic();
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.seekTo(0);
            handler.postDelayed(runnable, 500);
        });


        btnPrevious.setOnClickListener(view -> {
            try {
                goToPreviousMusic();
            } catch (IndexOutOfBoundsException e) {
                showToast("This is the first music.");
            }
        });


        btnNext.setOnClickListener(view -> {
            try {
                goToNextMusic();
            } catch (IndexOutOfBoundsException e) {
                showToast("This is the last music.");
            }
        });


        seekBarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mediaPlayer.seekTo(i);
                }

                tvAudioPosition.setText(seekBarTimeFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    @Override
    protected void onDestroy() {
        stopMusic();
        mediaPlayer.release();
        super.onDestroy();
    }

    private void init() {
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build());

        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            try {
                goToNextMusic();
            } catch (IndexOutOfBoundsException e) {
                showPlayButton();

                mediaPlayer.seekTo(0);
            }
        });
    }

    private void loadMusic(Music music) throws IOException {
        mediaPlayer.reset();
        mediaPlayer.setDataSource(getApplicationContext(), music.getUri());
        mediaPlayer.prepare();
        runnable = new Runnable() {
            @Override
            public void run() {
                seekBarAudio.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        };

        seekBarAudio.setMax(mediaPlayer.getDuration());
        tvAudioDuration.setText(seekBarTimeFormat(mediaPlayer.getDuration()));
        tvAudioName.setText(music.getTitle());
    }


    private void goToPreviousMusic() {
        if (nowPlaying == 0) {
            throw new IndexOutOfBoundsException();
        }
        stopMusic();
        nowPlaying -= 1;

        try {
            loadMusic(musicList.get(nowPlaying));
        } catch (IOException e) {
            e.printStackTrace();
        }
        playMusic();
    }


    private void goToNextMusic() {
        if (nowPlaying == musicList.size() - 1) {
            throw new IndexOutOfBoundsException();
        }
        stopMusic();
        nowPlaying += 1;

        try {
            loadMusic(musicList.get(nowPlaying));
        } catch (IOException e) {
            e.printStackTrace();
        }
        playMusic();
    }

    private void playMusic() {
        showPauseButton();

        mediaPlayer.start();
        handler.postDelayed(runnable, 0);
    }

    private void pauseMusic() {
        showPlayButton();

        mediaPlayer.pause();
        handler.removeCallbacks(runnable);
    }

    private void stopMusic() {
        showPlayButton();

        mediaPlayer.stop();
        handler.removeCallbacks(runnable);
    }

    @SuppressLint("DefaultLocale")
    private String seekBarTimeFormat(int durationInMs) {
        long minutesDuration = TimeUnit.MILLISECONDS.toMinutes(durationInMs);
        long secondsDuration = TimeUnit.MILLISECONDS.toSeconds(durationInMs);

        return String.format("%02d:%02d",
                minutesDuration,
                secondsDuration - TimeUnit.MINUTES.toSeconds(minutesDuration));
    }

    private void showPlayButton() {
        btnPlay.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.GONE);
    }

    private void showPauseButton() {
        btnPause.setVisibility(View.VISIBLE);
        btnPlay.setVisibility(View.GONE);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
                .show();
    }

    private Uri getRawUri(int rawId) {
        return Uri.parse("android.resource://" + getPackageName() + "/" + rawId);

    }
}