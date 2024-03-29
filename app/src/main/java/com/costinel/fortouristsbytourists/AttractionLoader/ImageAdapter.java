package com.costinel.fortouristsbytourists.AttractionLoader;

import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.costinel.fortouristsbytourists.AttractionDetails;
import com.costinel.fortouristsbytourists.Model.Attraction;
import com.costinel.fortouristsbytourists.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

// creating an ImageAdapter class which will play the role of a bridge between the data and the
// recyclerView;
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Attraction> listOfAttractions;
    private List<List<Uri>> listOfImagesUri;

    // creating a method which will save the context and the the list of upload items;
    // context is important in this class because an ImageAdapter doesn't accept Intent which
    // will be used in an onClickListener to send the user from one activity layout to another,
    // by clicking an image from the recyclerView;
    public ImageAdapter(Context context, List<Attraction> listOfAttractions, List<List<Uri>> imageUri) {
        this.mContext = context;
        this.listOfAttractions = listOfAttractions;
        this.listOfImagesUri = imageUri;
    }

    // this method is used to return a viewHolder to the image_item activity layout;
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    // this method will extract the data from the mUploads into the single card items which will
    // have the name and image of the attraction;
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

//        List<Attraction> tempAttraction = new ArrayList<>();
//        List<List<Uri>> tempImages = new ArrayList<>();
//
//        Iterator<Map.Entry<Attraction, List<Uri>>> entries = attraction.entrySet().iterator();
//        while(entries.hasNext()){
//            Map.Entry<Attraction, List<Uri>> entry = entries.next();
//            tempAttraction.add(entry.getKey());
//            tempImages.add(entry.getValue());
//        }

        holder.textViewName.setText(listOfAttractions.get(position).getName());
        Picasso.get()
                .load(listOfImagesUri.get(position).get(0).toString())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);

        // this onClickListener will allow the user to click an image from the recyclerView;
        // to do this an intent will send the relevant data using "i.putExtra" to the
        // attraction details class and send the user to the attraction details activity layout
        // by selecting an image to view all the details related to the selection;
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, AttractionDetails.class);
                i.putExtra("name", listOfAttractions.get(position).getName());
                i.putExtra("location", listOfAttractions.get(position).getLocation());
                i.putExtra("description", listOfAttractions.get(position).getDescription());
                i.putExtra("price", listOfAttractions.get(position).getPrice());
                i.putExtra("imagesUrl", (Serializable) listOfImagesUri.get(position));

//                i.putExtra("attractionKey", attractionKey.get(position));
                mContext.startActivity(i);
            }
        });
    }

    // this method will get the size of the upload list which contains the attractions;
    @Override
    public int getItemCount() {
        return listOfAttractions.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        // creating the impostors for the activity layout which will implement a recyclerView to
        // display the attraction image together with the name of the attraction;
        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            // linking the impostors to their textView and imageView in the image_item activity
            // layout;
            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
        }
    }
}