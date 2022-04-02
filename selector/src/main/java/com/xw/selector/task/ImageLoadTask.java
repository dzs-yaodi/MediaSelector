package com.xw.selector.task;

import android.content.Context;

import com.xw.selector.listener.MediaLoadListener;
import com.xw.selector.pojo.MediaFile;
import com.xw.selector.task.loader.ImageScanner;
import com.xw.selector.task.loader.MediaHandler;

import java.util.ArrayList;

public class ImageLoadTask implements Runnable {

    private Context mContext;
    private MediaLoadListener mMediaLoadListener;
    private ImageScanner mImageScanner;

    public ImageLoadTask(Context context, MediaLoadListener mediaLoadListener) {
        this.mContext = context;
        this.mMediaLoadListener = mediaLoadListener;
        mImageScanner = new ImageScanner(mContext);
    }

    @Override
    public void run() {

        ArrayList<MediaFile> fileArrayList = new ArrayList<>();

        if (mImageScanner != null) {
            fileArrayList = mImageScanner.queryMedia();
        }

        if (mMediaLoadListener != null) {
            mMediaLoadListener.loadMediaSuccess(MediaHandler.getImageFolder(fileArrayList));
        }
    }
}
