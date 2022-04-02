package com.xw.selector;

public class  MimeType {

    private static final int MEDIA_MIME_TYPE_ALL = 1;
    private static final int MEDIA_MIME_TYPE_VIDEO = 2;
    private static final int MEDIA_MIME_TYPE_IMAGE = 3;

    public static int ofAll() {
        return MEDIA_MIME_TYPE_ALL;
    }

    public static int ofImage() {
        return MEDIA_MIME_TYPE_IMAGE;
    }

    public static int ofVideo() {
        return MEDIA_MIME_TYPE_VIDEO;
    }

}
