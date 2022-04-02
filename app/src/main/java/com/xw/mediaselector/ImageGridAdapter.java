package com.xw.mediaselector;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.GridViewHolder>{

    private List<String> stringList = new ArrayList<>();

    public ImageGridAdapter(List<String> stringList) {
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GridViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_grid_layout,parent,false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(stringList.get(position))
                .into(holder.imageThumb);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    class GridViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageThumb;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);

            imageThumb = itemView.findViewById(R.id.image_thumb);
        }
    }
}
