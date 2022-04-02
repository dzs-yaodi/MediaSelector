package com.xw.selector.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xw.selector.R;
import com.xw.selector.adapter.MediaFoldersAdapter;
import com.xw.selector.pojo.MediaFolder;
import com.xw.selector.utils.CommonUtils;

import java.util.List;

public class FolderPopupWindow extends PopupWindow {

    private static final int DEFAULT_IMAGE_FOLDER_SELECT = 0;//默认选中文件夹

    private Context mContext;
    private List<MediaFolder> mMediaFolderList;
    private MediaFoldersAdapter mediaFoldersAdapter;
    private RecyclerView mRecyclerView;

    public FolderPopupWindow(Context context,List<MediaFolder> mediaFolderList) {
        this.mContext = context;
        this.mMediaFolderList = mediaFolderList;
        initViews();
    }

    private void initViews() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_folders, null);
        mRecyclerView = view.findViewById(R.id.rv_main_imageFolders);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mediaFoldersAdapter = new MediaFoldersAdapter(mContext, mMediaFolderList, DEFAULT_IMAGE_FOLDER_SELECT);
        mRecyclerView.setAdapter(mediaFoldersAdapter);

        initPopupWindow(view);
    }

    /**
     * 初始化PopupWindow的一些属性
     */
    private void initPopupWindow(View view) {
        setContentView(view);
        int[] screenSize = CommonUtils.getScreenSize(mContext);
        setWidth(screenSize[0]);
        setHeight((int) (screenSize[1] * 0.6));
        setBackgroundDrawable(new ColorDrawable());
        setOutsideTouchable(true);
        setFocusable(true);
        setTouchInterceptor((view1, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                dismiss();
            }
            return false;
        });
    }

    public MediaFoldersAdapter getAdapter() {
        return mediaFoldersAdapter;
    }
}
