package com.xw.selector;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xw.selector.engine.ImageEngine;
import com.xw.selector.entity.CaptureStrategy;
import com.xw.selector.entity.SelectSpec;
import com.xw.selector.listener.OnActivityResult;
import com.xw.selector.ui.CustomVideoActivity;
import com.xw.selector.ui.SelectActivity;

import java.util.List;
import java.util.Map;

public class SelectCreator {

    private MediaSelector mediaSelector;
    private SelectSpec mSelectSpec;
    private Router router;

    public SelectCreator(MediaSelector mediaSelector, int mediaType) {
        this.mediaSelector = mediaSelector;
        mSelectSpec = SelectSpec.getClearInstance();
        mSelectSpec.mimeType = mediaType;
    }

    public SelectCreator(Router router, int mediaType) {
        this.router = router;
        mSelectSpec = SelectSpec.getClearInstance();
        mSelectSpec.mimeType = mediaType;
    }


    public SelectCreator maxSelectable(int maxSelectable) {
        if (maxSelectable < 1) {
            throw new IllegalArgumentException("maxSelectable must be greater than or equal to one");
        }

        mSelectSpec.maxSelectable = maxSelectable;
        return this;
    }

    // 自定义 10.0 权限需要的FileProvider
    public SelectCreator captureStrategy(CaptureStrategy captureStrategy) {
        mSelectSpec.captureStrategy = captureStrategy;
        return this;
    }

    //图片加载引擎
    public SelectCreator imageEngine(ImageEngine imageEngine) {
        mSelectSpec.imageEngine = imageEngine;
        return this;
    }

    //需要跳转的activity路径
    public SelectCreator setTurnToCls(@Nullable String clsName) {
        mSelectSpec.className = clsName;
        return this;
    }

    //传递给activity的参数
    public SelectCreator setTurnParam(Map<String,String> params) {
        mSelectSpec.map = params;
        return this;
    }

    //是否显示预览图
    public SelectCreator showPreview(boolean showPreview) {
        mSelectSpec.showPreview = showPreview;
        return this;
    }

    /**
     * 是否只显示gif图片
     */
    public SelectCreator isOnlyGif(boolean show) {
        mSelectSpec.isOnlyGif = show;
        return this;
    }

    /**
     * 是否显示照相机
     */
    public SelectCreator showCamera(boolean isShow) {
        mSelectSpec.isShowCamera = isShow;
        return this;
    }

    /**
     * 使用自定义视频播放的页面
     */
    public SelectCreator toCustomVideo(boolean isShow) {
        mSelectSpec.toCustomVideo = isShow;
        return this;
    }

    /**
     * 添加/移除 图片，视频
     */
    public SelectCreator addStringToSelectPaths(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            mSelectSpec.addStringToSelectPaths(imagePath);
        }
        return this;
    }

    /**
     * 添加选中集合
     */
    public SelectCreator addListToSelectPaths(List<String> imagePaths) {
        if (imagePaths != null) {
            mSelectSpec.addListToSelectPaths(imagePaths);
        }

        return this;
    }

    /**
     * 设置录制视频的时间限制
     */
    public SelectCreator setVideoLimit(int limit) {
        if (limit > 0) {
            mSelectSpec.videoDuraionLimit = limit;
        }
        return this;
    }

    public void start(int requestCode) {
        Activity activity = mediaSelector.getActivity();
        if (activity == null) {
            return;
        }

        Intent intent = new Intent();

        if (SelectSpec.getInstance().mimeType == MimeType.ofVideo() &&
            SelectSpec.getInstance().toCustomVideo && SelectSpec.getInstance().maxSelectable == 1) {
            intent.setClass(activity, CustomVideoActivity.class);
        } else {
            intent.setClass(activity, SelectActivity.class);
        }
        Fragment fragment = mediaSelector.getFragment();
        if (fragment != null) {
            fragment.startActivityForResult(intent,requestCode);
        } else {
            activity.startActivityForResult(intent,requestCode);
        }
    }

    public void startLauncher(OnActivityResult activityResult) {

        router.setActivityResult(activityResult);
        Activity activity = router.getActivity();
        Intent intent = new Intent();

        if (SelectSpec.getInstance().mimeType == MimeType.ofVideo() &&
                SelectSpec.getInstance().toCustomVideo && SelectSpec.getInstance().maxSelectable == 1) {
            intent.setClass(activity, CustomVideoActivity.class);
        } else {
            intent.setClass(activity, SelectActivity.class);
        }

        router.getActivityResult().launch(intent);
    }
}
