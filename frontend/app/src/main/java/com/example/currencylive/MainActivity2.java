package com.example.currencylive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;


public class MainActivity2 extends AppCompatActivity {

    //Obtain information of buy and sell rate from lirarate.org
    //Strings to store API results.
    private String value_sell;
    private String value_buy;
    private String result_lira_api;


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

                //Log server return.
                Log.e("test", "result from lira api: " + result_lira_api);
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
                //Enter first layer "Lirarate"
                JSONObject json_obj2 = json_obj.getJSONObject("lirarate");
                //Enter second layer "the sell and buy"
                String rate_sell = json_obj2.getString("sell");
                String rate_buy = json_obj2.getString("buy");

                //Prepare the regex expression to extract the conversion rate //? in case the rate becomes
                //above 5 digits (Let's hope it won't).
                Pattern p = Pattern.compile("1663\\d\\d\\d\\d\\d\\d\\d\\d\\d,(\\d\\d\\d\\d\\d\\d?)", Pattern.MULTILINE);
                Matcher m1 = p.matcher(rate_sell);
                Matcher m2 = p.matcher(rate_buy);

                //Create a stack to push the buy and sell rates upon finding a match from the parsed string.
                Stack<String> stack = new Stack<String>();
                //Keep finding the matches of the regex expression above and append
                while(m1.find()){
                    stack.push(m1.group(1));
                }

                //Pop (last) element (Last sell value).
                value_sell = stack.pop();

                //Log server return.
                Log.e("test", "result splitting1 " + value_sell);

                //Keep finding the matches of the regex expression above and append
                while(m2.find()){
                    stack.push(m2.group(1));
                }

                //Pop last element (Last buy value).
                value_buy = stack.pop();

