package com.example.gemyni.gallery;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.CustomRecyclerViewHolder> {
    private Context context;
    private List<Image> images;
    private LayoutInflater layoutInflater;

    public CustomRecyclerViewAdapter(Context context, List<com.example.gemyni.gallery.Image> imageList) {
        this.context = context;
        this.images = imageList;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    public class CustomRecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public CustomRecyclerViewHolder(@NonNull final View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageview);
            textView = itemView.findViewById(R.id.textview);
        }
    }

    @NonNull
    @Override
    public CustomRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tmp = this.layoutInflater.inflate(R.layout.recycler_view_item, parent, false);
        return new CustomRecyclerViewHolder(tmp);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecyclerViewHolder holder, int position) {
        Image tmp = images.get(position);
        holder.textView.setText(tmp.getTitle());
        holder.imageView.setImageURI(tmp.getUri());
    }


    @Override
    public int getItemCount() {
        return images.size();
    }
}
