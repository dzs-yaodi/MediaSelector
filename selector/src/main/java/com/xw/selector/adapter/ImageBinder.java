package com.xw.selector.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xw.selector.R;
import com.xw.selector.entity.SelectSpec;
import com.xw.selector.listener.OnItemClickListener;
import com.xw.selector.pojo.MediaFile;
import com.xw.selector.view.SquareImageView;

import me.drakeet.multitype.ItemViewBinder;

public class ImageBinder extends ItemViewBinder<MediaFile, ImageBinder.ImageViewHolder> {

    private OnItemClickListener onItemClickListener;

    public ImageBinder(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    protected ImageViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ImageViewHolder(inflater.inflate(R.layout.item_media_image_layout,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ImageViewHolder holder, @NonNull MediaFile mediaFile) {

        String imagePath = mediaFile.path;
        String suffix = imagePath.substring(imagePath.lastIndexOf(".") + 1);
        if (suffix.toUpperCase().equals("GIF")) {
            holder.ivItemGif.setVisibility(View.VISIBLE);
            SelectSpec.getInstance().imageEngine
                    .loadGifThumbnail(holder.itemView.getContext(),
                            holder.itemView.getContext().getResources().getDrawable(R.mipmap.select_image_default),
                            holder.imageCover, imagePath);
        } else {
            holder.ivItemGif.setVisibility(View.GONE);
            SelectSpec.getInstance().imageEngine
                    .loadThumbnail(holder.itemView.getContext(),
                            holder.itemView.getContext().getResources().getDrawable(R.mipmap.select_image_default),
                            holder.imageCover, imagePath);
        }

        if (mediaFile.checked) {
            holder.image_check.setImageResource(R.mipmap.select_image_checked);
        } else {
            holder.image_check.setImageResource(R.mipmap.select_image_check);
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        private SquareImageView imageCover;
        private ImageView ivItemGif;
        private ImageView image_check;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageCover = itemView.findViewById(R.id.image_cover);
            ivItemGif = itemView.findViewById(R.id.iv_item_gif);
            image_check = itemView.findViewById(R.id.image_check);

            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });

            image_check.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onChecked(getAdapterPosition());
                }
            });
        }
    }
}
