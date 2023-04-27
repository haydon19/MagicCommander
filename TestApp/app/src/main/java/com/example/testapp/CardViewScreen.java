package com.example.testapp;

import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class CardViewScreen extends Fragment {


    public final String title;
    //Visual Components
    public SearchView cardSearch;
    public ImageView cardImageView;
    public RecyclerView cardView;
    public SearchBarAdapter adapter;

    //SearchBar Variables
    public String searchQuery;
    public ArrayList<MagicCard> itemList;
    public final String scryfallQuery = "https://scryfall.com/search?q=";

    //Adapters



    public CardViewScreen(String title){
        this.title = title;
        itemList = new ArrayList<>();
        searchQuery = "";

    }


    //Gets the card image link.
    //This link is used to display the cards image.
    public Bitmap getCardImage(Document doc){
        Bitmap cardImage = null;
        Elements image = doc.getElementsByTag("img");
        try {
            URL cardUrl = new URL(Objects.requireNonNull(image.first()).attr("src"));
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
        cardImageView = root.findViewById(R.id.cardImageView);
        cardView = root.findViewById(R.id.cardSearchView);
        cardView.setLayoutManager(new LinearLayoutManager(getContext()));
        cardView.setHasFixedSize(false);

        //Set cardImageView visibility
        if(cardImageView.getDrawable() == null){
            cardImageView.setVisibility(View.GONE);
        }

        //SearchBar listeners
        cardSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchForCard(query);
                adapter.notifyDataSetChanged();
                cardSearch.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        dataInitialized();

        adapter = new SearchBarAdapter(getContext(), itemList);
        cardView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void dataInitialized(){
        itemList = new ArrayList<MagicCard>();
    }

    public void searchForCard(String query){
        //Clear the item list. Notify the adapter of changes.
        itemList.clear();
        adapter.notifyItemRemoved(0);
        //Hide the card image, we are trying to find a new card.
        cardImageView.setVisibility(View.GONE);
        //Recycler View set to visible.
        cardView.setVisibility(View.VISIBLE);
        List<MagicCard> cards = new ArrayList<MagicCard>();
        try {
            Log.d("tag","Scraping card data...");
            cards = new GetListofCards().execute(query).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(!cards.isEmpty()) {
            itemList.addAll(cards);
        }

    }

    private class GetListofCards extends AsyncTask<String, Void, List<MagicCard>>{

        List<MagicCard> cardTitles = new ArrayList<MagicCard>();

        protected List<MagicCard> doInBackground(String...queries) {

            // implement API in background and store the response in current variable
            String title = null;
            int index = 0;
            try {
                Document listOfCardsDocument = Jsoup.connect(scryfallQuery + queries[0]).get();

                //If more than one card matches this description...
                if(listOfCardsDocument.baseUri().contains("search?q=")){
                    Elements cards = listOfCardsDocument.getElementsByClass("card-grid-item");
                    int listSize = 5;
                    while(index < listSize && index < cards.size()){
                        MagicCard card = new MagicCard("");
                        String href = cards.get(index).text();
                        Log.d("debug","List of cards: " + href);
                        try{
                            listOfCardsDocument = Jsoup.connect(scryfallQuery + href).get();
                            Log.d("tag", "Found: " + title);
                            title = getCardTitle(listOfCardsDocument);
                            if (title != null) {
                                card.cardTitle = title;
                                cardTitles.add(card);
                            }
                            index++;
                        }catch(Exception e){
                            Log.e("Error", e.getMessage());
                        }
                    }
                }else{
                    //We found the card we are looking for...
                    try {
                        Document singleCardDocument = Jsoup.connect(scryfallQuery + queries[0]).get();
                        MagicCard card = new MagicCard("");
                        title = getCardTitle(singleCardDocument);
                        card.cardTitle = title;
                        cardTitles.add(card);

                    } catch (Exception e){
                        Log.e("Error", e.getMessage());
                    }
                }
            } catch (Exception e){
                Log.e("Error", e.getMessage());
            }


            Log.d("tag","Finished getting list of cards...");

            return cardTitles;
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

        Context context;
        private List<MagicCard> mMagicCardList;

        //Constructor for adapter.
        public SearchBarAdapter(Context context, ArrayList<MagicCard> magicCards){
            this.context = context;
            this.mMagicCardList = magicCards;

        }


        public class ViewHolder extends RecyclerView.ViewHolder{

            private final TextView cardHeading;

            public ViewHolder(View view){
                super(view);
                cardHeading = (TextView) view.findViewById(R.id.cardHeading);
            }

            public TextView getTextView()
            {
                return cardHeading;
            }
        }

        //Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
           //Create a View
            View v = LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
            return new ViewHolder(v);
        }

        //Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position){

            MagicCard magicCard = mMagicCardList.get(position);

            viewHolder.cardHeading.setText(magicCard.cardTitle);
            int selectedPos = RecyclerView.NO_POSITION;
            viewHolder.itemView.setSelected(selectedPos == position);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){

                    try {
                        magicCard.cardImage = new DownloadCardImage().execute(magicCard.cardTitle).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //Set the image
                    cardImageView.setImageBitmap(magicCard.cardImage);
                    //Make the image visible
                    cardImageView.setVisibility(View.VISIBLE);
                    //Hide the recycler view.
                    cardView.setVisibility(View.GONE);
                }
            });
        }

        //Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount(){
            return mMagicCardList.size();
        }
    }

}


