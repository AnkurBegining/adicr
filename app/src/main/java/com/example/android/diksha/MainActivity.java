package com.example.android.diksha;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private final String URL_DOMAIN ="http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2012-01-01&endtime=2012-12-01&minmagnitude=6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sunamiReportAsyn task=new sunamiReportAsyn();
        task.execute();


    }
    private void updateUi(Words sunamiData){
        TextView eventTextFill = (TextView)findViewById(R.id.event_fill_text_view);
        eventTextFill.setText(sunamiData.getEvent());

        TextView dateTextFill = (TextView)findViewById(R.id.date_fill_text_view);
        dateTextFill.setText(getDateString(sunamiData.getDate()));

        TextView alertTextFill = (TextView)findViewById(R.id.alert_fill_text_view);
        alertTextFill.setText(getTsunamiAlertString(sunamiData.getAlert()));







    }
    private String getDateString(long timeInMilliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy 'at' HH:mm:ss z");
        return formatter.format(timeInMilliseconds);
    }

    private String getTsunamiAlertString(int tsunamiAlert) {
        switch (tsunamiAlert) {
            case 0:
                return "No";
            case 1:
                return "Yes";
            default:
                return "No data available";
        }
    }



    private class sunamiReportAsyn extends AsyncTask<URL, Void , Words>{

        @Override
        protected Words doInBackground(URL... urls) {
            //create url object
            URL url=createUrl(URL_DOMAIN);
            //perform http and get reponse in String back
            String jsonResponse="";
            try {
                jsonResponse=makeHttpConnection(url);
            }catch (Exception e){

            }
            Words sunamiData = null;
            try {
                sunamiData = extractFromJson(jsonResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return sunamiData;
        }

        private Words extractFromJson(String jsonResponse) throws JSONException {
            try {
                JSONObject root = new JSONObject(jsonResponse);
                JSONArray jsonArray= root.getJSONArray("features");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject firstFeature = jsonArray.getJSONObject(i);
                    JSONObject properties =firstFeature.getJSONObject("properties");
                    String title =properties.getString("title");
                    long time =properties.getLong("time");
                    int alert =properties.getInt("tsunami");
                    Words sunamiData= new Words(title, time, alert);
                    return sunamiData;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

             return null;
        }

        private String makeHttpConnection(URL url) throws IOException {
            String jsonResponse="";
            if(url == null){
                return  jsonResponse;
            }
            HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();

            //read data from url using InputStream class

            try {
                InputStream in =new BufferedInputStream(urlConnection.getInputStream());
                jsonResponse= readFromStream(in);
            }finally {
                urlConnection.disconnect();

            }
             return jsonResponse;

        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output=new StringBuilder();

            if(inputStream !=null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                while (line != null){
                    output.append(line);
                    line =bufferedReader.readLine();
                }

            }
            return output.toString();
        }

        private URL createUrl(String urlDomain) {
            URL url =null;
            try {
                url=new URL(urlDomain);

            }catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return url;

        }

        @Override
        protected void onPostExecute(Words words) {
            updateUi(words);

        }
    }
}
