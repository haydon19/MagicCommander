package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    String[] tabNames = {"Tab 1", "Tab 2", "Tab 3", "Tab 4"};
    String[] healthTabs = {"Commander Health","Commander Damage","Poison Damage"};
    EditText editText;

    //Main App activities created on startup.

    //4 tabs for commander (should be text changeable).
    //On each tab should be:
    //->Health
    //->
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the ViewPager Views (Each view is attached to a tab)
        ViewPager2 sampleViewPager = findViewById(R.id.sampleViewPager);
        sampleViewPager.setAdapter(
                new SampleAdapter(this)
        );
        //Set tabs
        setUpTabs(sampleViewPager);

        //ViewPager2 - Component Section

        //Scroll down features in order
        //Display Life


        //Display Commander Card


    }


    //Tab Section ---------
    private void setUpTabs(ViewPager2 sampleViewPager){
        //Set the Tab Layout
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(
                tabLayout,
                sampleViewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(tabNames[position]);
                        tab.setIcon(R.drawable.ic_launcher_background);
                    }
                }
        ).attach();

        //Set Listeners on each individual tab
        LinearLayout tabStrip = (LinearLayout) tabLayout.getChildAt(0);

        for(int i=0;i<tabStrip.getChildCount();i++){
            //Set LongClickListener to each tab
            int currentTab = i;
            tabStrip.getChildAt(i).setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view) {
                    Log.d("Long click Action", "Long Click on tab!");
                    TabLayout.Tab tab = tabLayout.getTabAt(currentTab);
                    popUpEditTabText(tab);
                    return true;
                }
            });
        }
    }
    private void popUpEditTabText(TabLayout.Tab tab){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name of Commander:");

        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tab.setText(input.getText());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    //---------------------

    class SampleAdapter extends FragmentStateAdapter{

        public SampleAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        public SampleAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        public SampleAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return new TabFragment(tabNames[position]);
        }

        @Override
        public int getItemCount() {
            return tabNames.length;
        }
    }

}