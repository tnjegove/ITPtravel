package com.example.itptravel;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class fetchBusStationInfo extends AsyncTask<Void, Void, Void> {
    String stationData = "";
    JSONObject stationsData;

    private ArrayList<StationDataMember> results = new ArrayList<StationDataMember>();
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
            stationsData = new JSONObject(stationData);
            JSONArray resultsArray = stationsData.getJSONArray("results");
            //stationData = resultsArray.toString();

            for (int i = 0; i<resultsArray.length();i++) {
                JSONObject singleStation = (JSONObject) resultsArray.get(i);
                StationDataMember member = new StationDataMember();
                member.setDm_stopid((String) singleStation.get("stopid"));
                member.setDm_displaystopid((String) singleStation.get("displaystopid"));
                member.setDm_shortname((String) singleStation.get("shortname"));
                member.setDm_shortnamelocalized((String) singleStation.get("shortnamelocalized"));
                member.setDm_fullname((String) singleStation.get("fullname"));
                member.setDm_fullnamelocalized((String) singleStation.get("fullnamelocalized"));
                member.setDm_latitude((String) singleStation.get("latitude"));
                member.setDm_longitude((String) singleStation.get("longitude"));
                member.setDm_lastupdated((String) singleStation.get("lastupdated"));
                results.add(member);
            }



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        MainActivity.StationDataList = this.results;
        FirstFragment.data.setText("Stations loaded. Current coordinates - latitude: " +MainActivity.latitude + "/n longitude: " +MainActivity.longitude);
    }
}
