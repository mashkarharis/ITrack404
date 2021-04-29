package com.mashkarharis.itrack404.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mashkarharis.itrack404.POJO.LocationsFromFirebase;
import com.mashkarharis.itrack404.POJO.LocationsToFirebase;
import com.mashkarharis.itrack404.POJO.OneLocation;
import com.mashkarharis.itrack404.R;

import java.util.ArrayList;

public class DashboardFragment extends Fragment  implements OnMapReadyCallback {

    private DashboardViewModel dashboardViewModel;
    MapView mapView;
    GoogleMap map;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);


        mapView = (MapView) root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);



        Button fetch=(Button) root.findViewById(R.id.FETCH);
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String device_id= Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

                    FirebaseDatabase database = FirebaseDatabase.getInstance(getActivity().getString(R.string.firebaseurl));
                    DatabaseReference myRef = database.getReference("UserData");
                    ValueEventListener eventListener=new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Iterable<DataSnapshot> fetchedlocs=dataSnapshot.child(device_id.toString()).child("recentloc").getChildren();
                            for (DataSnapshot locs:fetchedlocs) {
                                Log.i("FETCHING....", locs.getValue().toString());
                                Log.i("TIME FETCHING ...", Long.parseLong(locs.child("time").getValue().toString()) + "");

                                Long time = Long.parseLong(locs.child("time").getValue().toString());
                                Double latitude = Double.parseDouble(locs.child("latitude").getValue().toString());
                                Double longitude = Double.parseDouble(locs.child("longitude").getValue().toString());

                                OneLocation oneloc = new OneLocation();
                                oneloc.setTime(time);
                                oneloc.setLatitude(latitude);
                                oneloc.setLongitude(longitude);

                                LocationsFromFirebase.addRecentloc(oneloc);
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
                    ArrayList<Marker> mkr=new ArrayList<>();
                    LatLng pos=null;
                    //String text="";
                    int i=1;
                    map.clear();
                    PolylineOptions polyops=new PolylineOptions();
                    polyops.width(5).color(Color.BLUE).geodesic(true);
                    for (OneLocation oneloc:LocationsFromFirebase.getRecentloc()) {
                        pos = new LatLng(oneloc.getLatitude(),oneloc.getLongitude());
                        polyops.add(pos);
                        Marker TP = map.addMarker(new MarkerOptions().position(pos).title(i+""));
                        mkr.add(TP);

                        i=i+1;
                    }
                    map.addPolyline(polyops);


                    //locations.setText(text);

                    System.out.println("FireBase Fetching ... ");
                }catch(Exception ex){
                    System.out.println("ERR434 - "+ex.toString());
                }
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        //map.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint));
    }
}