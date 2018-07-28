package com.m_Sallam.mahmoudmostafa.myapplication.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.m_Sallam.mahmoudmostafa.myapplication.R;

public class Splash extends AppCompatActivity {

    TextView titleText ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        titleText= (TextView) findViewById(R.id.titleText);
        Typeface tf = Typeface.createFromAsset(getAssets() ,"daisy.ttf") ;
        titleText.setTypeface(tf);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, Home.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

    }
}
