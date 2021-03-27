package com.costinel.fortouristsbytourists.SelectedImagesLoader;


import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.costinel.fortouristsbytourists.R;


import java.util.List;

public class SelectedImageAdapter extends RecyclerView.Adapter<SelectedImageAdapter.ViewHolder> {

    private List<Uri> localDataSet;
    private Context mContext;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView2;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            imageView2 = view.findViewById(R.id.selected_image_view);
        }

        public ImageView getImageView() {
            return imageView2;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet List<String> dataSet containing the data to populate views to be used
     * by RecyclerView.
     */
    public SelectedImageAdapter(Context context, List<Uri> dataSet) {
        mContext = context;
        localDataSet = dataSet;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(mContext).inflate(R.layout.upload_image_item, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        for(int i=0; i<localDataSet.size(); i++){
            viewHolder.imageView2.setImageURI(localDataSet.get(position));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
