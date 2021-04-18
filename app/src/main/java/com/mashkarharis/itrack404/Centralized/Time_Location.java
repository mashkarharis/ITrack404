package com.mashkarharis.itrack404.Centralized;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

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
            OneLocation oneloc=new OneLocation();
            oneloc.setLatitude(lc.getLatitude());
            oneloc.setLongitude(lc.getLongitude());
            oneloc.setTime(lft);

            LocationsToFirebase firelocations=new LocationsToFirebase();

            ValueEventListener eventListener=new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                         //OneLocation loc = (ArrayList<>) snapshot.getValue();
                        System.out.println(((HashMap)((ArrayList)snapshot.getValue()).get(0)).get("latitude"));
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("ERR ::::::::::::::::::::");
                }
            };

            myRef.addListenerForSingleValueEvent(eventListener);
            firelocations.addRecentloc(oneloc);

            myRef.setValue(firelocations);
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
