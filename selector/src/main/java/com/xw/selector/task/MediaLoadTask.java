package com.xw.selector.task;

import android.content.Context;

import com.xw.selector.listener.MediaLoadListener;
import com.xw.selector.pojo.MediaFile;
import com.xw.selector.task.loader.ImageScanner;
import com.xw.selector.task.loader.MediaHandler;
import com.xw.selector.task.loader.VideoScanner;

import java.util.ArrayList;

public class MediaLoadTask implements Runnable {

    private Context mContext;
    private ImageScanner imageScanner;
    private VideoScanner videoScanner;
    private MediaLoadListener mediaLoadListener;

    public MediaLoadTask(Context context, MediaLoadListener mediaLoadListener) {
        this.mContext = context;
        this.mediaLoadListener = mediaLoadListener;

        imageScanner = new ImageScanner(mContext);
        videoScanner = new VideoScanner(mContext);
    }

    @Override
    public void run() {
        ArrayList<MediaFile> imageList = new ArrayList<>();
        ArrayList<MediaFile> videoList = new ArrayList<>();

        if (imageScanner != null) {
            imageList = imageScanner.queryMedia();
        }

        if (videoScanner != null) {
            videoList = videoScanner.queryMedia();
        }

        if (mediaLoadListener != null) {
            mediaLoadListener.loadMediaSuccess(MediaHandler.getMediaFolder(imageList,videoList));
        }
    }
}
