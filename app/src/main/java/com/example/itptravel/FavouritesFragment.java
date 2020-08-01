package com.example.itptravel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class FavouritesFragment extends Fragment {
    private static final String TAG = "FavFragment";
    private Spinner spn_favs;
    private Button btn_frag3Go;
    private Button btn_frag3Delete;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btn_frag3Go = (Button)view.findViewById(R.id.btn_frag3Go);
        btn_frag3Delete = (Button)view.findViewById(R.id.btn_frag3Delete);
        spn_favs = (Spinner) view.findViewById(R.id.spn_favouriteStations);

        spn_favs.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, MainActivity.favouriteStations));
        spn_favs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.foundStationID = MainActivity.favouriteStations.get(i);
                Log.d(TAG,"Selected station is: "+MainActivity.foundStationID);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_frag3Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.foundStationID!=null) {
                    NavHostFragment.findNavController(FavouritesFragment.this).navigate(R.id.action_FavouritesFragment_to_SecondFragment);
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "Nothing selected.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_frag3Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.foundStationID!="") {
                    //delete from favourites
                    for (int i=0;i<MainActivity.favouriteStations.size();i++) {
                        String temp= MainActivity.favouriteStations.get(i);

                        if (MainActivity.foundStationID==temp) {
                            MainActivity.favouriteStations.remove(i);
                            Toast.makeText(getActivity().getApplicationContext(),"Station deleted.",Toast.LENGTH_SHORT).show();
                            writeToFile();
                        }
                    }
                }
                else {

                    Toast.makeText(getActivity().getApplicationContext(), "Nothing selected.", Toast.LENGTH_SHORT).show();
                }
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


}
