package com.mashkarharis.itrack404.Services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.mashkarharis.itrack404.Centralized.Time_Location;
import com.mashkarharis.itrack404.MainActivity;
import com.mashkarharis.itrack404.R;

import java.sql.Time;
import java.time.Instant;
import java.util.List;

public class LocationChecker extends Service {

    private LocationManager mLocationManager;
    private Location location;
    public static final String CHANNEL_ID = "CH9182";
    int i=0;
    List<String> providers;

    public LocationChecker() {

    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
    private Notification getMyNotification(String Content){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("WE TRY TO COLLECT YOUR LOCATION").setSmallIcon(R.drawable.ic_home_black_24dp)
                .setContentIntent(pendingIntent).setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(Content))
                .build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(
                intent,flags,startId
        );
        createNotificationChannel();
        Notification notification = getMyNotification("READY");

        startForeground(1, notification);

        this.mLocationManager= (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        this.providers = mLocationManager.getProviders(true);


        Thread t = new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                super.run();

                while (true) {
                    
                    Object[] fetchdata= getLastKnownLocation();
                    location =(Location) fetchdata[1];
                    long time = (Long) fetchdata[0];
                    long last_remote_update = (Long) fetchdata[2];
                    System.out.println("LOC ::: "+time+" - - - " + location);
                    if(location!=null){
                        i=i+1;
                        String content = "Please keep your data and location ON.\nLast Fetched Time : "+time+"\nLast Remote Update : "+last_remote_update+"\nLast Know Location : ("+location.getLatitude()+
                                ","+location.getLongitude()+")\nLoop : "+i ;
                        Notification notification = getMyNotification(content);

                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(1, notification);

                    }
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
        return START_NOT_STICKY;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private Object[] getLastKnownLocation(){
        Location bestLocation = null;
        long now = Instant.now().toEpochMilli();
        long last_database_updated=-1;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l != null) {
                bestLocation = l;
                now=Instant.now().toEpochMilli();
                System.out.println("I am Running");
                last_database_updated= Time_Location.update_remote(l,now,getApplicationContext());
            }
        }
        return new Object[]{now,bestLocation,last_database_updated};
    }



}
