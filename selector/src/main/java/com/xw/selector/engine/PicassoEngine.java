package com.xw.selector.engine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoEngine implements ImageEngine {

    @Override
    public void loadThumbnail(Context context, Drawable placeholder, ImageView imageView, String uri) {
        Picasso.get().load(uri).placeholder(placeholder)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void loadGifThumbnail(Context context, Drawable placeholder, ImageView imageView,
                                 String uri) {
        loadThumbnail(context, placeholder, imageView, uri);
    }

    @Override
    public void loadImage(Context context, ImageView imageView, String uri) {
        Picasso.get().load(uri).priority(Picasso.Priority.HIGH)
                .centerInside().into(imageView);
    }

    @Override
    public void loadGifImage(Context context, ImageView imageView, String uri) {
        loadImage(context, imageView, uri);
    }

    @Override
    public boolean supportAnimatedGif() {
        return false;
    }
}
