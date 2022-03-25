package com.example.CurrencyConverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void OnClickGetStarted(View view){
        Intent intent = new Intent(this,MainActivity2.class);
        startActivity(intent);

        //To make all elements fade in
        com.airbnb.lottie.LottieAnimationView animation = findViewById(R.id.animationView);
        TextView welcome_text = (TextView) findViewById(R.id.welcomeText);
        Button start_button = (Button) findViewById(R.id.letsGetStarted);


    }

}