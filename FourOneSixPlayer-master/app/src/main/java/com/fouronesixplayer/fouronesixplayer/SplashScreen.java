package com.fouronesixplayer.fouronesixplayer;

import android.content.Intent;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.view.WindowManager;
import android.widget.EditText;



public class SplashScreen extends AppCompatActivity implements View.OnClickListener {

    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        Intent intent1=new Intent(this, Login.class);
        startActivity(intent1);
    }



}

