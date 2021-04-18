package com.mashkarharis.itrack404.Centralized;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import java.time.Instant;


public class Time_Location {

    public static Location location;
    public static long location_fetched_time;
    public static long last_remote_update=-1;

    private static boolean firebase_update(Location lc,long lft,Context applicationContext){
        try{
            String device_id= Settings.Secure.getString(applicationContext.getContentResolver(), Settings.Secure.ANDROID_ID);

            System.out.println("FireBase Updating ... ");
            return true;
        }catch(Exception ex){
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
