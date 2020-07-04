package com.example.itptravel;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class fetchBusStationInfo extends AsyncTask<Void, Void, Void> {
    String stationData = "";
    @Override
    protected Void doInBackground(Void... voids) {

        try {
            URL url = new URL("https://data.smartdublin.ie/cgi-bin/rtpi/busstopinformation?stopid=&operator=bac");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                stationData = stationData + line;

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        FirstFragment.data.setText(this.stationData);
    }
}
