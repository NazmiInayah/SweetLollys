package com.example.kelompok4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    ImageButton btnMusic, btnVideo, btnLogout;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnMusic = (ImageButton) findViewById(R.id.btnMusic);
        btnVideo = (ImageButton) findViewById(R.id.btnVideo);
        btnLogout = (ImageButton) findViewById(R.id.btnLogout);

        mAuth = FirebaseAuth.getInstance();

        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(getApplicationContext(), AudioActivity.class);
                startActivity(home);
            }
        });

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(getApplicationContext(), VideoActivity.class);
                startActivity(home);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                signOutUser();
            }
        });

    }

    private void signOutUser() {
        Intent LoginActivity = new Intent(HomeActivity.this, LoginActivity.class);
        LoginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(LoginActivity);
        finish();

    }


}