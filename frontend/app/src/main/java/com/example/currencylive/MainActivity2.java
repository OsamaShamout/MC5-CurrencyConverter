package com.example.currencylive;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity2 extends AppCompatActivity {

    //Strings to store API results.
    private String value_sell;
    private String value_buy;
    private String result_lira_api;

    //Obtain information of buy and sell rate from lirarate.org
    public class CallLiraAPI extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls){

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
                result_lira_api = result.toString();
                return result_lira_api;
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

                //Create an ArrayList to append the sell rates upon finding a match from the parsed string.
                ArrayList<String> arr = new ArrayList<String>();
                //Keep finding the matches of the regex expression above and append
                while(m1.find()  == true){
                    arr.add(m1.group(1));
                }
                int need_index =  (arr.size()-1);
                value_sell = arr.get(need_index).toString();


                //Create an ArrayList to append the buy rates upon finding a match from the parsed string.
                ArrayList<String> arr2 = new ArrayList<String>();

                //Keep finding the matches of the regex expression above and append
                while(m2.find()  == true){
                    arr.add(m2.group(1));
                }
                int need_index2 =  (arr.size()-1);
                value_buy = arr.get(need_index2).toString();

            }


            catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error in post execution.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String db_buy;
    private String db_sell;
    private String db_result;

    //Obtain information of buy and sell rate from the Database
    public class CallFromDBAPI extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls){

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
                db_result = result.toString();
                return db_result;
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
                String rate_buy = json_obj.getString("rate_lbp_buy");
                String rate_sell = json_obj.getString("rate_lbp_sell");

                db_buy = rate_buy;
                db_sell = rate_sell;

            }


            catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error in post execution.", Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
    public class CallSendDBAPI extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            //Variables to initiate connection.
            URL url;
            HttpsURLConnection https;

            String urlString = urls[0]; // URL to call
            String buy1 = urls[1]; //data buy to post
            String sell1 = urls[2]; //data sell to post
            OutputStream out = null;

            try {
                //Establishing connection between application and API.
                url = new URL(urlString);
                https = (HttpsURLConnection) url.openConnection();
                out = new BufferedOutputStream(https.getOutputStream());

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(buy1);
                writer.write(sell1);
                writer.flush();
                writer.close();
                out.close();

                https.connect();
            } catch (Exception e) {
               Toast.makeText(getApplicationContext(), "Failed to send data to server.", Toast.LENGTH_LONG).show();
            }
            return "";
        }
    }

     */

    //Exchange initialized as USD-->LBP (sell).
    boolean flag_USA = true;


    ImageView country_flag_from;
    ImageView country_flag_to;

    EditText value_inputted;
    TextView result_value;
    TextView dialogue3;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        country_flag_from = (ImageView) findViewById(R.id.coinIconFrom);
        country_flag_to = (ImageView) findViewById(R.id.coinIconTo);

        value_inputted = (EditText) findViewById(R.id.valueInput);
        result_value = (TextView) findViewById(R.id.resultValue);

        dialogue3 = (TextView) findViewById(R.id.dialogBoxUniversal);


        //URL API to obtain buy and sell rates.
        String url = "https://lirarate.org/wp-json/lirarate/v2/rates?currency=LBP&_ver=t202233013";

        //URL API to send data to MySQL sever.
        String url2 = "https://mcprojs.000webhostapp.com/backend/send_data.php";

        //Perform obtaining buy and sell rate.
        CallLiraAPI task = new CallLiraAPI();
        task.execute(url);

        //Perform to insert queries to DB.
       // CallSendDBAPI task2 = new CallSendDBAPI();
        //task2.execute(url2);


        //Set the last updated date for the currency rate upon opening the currency exchange page.
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        dialogue3.setText("Last updated: " + formatter.format(date));

    }


    //OnClick the flags rotate positions indication the transaction:
    //USD --> LBP means sell USD to LBP.
    //LBP --> USD means buy USD from LBP.
    public void OnClickSwitch(View view) {
        country_flag_to.setVisibility(View.GONE);
        country_flag_to.setVisibility(View.VISIBLE);

        //Switch from USD to LBP into LBP to USD.
        if (flag_USA) {
            flag_USA = false;
            country_flag_from.setImageResource(R.drawable.lb_lebanon_flag_icon);
            country_flag_to.setImageResource(R.drawable.us_united_states_flag_icon);

        }
        //Switch from LBP to USD into USD to LBP.
        else {
            flag_USA = true;
            country_flag_from.setImageResource(R.drawable.us_united_states_flag_icon);
            country_flag_to.setImageResource(R.drawable.lb_lebanon_flag_icon);
        }


    }


    public void OnClickConvert(View view) {

        try{
            //Convert from USD to LBP
            double result;
            DecimalFormat formatter = new DecimalFormat("#0.00");
            String currency;
            double parsed_sell = Double.parseDouble(value_sell);
            double parsed_buy = Double.parseDouble(value_buy);
            if (flag_USA) {
                result = Double.parseDouble(value_inputted.getText().toString()) * parsed_sell;
                currency = "\t\tL.L.";
            }
            //Convert from LBP to USD
            else{
                result = Double.parseDouble(value_inputted.getText().toString()) / parsed_buy;
                currency = "\t\tUSD";
            }

            String formatted_result = formatter.format(result);

            result_value.setText(formatted_result + currency);
        }
        catch(NumberFormatException e){
            Toast.makeText(getApplicationContext(), "Error in number formatting.", Toast.LENGTH_LONG).show();
        }

    }
}