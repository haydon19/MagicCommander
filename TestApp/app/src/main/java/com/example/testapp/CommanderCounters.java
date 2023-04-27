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

    public String getCommanderCounterText(int counterIndex){
        switch (counterIndex){
            case(0):
                return String.valueOf(getCommanderHealth());
            case(1):
                return String.valueOf(getCommanderDamage());
            case(2):
                return String.valueOf(getCommanderPoison());
        }
        return "0";
    }

    public int getCommanderCounterImage(int counterIndex){
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

    public int getCommanderHealth(){ return commanderHealth; }

    public void setCommanderHealth(int health){
        commanderHealth = health;
    }

    public int getCommanderDamage(){ return commanderDamage; }

    public void setCommanderDamage(int damage){
        commanderDamage = damage;
    }

    public int getCommanderPoison(){ return commanderPoison; }

    public void setCommanderPoison(int poison){
        commanderPoison = poison;
    }

    public void setCounterImage(int image){
        counterTypeImage = image;
    }

    public boolean poisonDeath(){
        return commanderPoison >= maxCommanderPoison;
    }

    public boolean commanderDeath(){
        return commanderDamage >= maxCommanderDamage;
    }
    //endregion

    //region Methods
    public void addCommanderCounter(int counterIndex){
        switch(counterIndex){
            case(0):
                setCommanderHealth(getCommanderHealth() + 1);
                break;
            case(1):
                setCommanderDamage(getCommanderDamage() + 1);
                break;
            case(2):
                setCommanderPoison(getCommanderPoison() + 1);
                break;
        }
    }

    public void subtractCommanderCounter(int counterIndex){
        switch(counterIndex){
            case(0):
                setCommanderHealth(getCommanderHealth() - 1);
                break;
            case(1):
                setCommanderDamage(getCommanderDamage() - 1);
                break;
            case(2):
                setCommanderPoison(getCommanderPoison() - 1);
                break;
        }
    }
    //endregion
}


