package com.xw.selector.pojo;

import java.util.ArrayList;

public class MediaFolder {

    //文件夹id
    public int folderId;
    //文件夹名称
    public String folderName;
    //文件夹下第一个媒体资源的封面图
    public String folderCover;
    //是否被选中
    public boolean isCheck;
    // 文件夹下的媒体资源
    public ArrayList<MediaFile> mediaFiles;
}
