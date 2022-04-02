package com.xw.selector.task.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.xw.selector.pojo.MediaFile;

/**
 * 视频扫描类
 */
public class VideoScanner extends AbsMediaScanner<MediaFile> {

    public VideoScanner(Context context) {
        super(context);
    }

    @Override
    protected Uri getScanUri() {
        return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected String[] getProjection() {
        return new String[]{
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATE_TAKEN
        };
    }

    @Override
    protected String getSelection() {
        return null;
    }

    @Override
    protected String[] getSelectionArgs() {
        return null;
//        return getSelectionArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
    }

    @Override
    protected String getOrder() {
        return MediaStore.Video.Media.DEFAULT_SORT_ORDER + " desc";
    }

    /**
     * 构建媒体对象
     */
    @Override
    protected MediaFile parse(Cursor cursor) {
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        String mime = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
        Integer folderId = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID));
        String folderName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
        long dateToken = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN));

        MediaFile mediaFile = new MediaFile();
        mediaFile.path = path;
        mediaFile.mimeType = mime;
        mediaFile.folderId = folderId;
        mediaFile.folderName = folderName;
        mediaFile.duration = duration;
        mediaFile.updateTime = dateToken;

        return mediaFile;
    }
}
