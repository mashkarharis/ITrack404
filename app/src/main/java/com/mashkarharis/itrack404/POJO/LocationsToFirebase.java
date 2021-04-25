package com.mashkarharis.itrack404.POJO;

import android.location.Location;

import java.util.ArrayList;

public class LocationsToFirebase {
    public static ArrayList<OneLocation> recentloc=new ArrayList<>();

    public static ArrayList<OneLocation> getRecentloc() {
        return recentloc;
    }

    public static void addRecentloc(OneLocation loc) {
        if(recentloc.size()>=10){
            recentloc.remove(0);
            recentloc.add(loc);
            return;
        }
        recentloc.add(loc);


        return;
    }
    public static void printme(){
        for (OneLocation one:recentloc) {
            System.out.println("#---->>>"+one.getTime());
        }
    }
}
