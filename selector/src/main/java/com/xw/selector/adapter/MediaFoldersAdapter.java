package com.xw.selector.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xw.selector.R;
import com.xw.selector.entity.SelectSpec;
import com.xw.selector.pojo.MediaFolder;

import java.util.List;

public class MediaFoldersAdapter extends RecyclerView.Adapter<MediaFoldersAdapter.ViewHolder> {

    private Context mContext;
    private List<MediaFolder> mMediaFolderList;
    private int mCurrentImageFolderIndex;


    public MediaFoldersAdapter(Context context, List<MediaFolder> mediaFolderList, int position) {
        this.mContext = context;
        this.mMediaFolderList = mediaFolderList;
        this.mCurrentImageFolderIndex = position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_folder_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final MediaFolder mediaFolder = mMediaFolderList.get(position);
        String folderCover = mediaFolder.folderCover;
        String folderName = mediaFolder.folderName;
        int imageSize = mediaFolder.mediaFiles.size();

        if (!TextUtils.isEmpty(folderName)) {
            holder.mFolderName.setText(folderName);
        }

        holder.mImageSize.setText(String.format(mContext.getString(R.string.image_num), imageSize));

        if (mCurrentImageFolderIndex == position) {
            holder.mImageFolderCheck.setVisibility(View.VISIBLE);
        } else {
            holder.mImageFolderCheck.setVisibility(View.GONE);
        }
        //加载图片
        try {
            SelectSpec.getInstance()
                    .imageEngine
                    .loadThumbnail(holder.itemView.getContext(),
                            holder.itemView.getContext().getResources().getDrawable(R.mipmap.select_image_default),
                            holder.mImageCover,folderCover);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mImageFolderChangeListener != null) {
            holder.itemView.setOnClickListener(view -> {
                mCurrentImageFolderIndex = position;
                notifyDataSetChanged();
                mImageFolderChangeListener.onImageFolderChange(view, position);
            });
        }

    }

    @Override
    public int getItemCount() {
        return mMediaFolderList == null ? 0 : mMediaFolderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageCover;
        private TextView mFolderName;
        private TextView mImageSize;
        private ImageView mImageFolderCheck;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageCover = itemView.findViewById(R.id.iv_item_imageCover);
            mFolderName = itemView.findViewById(R.id.tv_item_folderName);
            mImageSize = itemView.findViewById(R.id.tv_item_imageSize);
            mImageFolderCheck = itemView.findViewById(R.id.iv_item_check);
        }
    }

    /**
     * 接口回调，Item点击事件
     */
    private OnImageFolderChangeListener mImageFolderChangeListener;

    public void setOnImageFolderChangeListener(OnImageFolderChangeListener onItemClickListener) {
        this.mImageFolderChangeListener = onItemClickListener;
    }

    public interface OnImageFolderChangeListener {
        void onImageFolderChange(View view, int position);
    }
}
