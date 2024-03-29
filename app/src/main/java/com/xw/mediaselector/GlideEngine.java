package com.xw.mediaselector;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.xw.selector.engine.ImageEngine;

public class GlideEngine implements ImageEngine {


    @Override
    public void loadThumbnail(Context context, Drawable placeholder, ImageView imageView, String uri) {
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(new RequestOptions()
                .placeholder(placeholder))
                .into(imageView);
    }

    @Override
    public void loadGifThumbnail(Context context, Drawable placeholder, ImageView imageView, String uri) {
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(new RequestOptions()
                        .placeholder(placeholder)
                        .centerCrop())
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, ImageView imageView, String uri) {
        Glide.with(context)
                .load(uri)
                .apply(new RequestOptions()
                        .priority(Priority.HIGH)
                        .fitCenter())
                .into(imageView);
    }

    @Override
    public void loadGifImage(Context context, ImageView imageView, String uri) {
        Glide.with(context)
                .asGif()
                .load(uri)
                .apply(new RequestOptions()
                        .priority(Priority.HIGH)
                        .fitCenter())
                .into(imageView);
    }

    @Override
    public boolean supportAnimatedGif() {
        return true;
    }
}
