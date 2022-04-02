package com.xw.selector.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xw.selector.R;
import com.xw.selector.entity.SelectSpec;
import com.xw.selector.listener.OnItemClickListener;
import com.xw.selector.pojo.MediaFile;
import com.xw.selector.utils.CommonUtils;
import com.xw.selector.view.SquareImageView;

import me.drakeet.multitype.ItemViewBinder;

public class VideoBinder extends ItemViewBinder<MediaFile, VideoBinder.VideoViewHolder> {

    private OnItemClickListener onItemClickListener;

    public VideoBinder(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    protected VideoViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new VideoViewHolder(inflater.inflate(R.layout.item_media_video_layout,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull VideoViewHolder holder, @NonNull MediaFile mediaFile) {

        SelectSpec.getInstance()
                .imageEngine
                .loadThumbnail(holder.itemView.getContext(),
                        holder.itemView.getContext().getResources().getDrawable(R.mipmap.select_image_default),
                        holder.imageCover, mediaFile.path);

        holder.tvDuration.setText(CommonUtils.getVideoDuration(mediaFile.duration));
        if (mediaFile.checked) {
            holder.image_check.setImageResource(R.mipmap.select_image_checked);
        } else {
            holder.image_check.setImageResource(R.mipmap.select_image_check);
        }
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        private SquareImageView imageCover;
        private ImageView image_check;
        private TextView tvDuration;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            imageCover = itemView.findViewById(R.id.image_cover);
            image_check = itemView.findViewById(R.id.image_check);
            tvDuration = itemView.findViewById(R.id.tv_duration);

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
