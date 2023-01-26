package com.example.testapp;

import android.graphics.Bitmap;

public class MagicCard {

    public String cardTitle;
    public Bitmap cardImage;


    public MagicCard(){
        cardTitle = "";
        cardImage = null;
    }

    public MagicCard(String title){
        cardTitle = title;
        cardImage = null;
    }

    public MagicCard(String title, Bitmap image){
        cardTitle = title;
        cardImage = image;
    }

    public String getCardTitle(){
        if(cardTitle != null) {
            return cardTitle;
        }else{
            return "";
        }
    }

    public Bitmap getCardImage(){
        if(cardImage != null) {
            return cardImage;
        }else{
            return null;
        }
    }

}
