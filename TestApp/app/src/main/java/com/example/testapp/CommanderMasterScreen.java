package com.example.testapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.provider.SyncStateContract;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    public int counterIndex = 0;
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

        this.health = 40;
        this.commanderDamage = 0;
        this.poisonCounters = 1;
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
        symbolView.setImageResource(R.drawable.heart_icon);
        //Add recentOperation View
        recentOperationView = root.findViewById(R.id.recentOperationView);
        recentOperationView.setText(Integer.toString(0));

        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {

                        final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MIN_DISTANCE
                                    && velocityY < SWIPE_THRESHOLD_VELOCITY) {
                                Log.e("debug", "Down to Up");
                                if(counterIndex >= 2){
                                    counterIndex = 0;
                                }else{
                                    counterIndex++;
                                }
                                swapCounter(counterIndex);
                            }
                            else if(Math.abs(e2.getY() - e1.getY()) > SWIPE_MIN_DISTANCE
                                    && velocityY > SWIPE_THRESHOLD_VELOCITY) {
                                Log.e("debug", "Up to Down");
                                if(counterIndex <= 0){
                                    counterIndex = 2;
                                }else{
                                    counterIndex--;
                                }
                                swapCounter(counterIndex);
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });



        return root;
    }


    public void swapCounter(int index){
        switch(index){
            case(0):
                //set symbol view.
                symbolView.setImageResource(R.drawable.heart_icon);
                counterTextView.setText(Integer.toString(health));
                break;
            case(1):
                //set symbol view.
                symbolView.setImageResource(R.drawable.commander_icon);
                counterTextView.setText(Integer.toString(commanderDamage));
                break;
            case(2):
                //set symbol view.
                symbolView.setImageResource(R.drawable.poison_icon);
                counterTextView.setText(Integer.toString(poisonCounters));
                break;
        }
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