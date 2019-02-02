/*
package com.bitehunter.bitehunter.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bitehunter.bitehunter.Model.Meal;
import com.bitehunter.bitehunter.Others.CustomVolleyRequest;
import com.bitehunter.bitehunter.R;

import java.util.List;
*/
/**
 * Created by Jevin on 26-Dec-17.
 *//*


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    //Imageloader to load restaurant_image
    private ImageLoader imageLoader;
    private Context context;

    //List to store all superheroes
    //List<SuperHero> superHeroes;
    List<Meal> mealList;

    //Constructor of this class
    public CardAdapter(List<Meal> mealList, Context context){
        super();
        //Getting all superheroes
        this.mealList = mealList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.superheroes_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Getting the particular item from the list
       // SuperHero superHero =  superHeroes.get(position);
        Meal meal =  mealList.get(position);

        //Loading restaurant_image from url
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(meal.getImage(), ImageLoader.getImageListener(holder.imageView, R.drawable.img_white, android.R.drawable.ic_dialog_alert));

        //Showing data on the views
        holder.imageView.setImageUrl(meal.getImage(), imageLoader);
        holder.textViewName.setText(meal.getName());
        holder.textViewPublisher.setText(meal.getPrice());

    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public NetworkImageView imageView;
        public TextView textViewName;
        public TextView textViewPublisher;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (NetworkImageView) itemView.findViewById(R.id.imageViewHero);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewPublisher = (TextView) itemView.findViewById(R.id.textViewPublisher);
        }
    }
}*/
