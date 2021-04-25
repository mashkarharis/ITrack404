package com.mashkarharis.itrack404.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mashkarharis.itrack404.MainActivity;
import com.mashkarharis.itrack404.R;

import java.util.HashMap;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;


    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Get Device ID and Store in State
        String device_id=Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        homeViewModel.getData().put("device_id",device_id);
        System.out.println("device_id :"+device_id);

        // getActivity().getActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Home</font>"));


        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        // Load Saved State
        HashMap<String,String> data= new ViewModelProvider(this).get(HomeViewModel.class).getData();

        // Assign device_id
        TextView device_id=getActivity().findViewById(R.id.device_id_value);
        device_id.setText(data.get("device_id"));


    }

    @Override
    public void onPause() {
        super.onPause();
        HashMap<String,String> data = new ViewModelProvider(this).get(HomeViewModel.class).getData();

        // Get Device ID and Store in State
        TextView device_id=getActivity().findViewById(R.id.device_id_value);
        data.put("device_id",device_id.getText().toString());


    }
}