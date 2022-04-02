package com.xw.selector.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.xw.selector.entity.SelectSpec;
import com.xw.selector.pojo.MediaFile;

import java.util.LinkedList;
import java.util.List;

public class MediaPreAdapter extends PagerAdapter {

    private List<MediaFile> mediaFileList;
    private Context mContext;
    private LinkedList<ImageView> viewCache = new LinkedList<>();

    public MediaPreAdapter(List<MediaFile> mediaFileList, Context mContext) {
        this.mediaFileList = mediaFileList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mediaFileList != null ? mediaFileList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView;
        if (viewCache.size() > 0) {
            imageView = viewCache.remove();
        } else {
            imageView = new ImageView(mContext);
        }

        SelectSpec.getInstance().imageEngine
                .loadImage(mContext,imageView,mediaFileList.get(position).path);

        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ImageView imageView = (ImageView) object;
        container.removeView(imageView);
        viewCache.add(imageView);
    }
}
