package com.xw.selector.entity;

import com.xw.selector.engine.ImageEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectSpec {

    /**
     * 选择资源文件类型
     */
    public int mimeType;
    /**
     * 最大选择数量
     */
    public int maxSelectable;

    /**
     *  自定义 权限需要的FileProvider
     */
    public CaptureStrategy captureStrategy;
    /**
     * 图片加载引擎
     */
    public ImageEngine imageEngine;
    /**
     * 需要跳转到的路径
     */
    public String className;
    /**
     * 跳转页面需要传递的参数
     */
    public Map<String,String> map;
    /**
     * 是否显示预览图
     */
    public boolean showPreview;
    /**
     * 是否只显示gif
     */
    public boolean isOnlyGif;
    /**
     * 是否显示相机
     */
    public boolean isShowCamera;
    /**
     * 已经选中数据集合
     */
    private ArrayList<String> mediaPathList = new ArrayList<>();

    /**
     * 最大录制视频的时间限制
     */
    public int videoDuraionLimit = 30;

    /**
     * 是否使用自定义视频播放页面
     */
    public boolean toCustomVideo = false;

    public static SelectSpec getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static SelectSpec getClearInstance() {
        SelectSpec selectSpec = getInstance();
        selectSpec.reset();
        return selectSpec;
    }

    private void reset() {
        mimeType = 0;
        maxSelectable = 1;
        captureStrategy = null;
        imageEngine = null;
        className = null;
        map = null;
        showPreview = false;
        isOnlyGif = false;
        isShowCamera = false;
        mediaPathList = new ArrayList<>();
        videoDuraionLimit = 30;
        toCustomVideo = false;
    }

    private static final class InstanceHolder {
        private static final SelectSpec INSTANCE = new SelectSpec();
    }

    /**
     * 获取当前已经选中的媒体资源的个数
     */
    public int getSelectLenght() {
        return mediaPathList.size();
    }

    public ArrayList<String> getMediaPathList() {
        return mediaPathList;
    }

    /**
     * 添加/移除 图片，视频
     */
    public int addStringToSelectPaths(String imagePath) {
        if (mediaPathList.contains(imagePath)) {
            mediaPathList.remove(imagePath);
            return 2;
        } else {
            if (getSelectLenght() < maxSelectable) {
                mediaPathList.add(imagePath);
                return 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * 添加选中集合
     */
    public void addListToSelectPaths(List<String> imagePaths) {
        if (imagePaths != null) {
            for (int i = 0; i < imagePaths.size(); i++) {
                String path = imagePaths.get(i);
                if (!mediaPathList.contains(path) && getSelectLenght() < maxSelectable) {
                    mediaPathList.add(path);
                }
            }
        }
    }
}
