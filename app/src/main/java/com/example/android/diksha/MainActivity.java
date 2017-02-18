package com.example.android.diksha;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private final String URL_DOMAIN ="http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2012-01-01&endtime=2012-12-01&minmagnitude=6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sunamiReportAsyn task=new sunamiReportAsyn();
        task.execute();
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


            return null;
        }

        private String makeHttpConnection(URL url) throws IOException {
            String jsonResponse="";
            if(url == null){
                return  jsonResponse;
            }
            HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();

            try {
                InputStream in =new BufferedInputStream(urlConnection.getInputStream());
                jsonResponse= readFromStream(in);
            }finally {
                urlConnection.disconnect();

            }
             return jsonResponse;

        }

        private String readFromStream(InputStream in) {
            return null;
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
            super.onPostExecute(words);
        }
    }
}
