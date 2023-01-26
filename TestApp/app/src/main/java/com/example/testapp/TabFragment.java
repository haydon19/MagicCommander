package com.example.testapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TabFragment extends Fragment {


    private final String title;
    public CommanderMasterScreen commanderScreen;

    public TabFragment(String title) {
        // Required empty public constructor
        this.title = title;
        commanderScreen = new CommanderMasterScreen(title);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_tab, container, false);
        TextView tabTextView = root.findViewById(R.id.tabTextView);
        tabTextView.setText(title);
        return root;
    }
}