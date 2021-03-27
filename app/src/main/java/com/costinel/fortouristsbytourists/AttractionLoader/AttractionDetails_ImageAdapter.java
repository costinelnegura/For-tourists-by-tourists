package com.costinel.fortouristsbytourists.AttractionLoader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.costinel.fortouristsbytourists.AttractionDetails;
import com.costinel.fortouristsbytourists.Model.Attraction;
import com.costinel.fortouristsbytourists.R;


import java.util.List;

public class AttractionDetails_ImageAdapter extends RecyclerView.Adapter<AttractionDetails_ImageAdapter.ViewHolder> {

    private Context context;
    private List<Object> images;

    public AttractionDetails_ImageAdapter(Context context, List<Object> images) {
        this.context = context;
        this.images = images;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_attraction_details_image_item);
        }

        public ImageView getImageView(){
            return imageView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(context).inflate(R.layout.attraction_details_image_item, parent, false);
        return new ViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull AttractionDetails_ImageAdapter.ViewHolder holder, int position) {

        for(int i=0; i<images.size(); i++){
            holder.imageView.setImageURI((Uri) images.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
