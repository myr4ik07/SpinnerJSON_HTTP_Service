package com.example.jaros.inventoryapp;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        spinner = (Spinner) findViewById(R.id.spinner);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                sendGet();
            }
        });
        thread.start();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    private void sendGet() {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        String line;

        JSONObject dataJsonObj;
        ArrayList<String> unitArray = new ArrayList<String>();

        try {
            URL url = new URL("http://192.168.1.102/http_1/hs/http_1/getUnit");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            resultJson = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            dataJsonObj = new JSONObject(resultJson);
            JSONArray users = dataJsonObj.getJSONArray("unit");
            for (int i = 0; i < users.length(); i++) {
                unitArray.add(users.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, unitArray));

    }
}