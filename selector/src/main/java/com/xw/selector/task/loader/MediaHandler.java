package com.xw.selector.task.loader;

import android.content.Context;

import com.xw.selector.pojo.MediaFile;
import com.xw.selector.pojo.MediaFolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaHandler {

    public static final int ALL_MEDIA_FOLDER = -1;//全部媒体
    public static final int ALL_VIDEO_FOLDER = -2;//全部视频

    /**
     * 对搜索到的图片进行分类
     */
    public static List<MediaFolder> getImageFolder( ArrayList<MediaFile> imageFileList) {
        return getMediaFolder(imageFileList,null);
    }

    /**
     * 对搜索的视频进度分类
     */
    public static List<MediaFolder> getVideoFolder(ArrayList<MediaFile> videoFileList) {
        return getMediaFolder(null,videoFileList);
    }

    /**
     * 对图片/视频进行分类
     */
    public static List<MediaFolder> getMediaFolder(ArrayList<MediaFile> imageFileList,ArrayList<MediaFile> videoFileList) {

        //根据媒体所在的文件夹id 进行分类
        Map<Integer,MediaFolder> mediaFolderMap = new HashMap<>();

        //全部图片和视频
        ArrayList<MediaFile> mediaFiles = new ArrayList<>();
        if (imageFileList != null) {
            mediaFiles.addAll(imageFileList);
        }

        if (videoFileList != null) {
            mediaFiles.addAll(videoFileList);
        }

        //根据媒体资源更新事件排序，越近期的越考前
        Collections.sort(mediaFiles, (o1, o2) -> {
            if (o1.updateTime > o2.updateTime) {
                return  -1;
            } else if (o1.updateTime < o2.updateTime) {
                return 1;
            } else {
                return 0;
            }
        });

        //所有资源
        if (!mediaFiles.isEmpty()) {
            MediaFolder mediaFolder = new MediaFolder();
            mediaFolder.folderId = ALL_MEDIA_FOLDER;
            mediaFolder.folderName = "全部";
            mediaFolder.folderCover = mediaFiles.get(0).path;
            mediaFolder.mediaFiles = mediaFiles;

            mediaFolderMap.put(ALL_MEDIA_FOLDER,mediaFolder);
        }

        //视频
        if (videoFileList != null && !videoFileList.isEmpty()) {
            MediaFolder videoFoler = new MediaFolder();
            videoFoler.folderId = ALL_VIDEO_FOLDER;
            videoFoler.folderName = "所有视频";
            videoFoler.folderCover = videoFileList.get(0).path;
            videoFoler.mediaFiles = videoFileList;

            mediaFolderMap.put(ALL_VIDEO_FOLDER,videoFoler);
        }

        //按文件夹对图片进行分类
        if (imageFileList != null && !imageFileList.isEmpty()) {
            int size = imageFileList.size();
            for (int i = 0; i < size; i++) {
                MediaFile mediaFile = imageFileList.get(i);
                int folderId = mediaFile.folderId;

                //先查看map 中是否存在当前的文件夹 ，不存在则创建
                MediaFolder imageFolder = mediaFolderMap.get(folderId);
                if (imageFolder == null) {
                    imageFolder = new MediaFolder();
                    imageFolder.folderId = folderId;
                    imageFolder.folderName = mediaFile.folderName;
                    imageFolder.folderCover = mediaFile.path;
                    imageFolder.mediaFiles = new ArrayList<>();
                }
                //取出MediaFolder的图片列表，把当前的图片资源添加进去
                ArrayList<MediaFile> imageList = imageFolder.mediaFiles;
                imageList.add(mediaFile);

                //重新再给图片资源列表复制
                imageFolder.mediaFiles = imageList;
                mediaFolderMap.put(folderId,imageFolder);
            }
        }

        //重新整理数据
        List<MediaFolder> mediaFolderList = new ArrayList<>();
        for (Integer folderId : mediaFolderMap.keySet()) {
            mediaFolderList.add(mediaFolderMap.get(folderId));
        }

        //按文件夹中的图片数据排序
        Collections.sort(mediaFolderList, (o1, o2) -> {
            if (o1.mediaFiles.size() > o2.mediaFiles.size()) {
                return -1;
            } else if (o1.mediaFiles.size() < o2.mediaFiles.size()){
                return 1;
            } else {
                return 0;
            }
        });

        return mediaFolderList;
    }
}
