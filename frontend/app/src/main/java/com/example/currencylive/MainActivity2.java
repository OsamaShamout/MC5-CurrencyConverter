package com.example.currencylive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity2 extends AppCompatActivity {

    private String value_sell;
    private String value_buy;

    public class DownloadTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls){

            //String to store API result.

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
                String newLine = System.getProperty("line.separator");
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                for (String line; (line = reader.readLine()) != null; ) {
                    if (result.length() > 0) {
                        result.append(newLine);
                    }
                    result.append(line);
                }
                return result.toString();
            }
            catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error in receiving data.", Toast.LENGTH_LONG).show();

                return null;
            }

        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);

            try{

                //Convert JSON objects into Strings.
                JSONObject json_obj = new JSONObject(s);
                String rate_sell = json_obj.getString("sell");
                String rate_buy = json_obj.getString("buy");

                //Prepare the regex expression to extract the conversion rate
                Pattern p = Pattern.compile("1648\\d\\d\\d\\d\\d\\d\\d\\d\\d,(\\d\\d\\d\\d\\d)", Pattern.MULTILINE);
                Matcher m1 = p.matcher(rate_sell);
                Matcher m2 = p.matcher(rate_buy);

                //Create an array list and append to sell ArrayList upon finding a match into list for sell rate
                ArrayList<String> arr = new ArrayList<String>();
                while(m1.find()  == true){
                    arr.add(m1.group(1));
                }
                Log.i("ArrayList Sell: ", Arrays.toString(arr.toArray()));
                int need_index =  (arr.size()-3);
                value_sell = arr.get(need_index).toString();

                //Create an array list and append to buy ArrayList upon finding a match into list for buy rate
                ArrayList<String> arr2 = new ArrayList<String>();
                while(m2.find()  == true){
                    arr.add(m2.group(1));
                }
                Log.i("ArrayList Buy: ", Arrays.toString(arr.toArray()));
                int need_index2 =  (arr.size()-3);
                value_buy = arr.get(need_index2).toString();
            }


            catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error in post execution.", Toast.LENGTH_LONG).show();
            }
        }
    }

    boolean flag_USA = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        String url = "https://lirarate.org/wp-json/lirarate/v2/rates?currency=LBP&_ver=t202233013";

        String url2 = "https://lirarate.org/wp-json/lirarate/v2/rates?currency=LBP&_ver=t202233013";

        DownloadTask task = new DownloadTask();

        task.execute(url);
       // task.execute(url2);

        value_received = (TextView) findViewById(R.id.resultValue);







    }


    public void OnClickSwitch(View view) {
        ImageView country_flag_from = (ImageView) findViewById(R.id.coinIconFrom);
        ImageView country_flag_to = (ImageView) findViewById(R.id.coinIconTo);

        country_flag_to.setVisibility(View.GONE);

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