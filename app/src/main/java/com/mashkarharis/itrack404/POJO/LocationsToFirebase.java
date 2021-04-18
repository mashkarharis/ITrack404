package com.mashkarharis.itrack404.POJO;

import android.location.Location;

import java.util.ArrayList;

public class LocationsToFirebase {
    public ArrayList<OneLocation> recentloc=new ArrayList<>();

    public ArrayList<OneLocation> getRecentloc() {
        return recentloc;
    }

    public void addRecentloc(OneLocation loc) {
        if(this.recentloc.size()>=10){
            this.recentloc.remove(0);
            this.recentloc.add(loc);
            return;
        }
        recentloc.add(loc);
        return;
    }
}
