package com.mashkarharis.itrack404.Centralized;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mashkarharis.itrack404.POJO.LocationsToFirebase;
import com.mashkarharis.itrack404.POJO.OneLocation;
import com.mashkarharis.itrack404.R;

import org.json.JSONArray;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;


public class Time_Location {

    public static Location location;
    public static long location_fetched_time;
    public static long last_remote_update=-1;

    private static boolean firebase_update(Location lc,long lft,Context applicationContext){
        try{
            String device_id= Settings.Secure.getString(applicationContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance(applicationContext.getString(R.string.firebaseurl));
            DatabaseReference myRef = database.getReference("UserData");
            ValueEventListener eventListener=new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                       Iterable<DataSnapshot> fetchedlocs=dataSnapshot.child(device_id.toString()).child("recentloc").getChildren();
                        for (DataSnapshot locs:fetchedlocs) {
                            Log.i("LOCFETCH", locs.getValue().toString());
                            Log.i("TIME", Long.parseLong(locs.child("time").getValue().toString()) + "");

                            Long time = Long.parseLong(locs.child("time").getValue().toString());
                            Double latitude = Double.parseDouble(locs.child("latitude").getValue().toString());
                            Double longitude = Double.parseDouble(locs.child("longitude").getValue().toString());

                            OneLocation oneloc = new OneLocation();
                            oneloc.setTime(time);
                            oneloc.setLatitude(latitude);
                            oneloc.setLongitude(longitude);

                            LocationsToFirebase.addRecentloc(oneloc);
                        }




                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("ERR ::::::::::::::::::::");
                }
            };

           // System.out.println(myRef.child("4").);
            myRef.addListenerForSingleValueEvent(eventListener);
            myRef.removeEventListener(eventListener);

            OneLocation newloc=new OneLocation();
            newloc.setTime(lft);
            newloc.setLongitude(lc.getLongitude());
            newloc.setLatitude(lc.getLatitude());
            LocationsToFirebase.addRecentloc(newloc);

            LocationsToFirebase.printme();
            myRef.child(device_id.toString()).child("recentloc").setValue(LocationsToFirebase.getRecentloc());

            System.out.println("FireBase Updating ... ");
            return true;
        }catch(Exception ex){
            System.out.println("ERR434 - "+ex.toString());
            return false;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long update_remote(Location lc, long lft, Context applicationContext){
        location=lc;
        location_fetched_time=lft;
        if(firebase_update(lc,lft,applicationContext)){
            last_remote_update= Instant.now().toEpochMilli();
        }
        return last_remote_update;
    }

    public static Location getLocation() {
        return location;
    }

    public static long getLocation_fetched_time() {
        return location_fetched_time;
    }

    public static long getLast_remote_update() {
        return last_remote_update;
    }
}
