package com.example.thatsmepratik.moviebuddies.fragment;

/**
 * Created by pratikgarala on 10/05/2016.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thatsmepratik.moviebuddies.R;


public class BuddiesFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buddies,container,false);
        return view;
    }
}
