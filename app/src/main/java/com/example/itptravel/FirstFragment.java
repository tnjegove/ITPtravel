package com.example.itptravel;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.w3c.dom.Text;

public class FirstFragment extends Fragment {
    public static TextView data;
    public static Button gpsButton;



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        data = (TextView) view.findViewById(R.id.textView2);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        fetchBusStationInfo getJSON = new fetchBusStationInfo();
        getJSON.execute();
        gpsButton = view.findViewById(R.id.gps_button);
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        TrackStation startTracking = new TrackStation();
        startTracking.execute();


    }
    private class TrackStation extends AsyncTask<Void, Void, Void> {
        private boolean stationFound=false;
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            data.setText("Station ID is "+foundStationID);
            if (stationFound) {
            NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);


            }
        }

        private static final String TAG = "MyActivity";
        private String foundStationID;

        @Override
        protected Void doInBackground(Void... voids) {
            StationDataMember temp;


            while (!stationFound) {
                foundStationID = "testvalue";
                for (int i = 0; i<MainActivity.StationDataList.size();i++) {
                    temp = (StationDataMember)MainActivity.StationDataList.get(i);
                    //Log.d(TAG, "station is "+temp.getDm_stopid() +" latitude result  "  +(MainActivity.latitude - Double.parseDouble(temp.getDm_latitude()))+ "longitude result: "+(MainActivity.longitude - Double.parseDouble(temp.getDm_longitude())));

                    stationFound = false;
                    if ((MainActivity.latitude - Double.parseDouble(temp.getDm_latitude())) > -0.00006 && (MainActivity.latitude - Double.parseDouble(temp.getDm_latitude())) < 0.00006) {
                        //Log.d(TAG,"latitude found." + temp.getDm_stopid() + " latitude calc = " +(MainActivity.latitude - Double.parseDouble(temp.getDm_latitude())));
                        if ((MainActivity.longitude - Double.parseDouble(temp.getDm_longitude())) > -0.00006 && (MainActivity.longitude - Double.parseDouble(temp.getDm_longitude())) < 0.00006) {
                            //Log.d(TAG,"longitude found, stationID " +temp.getDm_stopid() );
                            stationFound = true;
                            foundStationID = temp.getDm_stopid();
                            MainActivity.foundStationID = temp.getDm_stopid();
                            break;
                        }
                    }

                }
                SystemClock.sleep(1000);
                //break;

            }
            return null;
        }
    }
}