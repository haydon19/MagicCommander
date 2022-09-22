package com.example.testapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class CommanderMasterScreen extends Fragment {

    //Global variables
    public final String title;
    public int health = 40;
    public int commanderDamage;
    public int poisonCounters;

    //Max variables
    int maxCommanderDamage = 21;
    int maxPoisonCounters = 10;

    //Components
    TextView counterTextView;
    Button addButton;
    Button subtractButton;
    ImageView symbolView;
    TextView recentOperationView;
    CountDownTimer recentOperationTimer;

    //Possibly give a game state to start at default settings in parameters.
    public CommanderMasterScreen(String title) {

        this.title = title;

        health = 40;
        commanderDamage = 0;
        poisonCounters = 0;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_commander_master_screen, container, false);
        //Add components

        counterTextView = root.findViewById(R.id.counterTextView);
        //TextView to only accept numbers.
        counterTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
        counterTextView.setText(Integer.toString(health));

        //Add Buttons
        addButton = root.findViewById(R.id.addButton);
        addButton.setText("+");

        addButton.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            public void onClick(View v){
                counterTextView.setText((Integer.parseInt(counterTextView.getText().toString()) + 1) + "");
                setUpTimer();
                recentOperationView.setText((Integer.parseInt(recentOperationView.getText().toString()) + 1) + "");
            }
        });


        subtractButton = root.findViewById(R.id.subtractButton);
        subtractButton.setText("-");

        subtractButton.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            public void onClick(View v){
                counterTextView.setText((Integer.parseInt(counterTextView.getText().toString()) - 1) + "");
                setUpTimer();
                recentOperationView.setText((Integer.parseInt(recentOperationView.getText().toString()) - 1) + "");
            }
        });

        //Add Symbol Image
        symbolView = root.findViewById(R.id.symbolView);

        //Add recentOperation View
        recentOperationView = root.findViewById(R.id.recentOperationView);
        recentOperationView.setText(Integer.toString(0));


        return root;
    }

    public void setUpTimer(){
        //If timer is already going, restart it.
        if(recentOperationTimer != null){
            recentOperationTimer.cancel();
        }
        recentOperationTimer = new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                recentOperationView.setVisibility(View.VISIBLE);
            }

            public void onFinish() {
                recentOperationView.setVisibility(View.INVISIBLE);
                recentOperationView.setText("0");
            }

        }.start();
    }


}