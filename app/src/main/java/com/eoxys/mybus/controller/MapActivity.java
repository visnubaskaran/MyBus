package com.eoxys.mybus.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eoxys.mybus.R;
import com.eoxys.mybus.model.Location_Coordinates;
import com.eoxys.mybus.model.Stop_list_item;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private static final String URL_DATA = "http://barcelonaapi.marcpous.com/bus/nearstation/latlon/41.3985182/2.1917991/1.json";

    List<Location_Coordinates> markersArray = new ArrayList<Location_Coordinates>();

    private Marker myMarker;

    Marker[] marker = new Marker[20];

    ImageButton imagemapButton;

    CameraUpdate cameraUpdate = null;

    private FusedLocationProviderClient mFusedLocationClient;

    private GoogleMap mMap;

    private static final float DEFAULT_ZOOM = 15f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        imagemapButton = (ImageButton) findViewById(R.id.imagemapButton);

        imagemapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
                mMap.setMyLocationEnabled(true);
            }
        });

        loadJSONData();

    }

    private void getDeviceLocation(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Task location = mFusedLocationClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Location currentLoaction = (Location) task.getResult();

                    moveCamera(new LatLng(currentLoaction.getLatitude(),currentLoaction.getLongitude()),DEFAULT_ZOOM);
                }
            }
        });
    }

    private void moveCamera(LatLng latLng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    private void loadJSONData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject respJson = new JSONObject(response);
                            JSONObject data = respJson.getJSONObject("data");
                            JSONArray stationarray = data.getJSONArray("nearstations");

                            for(int i=0; i<stationarray.length();i++){
                                JSONObject o = stationarray.getJSONObject(i);
                                Location_Coordinates list_item = new Location_Coordinates(o.getString("street_name"),
                                        o.getDouble("lat"),
                                        o.getDouble("lon"));

                                markersArray.add(list_item);
                            }

                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                            mapFragment.getMapAsync(MapActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;

        float zoomLevel = 14.0f;

        map.setOnMarkerClickListener(this);

        map.setOnInfoWindowClickListener(this);

        for(int i = 0 ; i < markersArray.size() ; i++) {

            LatLng latLng = new LatLng(markersArray.get(i).getlat(), markersArray.get(i).getlon());

            myMarker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(markersArray.get(i).getlat(), markersArray.get(i).getlon()))
                    .title(markersArray.get(i).getStationname()));

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        Intent intent = new Intent(MapActivity.this, PhotoActivity.class);
        intent.putExtra("stop_name", marker.getTitle().toString());
        startActivity(intent);
    }
}
