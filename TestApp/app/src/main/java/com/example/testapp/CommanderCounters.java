package com.example.testapp;

import android.widget.ImageView;

public class CommanderCounters {

    int commanderHealth = 40;
    int commanderDamage = 0;
    int commanderPoison = 0;

    int maxCommanderDamage = 21;
    int maxCommanderPoison = 10;

    int counterTypeImage;

    int healthSymbol = R.drawable.heart_icon;
    int damageSymbol = R.drawable.commander_icon;
    int poisonSymbol = R.drawable.poison_icon;

    public CommanderCounters(){
        commanderHealth = 40;
        commanderDamage = 0;
        commanderPoison = 0;
        counterTypeImage = healthSymbol;
    }

    public CommanderCounters(int health, int damage, int poison, ImageView image){
        commanderHealth = health;
        commanderDamage = damage;
        commanderPoison = poison;
        counterTypeImage = healthSymbol;
    }

    //region Getters / Setters

    public String GetCommanderCounterText(int counterIndex){
        switch (counterIndex){
            case(0):
                return String.valueOf(GetCommanderHealth());
            case(1):
                return String.valueOf(GetCommanderDamage());
            case(2):
                return String.valueOf(GetCommanderPoison());
        }
        return "0";
    }

    public int GetCommanderCounterImage(int counterIndex){
        switch(counterIndex){
            case(0):
                return healthSymbol;
            case(1):
                return damageSymbol;
            case(2):
                return poisonSymbol;
        }

        return healthSymbol;
    }

    public int GetCommanderHealth(){ return commanderHealth; }

    public void SetCommanderHealth(int health){
        commanderHealth = health;
    }

    public int GetCommanderDamage(){ return commanderDamage; }

    public void SetCommanderDamage(int damage){
        commanderDamage = damage;
    }

    public int GetCommanderPoison(){ return commanderPoison; }

    public void SetCommanderPoison(int poison){
        commanderPoison = poison;
    }

    public void SetCounterImage(int image){
        counterTypeImage = image;
    }

    public boolean PoisonDeath(){
        return commanderPoison >= maxCommanderPoison;
    }

    public boolean CommanderDeath(){
        return commanderDamage >= maxCommanderDamage;
    }
    //endregion

    //region Methods
    public void AddCommanderCounter(int counterIndex){
        switch(counterIndex){
            case(0):
                SetCommanderHealth(GetCommanderHealth() + 1);
                break;
            case(1):
                SetCommanderDamage(GetCommanderDamage() + 1);
                break;
            case(2):
                SetCommanderPoison(GetCommanderPoison() + 1);
                break;
        }
    }

    public void SubtractCommanderCounter(int counterIndex){
        switch(counterIndex){
            case(0):
                SetCommanderHealth(GetCommanderHealth() - 1);
                break;
            case(1):
                SetCommanderDamage(GetCommanderDamage() - 1);
                break;
            case(2):
                SetCommanderPoison(GetCommanderPoison() - 1);
                break;
        }
    }
    //endregion
}


