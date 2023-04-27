package com.example.testapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
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
import java.util.Dictionary;

public class CommanderMasterScreen extends Fragment {

    //Global variables
    int counterIndex = 0;
    //Global Objects
    CommanderCounters _commanderCounters;

    //Components
    TextView counterTextView;
    Button addButton;
    Button subtractButton;
    ImageView symbolView;
    TextView recentOperationView;
    CountDownTimer recentOperationTimer;

    //Possibly give a game state to start at default settings in parameters.
    public CommanderMasterScreen(String title) {
        _commanderCounters = new CommanderCounters();
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
        counterTextView.setText(_commanderCounters.GetCommanderCounterText(counterIndex));

        //Add Buttons
        addButton = root.findViewById(R.id.addButton);
        addButton.setText("+");

        addButton.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            public void onClick(View v){
                _commanderCounters.AddCommanderCounter(counterIndex);
                counterTextView.setText(_commanderCounters.GetCommanderCounterText(counterIndex) + "");
                setUpTimer();
                recentOperationView.setText((Integer.parseInt(recentOperationView.getText().toString()) + 1) + "");
            }
        });


        subtractButton = root.findViewById(R.id.subtractButton);
        subtractButton.setText("-");

        subtractButton.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            public void onClick(View v){
                _commanderCounters.SubtractCommanderCounter(counterIndex);
                counterTextView.setText(_commanderCounters.GetCommanderCounterText(counterIndex) + "");
                setUpTimer();
                recentOperationView.setText((Integer.parseInt(recentOperationView.getText().toString()) - 1) + "");
            }
        });

        //Add Symbol Image
        symbolView = root.findViewById(R.id.symbolView);
        symbolView.setImageResource(_commanderCounters.GetCommanderCounterImage(counterIndex));
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
                        //end the recentOperationView timer.
                        endTimer();

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

        symbolView.setImageResource(_commanderCounters.GetCommanderCounterImage(index));
        counterTextView.setText(_commanderCounters.GetCommanderCounterText(index));
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

    public void endTimer(){
        if(recentOperationTimer != null){
            recentOperationTimer.cancel();
            recentOperationView.setVisibility(View.INVISIBLE);
            recentOperationView.setText("0");
        }
    }
}