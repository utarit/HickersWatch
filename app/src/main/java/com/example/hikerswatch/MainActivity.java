package com.example.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation != null) {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    public void startListening() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    public void updateLocationInfo(Location location){
        TextView lat = findViewById(R.id.latitudeView);
        TextView log = findViewById(R.id.longtitudeView);
        TextView alt = findViewById(R.id.altitudeView);
        TextView acc = findViewById(R.id.AccuracyView);
        TextView add = findViewById(R.id.addressView);

        lat.setText(String.format("Latitude: %f", location.getLatitude()));
        log.setText(String.format("Longtitude: %f", location.getLongitude()));
        alt.setText(String.format("Altitude: %f", location.getAltitude()));
        acc.setText(String.format("Accuracy: %f", location.getAccuracy()));

        String address = "Could not find damnn";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try{
            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if(listAddresses != null && listAddresses.size() > 0) {
                address = "Address: \n";

                if(listAddresses.get(0).getThoroughfare() != null) {
                    address += listAddresses.get(0).getThoroughfare() + "\n";
                }

                if(listAddresses.get(0).getLocality() != null) {
                    address += listAddresses.get(0).getLocality() + "\n";
                }

                if(listAddresses.get(0).getPostalCode() != null) {
                    address += listAddresses.get(0).getPostalCode() + "\n";
                }
                if(listAddresses.get(0).getAdminArea() != null) {
                    address += listAddresses.get(0).getAdminArea() + "\n";
                }


            }
        } catch (Exception e){
            e.printStackTrace();
        }

        add.setText(address);
    }
}
