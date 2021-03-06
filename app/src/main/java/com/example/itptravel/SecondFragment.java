package com.example.itptravel;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SecondFragment extends Fragment {
    public static ArrayList<String> routeInfo = new ArrayList<String>();
    private static final String TAG = "SecondFragment" ;
    private TextView data;
    private Button btn_frag2GoBack;
    FloatingActionButton fab;
    Spinner spinner;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        data = (TextView) view.findViewById(R.id.textview_second);
        spinner = (Spinner) view.findViewById(R.id.sp_routeList);
        fab = view.findViewById(R.id.fab_addFavourite);
        btn_frag2GoBack = view.findViewById(R.id.btn_frag2GoBack);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.favouriteStations.add(MainActivity.foundStationID);
                Toast.makeText(getActivity().getApplicationContext(),"Station added to favourites.", Toast.LENGTH_SHORT).show();
                writeToFile();
            }
        });

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.selectedRouteID!="") {
                    NavHostFragment.findNavController(SecondFragment.this).navigate(R.id.action_SecondFragment_to_ThirdFragment);
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(),"Select station first.",Toast.LENGTH_SHORT).show();

                }
            }
        });
        getRouteInfo getRouteInfo = new getRouteInfo();
        getRouteInfo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        Log.d(TAG,"getRouteInfo executed.");
        btn_frag2GoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this).navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    private void writeToFile() {
        File directory = new File(getContext().getFilesDir().getAbsolutePath()+File.separator+"ITPTravel");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = "favourites.dat";
        ObjectOutput output;

        try {
            output = new ObjectOutputStream(new FileOutputStream(directory+File.separator+fileName));
            output.writeObject(MainActivity.favouriteStations);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private class getRouteInfo extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d(TAG,routeInfo.toString());
            data.setText("Station no: " +MainActivity.foundStationID);
            //NavHostFragment.findNavController(SecondFragment.this).navigate(R.id.action_SecondFragment_to_ThirdFragment);
            spinner.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, routeInfo));
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    MainActivity.selectedRouteID = routeInfo.get(i);
                    Log.d(TAG,MainActivity.selectedRouteID);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }

        @Override
        protected Void doInBackground(Void... voids) {
            String urlString = "https://data.smartdublin.ie/cgi-bin/rtpi/busstopinformation?stopid="+MainActivity.foundStationID;
            Log.d(TAG,"urlString = "+urlString);
            String stationData="";
            JSONObject stationDataJSON;
            JSONArray tempArray;
            routeInfo.clear();
            MainActivity.selectedRouteID="";

            try {
                URL url = new URL(urlString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    stationData = stationData + line;
                }
                stationDataJSON = new JSONObject(stationData);
                JSONArray stationDataArray = stationDataJSON.getJSONArray("results");
                stationDataJSON = (JSONObject) stationDataArray.get(0);
                stationDataArray = stationDataJSON.getJSONArray("operators");
                for (int i=0;i<stationDataArray.length();i++) {
                    stationDataJSON=(JSONObject) stationDataArray.get(i);
                    tempArray = stationDataJSON.getJSONArray("routes");
                    for (int j=0;j<tempArray.length();j++) {
                        String tempSingleString = (String) tempArray.get(j);
                        routeInfo.add(tempSingleString);

                    }


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
    }
}