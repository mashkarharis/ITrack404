package com.mashkarharis.itrack404.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

public class HomeViewModel extends ViewModel {

    private HashMap<String,String> hash_map;

    public HomeViewModel() {
        hash_map = new HashMap<String,String>();
    }

    public HashMap<String,String> getData() {
        return hash_map;
    }
}