package com.example.currencylive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity2 extends AppCompatActivity {


    boolean flag_USA = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }

    //Test Comment

    public void OnClickSwitch(View view) {
        ImageView country_flag_from = (ImageView) findViewById(R.id.coinIconFrom);
        ImageView country_flag_to = (ImageView) findViewById(R.id.coinIconTo);

        country_flag_to.setVisibility(View.GONE);
        country_flag_to.setVisibility(0);


        if (flag_USA) {
            flag_USA = false;
            country_flag_from.setImageResource(R.drawable.lb_lebanon_flag_icon);
            country_flag_to.setImageResource(R.drawable.us_united_states_flag_icon);
        } else {
            flag_USA = true;
            country_flag_from.setImageResource(R.drawable.us_united_states_flag_icon);
            country_flag_to.setImageResource(R.drawable.lb_lebanon_flag_icon);
        }


    }


    public void OnClickConvert(View view) {
        EditText value_inputted = (EditText) findViewById(R.id.valueInput);
        TextView result_value = (TextView) findViewById(R.id.resultValue);

        //Universal Dialogue Box for information when both values might cause an error.
        TextView dialogue3 = (TextView) findViewById(R.id.dialogBoxUniversal);


        try{
            //Convert from USD to LBP
            double result;
            DecimalFormat formatter = new DecimalFormat("#0.00");
            String currency;

            if (flag_USA) {
                result = Double.parseDouble(value_inputted.getText().toString()) * 22000;
                currency = "\t\tL.L.";
            }
            //Convert from LBP to USD
            else{
                result = Double.parseDouble(value_inputted.getText().toString()) / 22000;
                currency = "\t\tUSD";
            }

            String formatted_result = formatter.format(result);

            result_value.setText(formatted_result + currency);
        }
        catch(NumberFormatException e){
            dialogue3.setText("Error in number formatting.");
        }

    }
}