                //Log server return.
                Log.e("test", "result splitting2 " + value_buy);

            }

            catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error in post execution.", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Send data to DB.
    String value_user;
    String result_db1;
    public class CallSendDBAPI extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls){

            //Variables to initiate connection.
            URL url;
            HttpsURLConnection conn;

            try{
                //Establishing connection between application and API.
                url = new URL(urls[0]);
                conn = (HttpsURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset", "UTF-8");

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                conn.connect();

                //Creating new JSON object to communicate with it to DB.
                JSONObject jo = new JSONObject();
                StringBuffer packedData=new StringBuffer();
                value_user = value_inputted.getText().toString();

                //Send the variables to their respective $_POST.
                jo.put("buy", value_buy);
                jo.put("sell", value_sell);
                jo.put("currency", currency);
                jo.put("amount", value_user);

                //Pack data to be processed by PHP for $_POST.
                boolean firstValue=true;

                Iterator it=jo.keys();

                do {
                    String key=it.next().toString();
                    String value=jo.get(key).toString();

                    if(firstValue)
                    {
                        firstValue=false;
                    }else
                    {
                        packedData.append("&");
                    }

                    packedData.append(URLEncoder.encode(key,"UTF-8"));
                    packedData.append("=");
                    packedData.append(URLEncoder.encode(value,"UTF-8"));

                }while (it.hasNext());

                //Log in console to track. "e" used as color red will appear more significantly while reading log.
                Log.e("Packed data:", packedData.toString());

                //Write to PHP file.
                OutputStream os=conn.getOutputStream();
                BufferedWriter wr=new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));

                wr.write(packedData.toString());
                wr.flush();
                wr.close();

                //InputStreams to obtain input from API.
                InputStream in = conn.getInputStream();

                //Obtain server return result as a string.
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
                result_db1 = result.toString();

                //Log server return.
                Log.e("test", "result from server: " + result_db1);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
            }

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            try{

            }

            catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error in post execution.", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Obtain information conversion and data from the DB.
    private String result_db;
    private String amount_db;
    public class CallDBAPI extends AsyncTask<String, Void, String> {
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
                result_db = result.toString();

                return result_db;
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
                JSONArray json_arr = new JSONArray(s);

                JSONObject jsonObject = json_arr.getJSONObject(0);
                amount_db = jsonObject.getString("amount_result");

                //To format value.
                float value = Float.parseFloat(amount_db);

                Toast.makeText(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();

                //Display to user the value.
                if(flag_USA){
                    result_value.setText(String.format("%.2f", value) + "\t\tLBP");
                }
                else {
                    result_value.setText(String.format("%.2f", value) + "\t\tUSD");
                }
            }

            catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error in post execution.", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Exchange initialized as USD-->LBP (sell).
    boolean flag_USA = true;
    String currency ="USD";
    ImageView country_flag_from;
    ImageView country_flag_to;
    EditText value_inputted;
    TextView result_value;
    TextView dialogue1;
    TextView dialogue2;
    int year;
    int month;
    int day;
    Date date;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        country_flag_from = (ImageView) findViewById(R.id.coinIconFrom);
        country_flag_to = (ImageView) findViewById(R.id.coinIconTo);

        value_inputted = (EditText) findViewById(R.id.valueInput);
        result_value = (TextView) findViewById(R.id.resultValue);

        dialogue1 = (TextView) findViewById(R.id.dialogBoxUniversal1);
        dialogue2 = (TextView) findViewById(R.id.dialogBoxUniversal2);

        //URL API to obtain buy and sell rates.

        Calendar right_now = Calendar.getInstance();
        Log.e("Date is: ", right_now.toString());

        year = right_now.get(Calendar.YEAR);
        Log.e("Year is:", String.valueOf(year));
        month = right_now.get(Calendar.MONTH) + 1;
        Log.e("Month is:", String.valueOf(month));
        day = right_now.get(Calendar.DAY_OF_MONTH);
        Log.e("Day is:", String.valueOf(day));
        date = right_now.getTime();
        Log.e("Day is:", date.toString());



        String var = "";
        //var apparently seems to be the hour that the rate is updated.
        //Get the update date and set the var to be the hour.
//        for (int i = 0; i < 24; i++){
//            var = String.valueOf(i);
//            String url1 = "https://lirarate.org/wp-json/lirarate/v2/rates?currency=LBP&_ver=t" + year + month + day + var;
//
//        }

        //Perform obtaining buy and sell rate.
        CallLiraAPI task1 = new CallLiraAPI();
        //String url1 = "https://lirarate.org/wp-json/lirarate/v2/rates?currency=LBP&_ver=t" + year + month + day + "1";
        String url1 = "https://lirarate.org/wp-json/lirarate/v2/all?currency=LBP&_ver=t202291718";
        task1.execute(url1);


        //Display current rate for user.
        //Wait until process gets the buy and sell value.
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (value_buy != null) {
                    dialogue1.setText("Buy at:  " + value_buy + "\t Sell at:  " + value_sell);
                    dialogue2.setText("Updated at: " + date);
                    return;
                }


            }
        }, 0, 500);

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
            currency = "LBP";
            country_flag_from.setImageResource(R.drawable.lb_lebanon_flag_icon);
            country_flag_to.setImageResource(R.drawable.us_united_states_flag_icon);
        }
        //Switch from LBP to USD into USD to LBP.
        else {
            flag_USA = true;
            currency = "USD";
            country_flag_from.setImageResource(R.drawable.us_united_states_flag_icon);
            country_flag_to.setImageResource(R.drawable.lb_lebanon_flag_icon);
        }
    }

    boolean result_processing = false;
    public void OnClickConvert(View view) throws IOException {

        //URL API to send data to MySQL sever.
        String url2 = "https://mcprojs.000webhostapp.com/backend/send_data.php";
        
        String url3 = "https://mcprojs.000webhostapp.com/backend/get_data.php";

        Toast.makeText(getApplicationContext(), "Please wait.", Toast.LENGTH_SHORT).show();

        //Perform to insert queries to DB.
       CallSendDBAPI task2 = new CallSendDBAPI();
       task2.execute(url2);

       //Retrieve information from DB and return result to user.
       CallDBAPI task3 = new CallDBAPI();
       task3.execute(url3);
    }

    //Back to main menu
    public void OnClickBackButton(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}