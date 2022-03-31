package com.example.currencylive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

                //Create an ArrayList to append the buy and sell rates upon finding a match from the parsed string.
                ArrayList<String> arr = new ArrayList<>();
                //Keep finding the matches of the regex expression above and append
                while(m1.find()){
                    arr.add(m1.group(1));
                }
                int need_index =  (arr.size()-1);
                value_sell = arr.get(need_index);

                //Keep finding the matches of the regex expression above and append
                while(m2.find()){
                    arr.add(m2.group(1));
                }
                int need_index2 =  (arr.size()-1);
                value_buy = arr.get(need_index2);

                //Display current rate for user.
                dialogue1.setText("Buy at: " + value_buy + "\t Sell at: " + value_sell);

                //Display the update date of the current displayed conversion rate.
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                dialogue1.append("\nLast updated: " + formatter.format(date));

            }

            catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error in post execution.", Toast.LENGTH_LONG).show();
            }
        }
    }
//
//    //Send data to DB.
//    String value_user;
//
//    public class CallSendDBAPI extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... strings) {
//            try {
//
//                postText();
//
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//        private void postText(){
//            try{
//            //Variables to initiate connection.
//            URL url;
//            HttpsURLConnection conn;
//
//            //Get Strings ready
//            if(!flag_USA){
//                currency = "LBP";
//            }
//
//            value_user = value_inputted.getText().toString();
//
//            String urlString = "https://mcprojs.000webhostapp.com/backend/send_data.php"; // URL to call
//
//
//                //Establishing connection between application and API.
//                //url = new URL(urlString);
//
//                HttpClient httpClient = new DefaultHttpClient();
//                HttpPost httpPost = new HttpPost(urlString);
//                HttpResponse response;
//
//                //add data
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//                nameValuePairs.add(new BasicNameValuePair("buy", "buy11"));
//                nameValuePairs.add(new BasicNameValuePair("sell", "sell11"));
//                nameValuePairs.add(new BasicNameValuePair("currency", "currency11"));
//                nameValuePairs.add(new BasicNameValuePair("amount", "amount11"));
//
//                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                // execute HTTP post request
//                response = httpClient.execute(httpPost);
//                HttpEntity resEntity = response.getEntity();
//
//                if (resEntity != null) {
//
//                    String responseStr = EntityUtils.toString(resEntity).trim();
//                    Log.e("Response: ", responseStr);
//
//                }
//
//
//            } catch (Exception e) {
//               Toast.makeText(getApplicationContext(), "Failed to send data to server.", Toast.LENGTH_LONG).show();
//            }
//        }
//    }


    //Obtain information conversion and data from the DB.
    private String result_db;
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
                String currency = jsonObject.getString("currency");
                String amount_db = jsonObject.getString("amount_result");

                result_value.setText(amount_db + "\t" + currency);
            }

            catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error in post execution.", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Exchange initialized as USD-->LBP (sell).
    boolean flag_USA = true;
    String currency = "USA";

    ImageView country_flag_from;
    ImageView country_flag_to;
    EditText value_inputted;
    TextView result_value;
    TextView dialogue1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        country_flag_from = (ImageView) findViewById(R.id.coinIconFrom);
        country_flag_to = (ImageView) findViewById(R.id.coinIconTo);

        value_inputted = (EditText) findViewById(R.id.valueInput);
        result_value = (TextView) findViewById(R.id.resultValue);

        dialogue1 = (TextView) findViewById(R.id.dialogBoxUniversal1);

        //URL API to obtain buy and sell rates.
        String url1 = "https://lirarate.org/wp-json/lirarate/v2/rates?currency=LBP&_ver=t202233013";

        //Perform obtaining buy and sell rate.
        CallLiraAPI task1 = new CallLiraAPI();
        task1.execute(url1);

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
        //URL API to send data to MySQL sever.
        String url2 = "https://mcprojs.000webhostapp.com/backend/send_data.php";
        
        String url3 = "https://mcprojs.000webhostapp.com/backend/get_data.php";

        //Perform to insert queries to DB.
        //CallSendDBAPI task2 = new CallSendDBAPI();
        //task2.execute(url2);

        //value_user = value_inputted.getText().toString();

        if(!flag_USA){
                currency = "LBP";
            }

        //Retrieve information from DB and return result to user.
        CallDBAPI task3 = new CallDBAPI();
        task3.execute(url2);


    }
}