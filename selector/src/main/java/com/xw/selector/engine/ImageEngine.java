package com.xw.selector.engine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

public interface ImageEngine {

    void loadThumbnail(Context context, Drawable placeholder, ImageView imageView, String uri);

    void loadGifThumbnail(Context context, Drawable placeholder, ImageView imageView, String uri);

    void loadImage(Context context, ImageView imageView, String uri);

    void loadGifImage(Context context, ImageView imageView, String uri);

    boolean supportAnimatedGif();
}
