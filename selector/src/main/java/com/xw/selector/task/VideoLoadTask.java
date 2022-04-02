package com.xw.selector.task;

import android.content.Context;

import com.xw.selector.listener.MediaLoadListener;
import com.xw.selector.pojo.MediaFile;
import com.xw.selector.task.loader.MediaHandler;
import com.xw.selector.task.loader.VideoScanner;

import java.util.ArrayList;

public class VideoLoadTask implements Runnable{

    private Context mContext;
    private VideoScanner mVideoScanner;
    private MediaLoadListener mediaLoadListener;


    public VideoLoadTask(Context mContext, MediaLoadListener mediaLoadListener) {
        this.mContext = mContext;
        this.mediaLoadListener = mediaLoadListener;
        mVideoScanner = new VideoScanner(mContext);
    }

    @Override
    public void run() {

        ArrayList<MediaFile> mediaFiles = new ArrayList<>();

        if (mVideoScanner != null) {
            mediaFiles = mVideoScanner.queryMedia();
        }

        if (mediaLoadListener != null) {
            mediaLoadListener.loadMediaSuccess(MediaHandler.getVideoFolder(mediaFiles));
        }
    }
}
