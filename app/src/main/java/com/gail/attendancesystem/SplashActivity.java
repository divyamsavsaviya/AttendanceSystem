package com.gail.attendancesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.d("LOG--", "onCreate: Splash Activity launched" );
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Intent intent;
            if(user==null){
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            else {
                intent = new Intent(SplashActivity.this, Home.class);
            }
            startActivity(intent);
            finish();
        }, 2500);
    }
}