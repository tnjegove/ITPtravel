package com.example.itptravel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

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


public class ThirdFragment extends Fragment {
    private static final String TAG = "ThirdFragment";
    private TextView textview_thirdFragment;
    private String urlString="";
    private ArrayList<String> realTimeInfoList = new ArrayList<String>();
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_third).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ThirdFragment.this)
                        .navigate(R.id.action_ThirdFragment_to_SecondFragment);
            }
        });
        textview_thirdFragment = (TextView) view.findViewById(R.id.textview_third);
        getRealTimeInfo getrealtimeinfo = new getRealTimeInfo();
        getrealtimeinfo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



    }
    private class getRealTimeInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            textview_thirdFragment.setText("");
            if (realTimeInfoList.size()!=0){
                for (int i=0;i<realTimeInfoList.size();i++) {
                    textview_thirdFragment.append("Bus is due in: " + realTimeInfoList.get(i) + " minutes.\n");
                }
            }
            else {
                textview_thirdFragment.setText("No data at this time.");

            }


        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "Selected station is " + MainActivity.foundStationID+" selected route is " +MainActivity.selectedRouteID);
            urlString = "https://data.smartdublin.ie/cgi-bin/rtpi/realtimebusinformation?stopid="+MainActivity.foundStationID+"&routeid="+ MainActivity.selectedRouteID+"&format=json";
            String realTimeData="";
            JSONObject JSONObj;
            JSONArray tempArray;
            realTimeInfoList.clear();

            try {
                URL url = new URL(urlString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line!= null) {
                    line = bufferedReader.readLine();
                    realTimeData = realTimeData + line;
                }
                JSONObj = new JSONObject(realTimeData);
                tempArray = JSONObj.getJSONArray("results");
                for (int i = 0; i<tempArray.length();i++) {
                    JSONObj = (JSONObject) tempArray.get(i);
                    realTimeData= (String) JSONObj.get("duetime");
                    realTimeInfoList.add(realTimeData);

                }
                Log.d(TAG, realTimeInfoList.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
