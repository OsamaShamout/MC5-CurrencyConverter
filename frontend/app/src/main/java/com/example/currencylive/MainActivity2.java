package com.example.currencylive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity2 extends AppCompatActivity {



    public class DownloadTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls){

            //String to store API result.
            String result = "";

            //Variables to initiate connection.
            URL url;
            HttpsURLConnection https;

            try{
                //Establishing connection between application and API.
                url = new URL(urls[0]);
                https = (HttpsURLConnection) url.openConnection();

                //InputStreams to obtain input from API.
                InputStream in = https.getInputStream();

                //Perform conversion function to obtain result as a string.
                result = convertStreamToString(in);
            }
            catch(Exception e){
                e.printStackTrace();
                return null;
            }

            return null;
        }

        protected void onPostExecute(String s){

        }


        //Function that converts stream from InputStream into a String
        //Responsible for turning url text into a string.
        protected String convertStreamToString(InputStream is) {
            try {
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                for (int length; (length = is.read(buffer)) != -1; ) {
                    result.write(buffer, 0, length);
                }

                return result.toString();
            }
            catch(IOException e){
                e.printStackTrace();
                return null;
            }
            catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }

    boolean flag_USA = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        String url = "https://mcprojs.000webhostapp.com/backend/test.php";

        DownloadTask task = new DownloadTask();

        task.execute(url);

    }


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