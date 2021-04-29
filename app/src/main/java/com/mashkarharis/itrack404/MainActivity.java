package com.mashkarharis.itrack404;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mashkarharis.itrack404.Services.LocationChecker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    private final int REQUESTCODE=121;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUESTCODE);
        }
        setContentView(R.layout.activity_main);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
      //  getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Home</font>"));
       // getActionBar().setTitle("I'M Here");

    }

    @Override
    protected void onResume() {
        super.onResume();
        StartLocationChecking(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        StartLocationChecking(this);

    }

    private void StartLocationChecking(MainActivity mainActivity) {
        Intent serviceIntent = new Intent(this, LocationChecker.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        System.out.println("CAME HERE");
        switch (requestCode) {
            case REQUESTCODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                    System.out.println("ACCEPTED");
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                    System.out.println("DENIED");
                    finishAffinity();
                    System.exit(0);
                }
            }
        }
    }


}