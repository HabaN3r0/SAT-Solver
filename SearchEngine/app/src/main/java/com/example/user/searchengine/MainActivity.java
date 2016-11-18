package com.example.user.searchengine;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Button enter;
    private RadioGroup rgrp;
    private RadioButton rmap;
    private RadioButton rsat;
    private EditText search_bar;
    private TextView result_bar;
    private String input;
    public String result;
    private String[][] dictionary = {{"Sentosa", "1.2494041", "103.830321", "RWS", "Resort World Sentosa"}, {"Marina Bay Sands", "1.2828484", "103.8609294", "Marina Bay", "Bay Sands", "Marina", "MBS"}, {"Singapore Flyer", "1.2895301", "103.8632483", "Flyer"}, {"Singapore Zoo", "1.352083", "103.819836", "Zoo"}, {"Vivo City", "1.26463", "103.8207793", "Vivo"}, {"Buddha Tooth Relic Temple", "1.2815901", "103.8443033", "Buddha", "Tooth Relic Temple", "Relic Temple"}, {"Supreme Court & City Hall", "1.2899018", "103.8509197", "City Hall", "Court", "Supreme Court", "Singapore Court"}};
    private GoogleMap mMap;
    private double lat = 1.3521;
    private double lng = 103.8198;
    private LatLng latlng = new LatLng(lat,lng);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enter = (Button) findViewById(R.id.enter);
        search_bar = (EditText) findViewById(R.id.search_bar);
        result_bar = (TextView) findViewById(R.id.results);
        rgrp = (RadioGroup) findViewById(R.id.radio_grp);
        rmap = (RadioButton) findViewById(R.id.map_view);
        rsat = (RadioButton) findViewById(R.id.sat_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == enter) {
                    input = search_bar.getText().toString();
                    result = compare(dictionary, input);
                    result_bar.setText(result);
                    mMap.addMarker(new MarkerOptions().position(latlng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                }
            }
        });
        rmap.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (v == rmap){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });
        rsat.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (v == rsat){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(latlng));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public String compare(String[][] dictionary, String input) {
        String val = "";
        int distance = 0;
        int min = 100;
        int list_no = 0;
        for(int i = 0; i<dictionary.length; i++) {
            for (String str : dictionary[i]) {
                distance = LD(str, input);
                if (min > distance) {
                    min = distance;
                    list_no = i;
                }
            }
        }
        lat = Double.parseDouble(dictionary[list_no][1]);
        lng = Double.parseDouble(dictionary[list_no][2]);
        val = dictionary[list_no][0];
        latlng = new LatLng(lat,lng);
        return val;
    }

    public int LD(String s, String t) {
        int d[][]; // matrix
        int n; // length of s
        int m; // length of t
        int i; // iterates through s
        int j; // iterates through t
        char s_i; // ith character of s
        char t_j; // jth character of t
        int cost; // cost

        // Step 1

        n = s.length();
        m = t.length();
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];

        // Step 2

        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }

        // Step 3

        for (i = 1; i <= n; i++) {

            s_i = s.charAt(i - 1);

            // Step 4

            for (j = 1; j <= m; j++) {

                t_j = t.charAt(j - 1);

                // Step 5

                if (s_i == t_j) {
                    cost = 0;
                } else {
                    cost = 1;
                }

                // Step 6

                d[i][j] = Minimum(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);

            }

        }

        // Step 7

        return d[n][m];

    }


    private int Minimum(int a, int b, int c) {
        int mi;

        mi = a;
        if (b < mi) mi = b;
        if (c < mi) mi = c;

        return mi;
    }

}