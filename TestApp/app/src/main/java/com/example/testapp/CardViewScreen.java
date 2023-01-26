package com.example.testapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class CardViewScreen extends Fragment {


    public final String title;
    //Visual Components
    public SearchView cardSearch;
    public ImageView cardImageView;
    public String cardTitle;
    public RecyclerView cardView;


    //SearchBar Variables
    public String searchQuery;
    public ArrayList<MagicCard> itemList;
    public String[] cardNames;
    public final String scryfallQuery = "https://scryfall.com/search?q=";


    //MagicCard Variables
    public Bitmap cardImage;
    //Adapters



    public CardViewScreen(String title){
        this.title = title;
        searchQuery = "";

    }


    //Gets the card image link.
    //This link is used to display the cards image.
    public Bitmap getCardImage(Document doc){
        Bitmap cardImage = null;
        Elements image = doc.getElementsByTag("img");
        try {
            URL cardUrl = new URL(image.first().attr("src"));
            cardImage = BitmapFactory.decodeStream(cardUrl.openConnection().getInputStream());
        } catch (Exception e) {
            Log.d("Error: ",e.toString());
        }

        //System.out.println(image.first().attr("src"));
        return cardImage;
    }

    //Get the cards top most name.
    public String getCardTitle(Document doc){
        String cardName = "";
        try {
            Elements name = doc.getElementsByClass("card-text-card-name");
            cardName = name.get(0).ownText();
        }
        catch(Exception ex) {
            System.out.println(ex.toString());
        }

        return cardName;
    }

    public void searchForCard(String query){

            new DownloadCardTitles().execute(query);

            /*
            Document document = Jsoup.connect(scryfallQuery + query).get();
            if(document.baseUri().contains("search?q=")){
                //This scrapes the cards displayed on scryfall (shown via a grid).
                Elements cards = document.getElementsByClass("card-grid-item");
                int index = 0;
                //Keep the list at less than 3 for now.
                while(index < 3 && index < cards.size()){
                    String href = cards.get(index).text();
                    try{
                        //1. Search for magic card in scryfall
                        //2. Get the magic cards title and image.
                        //3. Add magic card to the visible list of cards.
                        Document queryDoc = Jsoup.connect("https://scryfall.com/search?q=" + href).get();
                        cardTitle = getCardTitle(queryDoc);
                        cardImage = getCardImage(queryDoc);
                        //cardImageView.setImageBitmap(getCardImage(queryDoc));
                        MagicCard newCard = new MagicCard(cardTitle, cardImage);
                        itemList.add(newCard);
                        index++;
                    }catch(Exception ex){
                        System.out.println(ex.toString());
                    }
                }
            }else {
                try {
                    //We have found the exact card you are looking for.
                    cardTitle = getCardTitle(document);
                    cardImageView.setImageBitmap(getCardImage(document));
                }
                catch (Exception ex){
                    System.out.println(ex.toString());
                }
            }
            */
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_card_view_screen, container, false);

        //Component initialization
        cardSearch = root.findViewById(R.id.cardSearch);
        cardImageView = root.findViewById(R.id.cardImage);
        cardView = root.findViewById(R.id.cardSearchView);




        //SearchBar listeners
        cardSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchForCard(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return root;
    }


    private class DownloadCardTitles extends AsyncTask<String, Void, String> {

        String title;

        protected String doInBackground(String...queries) {
            // implement API in background and store the response in current variable

            String title = null;

            try {
                Document document = Jsoup.connect(scryfallQuery + queries[0]).get();
                title = getCardTitle(document);

            } catch (Exception e){
                Log.e("Error", e.getMessage());
            }


            Log.d("tag","DownloadCardTitle: " + title);
            return title;
        }

        protected void onPostExecute(String result) {

        }

    }




    public class DownloadCardImage extends AsyncTask<String, Integer, Bitmap> {

        ImageView bitmapImage;

        @Override
        protected Bitmap doInBackground(String...queries) {
            // implement API in background and store the response in current variable
            Bitmap icon = null;


            try {
                Document document = Jsoup.connect(scryfallQuery + queries[0]).get();
                icon = getCardImage(document);


            } catch (Exception e){
                Log.e("Error", e.getMessage());
            }

            return icon;
        }

        protected void onProgressUpdate(Integer... progress){
        }

        protected void onPostExecute(Long result) {
        }

    }

    public class SearchBarAdapter extends RecyclerView.Adapter<SearchBarAdapter.ViewHolder>{

        private String[] localDataSet;


        public class ViewHolder extends RecyclerView.ViewHolder{
            private final TextView textView;

            public ViewHolder(View view){
                super(view);
                textView = (TextView) view.findViewById(R.id.cardHeading);
            }

            public TextView getTextView(){
                return textView;
            }

        }

        //Initialize the dataset of the Adapter
        public SearchBarAdapter(String[] dataSet){
            localDataSet = dataSet;
        }

        //Create new views (invoked by the layour manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
            //Create a new view
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);

            return new ViewHolder(view);
        }

        //Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position){
            //Get element from your dataset at this position and replace the contents
            //of the view with that element
            viewHolder.getTextView().setText(localDataSet[position]);
        }

        //Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount(){
            return localDataSet.length;
        }
    }

}


