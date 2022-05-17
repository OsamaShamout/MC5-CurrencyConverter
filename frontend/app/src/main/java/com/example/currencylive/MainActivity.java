package com.example.currencylive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    com.airbnb.lottie.LottieAnimationView animation;
    TextView welcome_text;
    Button start_button;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //To make all elements fade in
        animation = findViewById(R.id.animationView);
        welcome_text = (TextView) findViewById(R.id.welcomeText);
        start_button = (Button) findViewById(R.id.letsGetStarted);

        animation.setX(-1500);
        animation.animate().translationXBy(1500).setDuration(1500);

        welcome_text.setX(-1500);
        welcome_text.animate().translationXBy(1500).setDuration(1500);

        start_button.setX(-1500);
        start_button.animate().translationXBy(1500).setDuration(1500);


    }
    public void OnClickGetStarted(View view){

        welcome_text.setY(-1500);

        Intent intent = new Intent(this,MainActivity2.class);
        startActivity(intent);




    }
}