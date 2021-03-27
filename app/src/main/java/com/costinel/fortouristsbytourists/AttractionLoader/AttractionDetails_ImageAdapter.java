package com.costinel.fortouristsbytourists.AttractionLoader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class AttractionDetails_ImageAdapter extends RecyclerView.Adapter<AttractionDetails_ImageAdapter.ViewHolder> {

    private final Context context;
    private final List<Uri> images;

    public AttractionDetails_ImageAdapter(Context context, List<Uri> images) {
        this.context = context;
        this.images = images;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_attraction_details_image_item);
        }
        public ImageView getImageView(){
            return imageView;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(context).inflate(R.layout.attraction_details_image_item, parent, false);
        return new ViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.get()
                .load(images.get(position))
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
