package com.xw.selector.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xw.selector.R;
import com.xw.selector.entity.SelectSpec;
import com.xw.selector.listener.ItemClickListener;
import com.xw.selector.pojo.MediaFile;
import com.xw.selector.utils.CommonUtils;

import java.util.List;

public class CustomAlbumAdapter extends RecyclerView.Adapter<CustomAlbumAdapter.CustomAlbumViewHolder>{

    private List<MediaFile> mediaFileList;
    private ItemClickListener itemClickListener;

    public CustomAlbumAdapter(List<MediaFile> mediaFileList) {
        this.mediaFileList = mediaFileList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CustomAlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomAlbumViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_custom_media_select,parent,false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAlbumViewHolder holder, int position) {

        MediaFile mediaFile = mediaFileList.get(position);
        if (mediaFile != null) {
            SelectSpec.getInstance().imageEngine
                    .loadThumbnail(holder.itemView.getContext(),
                            holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_media_video_slect_bg),
                            holder.media_thumbnail, mediaFile.path);

            holder.video_duration.setText(CommonUtils.getVideoDuration(mediaFile.duration));

            if (mediaFile.checked) {
                holder.viewCover.setVisibility(View.VISIBLE);
            } else {
                holder.viewCover.setVisibility(View.GONE);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaFileList.size();
    }

    class CustomAlbumViewHolder extends RecyclerView.ViewHolder {

        private ImageView media_thumbnail;
        private TextView video_duration;
        private View viewCover;

        public CustomAlbumViewHolder(@NonNull View itemView) {
            super(itemView);

            media_thumbnail = itemView.findViewById(R.id.media_thumbnail);
            video_duration = itemView.findViewById(R.id.video_duration);
            viewCover = itemView.findViewById(R.id.view_cover);

            int width = CommonUtils.getScreenWidth(itemView.getContext());
            int imgWidth = width - (itemView.getContext().getResources().getDimensionPixelSize(
                    R.dimen.custom_video_recycler_indecor) * 3);

            ViewGroup.LayoutParams layoutParams = media_thumbnail.getLayoutParams();
            layoutParams.width = imgWidth / 4;
            layoutParams.height = imgWidth / 4;

            media_thumbnail.setLayoutParams(layoutParams);
        }
    }
}
