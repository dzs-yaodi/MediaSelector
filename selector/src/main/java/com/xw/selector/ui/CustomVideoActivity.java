package com.xw.selector.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.xw.selector.R;
import com.xw.selector.adapter.CustomAlbumAdapter;
import com.xw.selector.entity.SelectSpec;
import com.xw.selector.listener.MediaLoadListener;
import com.xw.selector.pojo.MediaFile;
import com.xw.selector.pojo.MediaFolder;
import com.xw.selector.task.CommonExcutor;
import com.xw.selector.task.VideoLoadTask;
import com.xw.selector.utils.CommonUtils;
import com.xw.selector.view.MediaGridItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.drakeet.multitype.Items;

public class CustomVideoActivity extends AppCompatActivity {

    public static final String EXTRA_RESULT_VIDEO_URI = "extra_result_video_uri";
    public static final String EXTRA_TURN_CLASS_NAME = "extra_turn_to_class";
    public static final String EXTRA_KEY_VALUE = "extra_key_value";

    private TextView tvNext;
    private StandardGSYVideoPlayer videoPlayer;
    private RecyclerView recyclerVideo;
    private ImageView mImageCover;
    private List<MediaFile> mediaFileList = new ArrayList<>();
    private CustomAlbumAdapter albumAdapter;
    private int selectIndex = 0;
    private String clasName;
    private Map<String, String> mParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setTitleBar(getWindow(),"#303030");
        setContentView(R.layout.activity_custom_video);

        //EXOPlayer内核
        PlayerFactory.setPlayManager(SystemPlayerManager.class);

        tvNext = findViewById(R.id.tv_next);
        videoPlayer = findViewById(R.id.video_player);
        recyclerVideo = findViewById(R.id.recycler_video);

        initPlayer();
        initRecycler();
        startVideoSCanner();
        setListener();
    }

    private void setListener() {
        tvNext.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_RESULT_VIDEO_URI,mediaFileList.get(selectIndex).path);
            if (!TextUtils.isEmpty(clasName)) {
                ComponentName comp = new ComponentName(this,clasName);
                intent.setComponent(comp);
                intent.setAction("android.intent.action.VIEW");
                if (mParams != null) {
                    for (Map.Entry<String, String> entry : mParams.entrySet()) {
                        intent.putExtra(entry.getKey(), entry.getValue());
                    }
                }
                startActivity(intent);
            } else {
                setResult(RESULT_OK,intent);
            }
            finish();
        });

        albumAdapter.setItemClickListener(position -> {
            for (int i = 0; i < mediaFileList.size(); i++) {
                if (i == position) {
                    mediaFileList.get(i).checked = true;
                } else {
                    mediaFileList.get(i).checked = false;
                }
            }

            selectIndex = position;
            albumAdapter.notifyDataSetChanged();

            String path = mediaFileList.get(selectIndex).path;
            videoPlayer.setUp(path,false,"");
            videoPlayer.startPlayLogic();
        });
    }

    private void startVideoSCanner() {
        Runnable mediaLoadTask = new VideoLoadTask(this, new VideoLoader());
        CommonExcutor.getInstance().excute(mediaLoadTask);
    }

    private void initRecycler() {
        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        recyclerVideo.setLayoutManager(layoutManager);
        albumAdapter = new CustomAlbumAdapter(mediaFileList);
        recyclerVideo.setAdapter(albumAdapter);

        int space = getResources().getDimensionPixelSize(R.dimen.custom_video_recycler_indecor);
        recyclerVideo.addItemDecoration(new MediaGridItemDecoration(4,space,false));

        if (getIntent().hasExtra(EXTRA_TURN_CLASS_NAME)) {
            clasName = getIntent().getStringExtra(EXTRA_TURN_CLASS_NAME);
        }

        if (getIntent().hasExtra(EXTRA_KEY_VALUE)) {
            mParams = (Map<String, String>) getIntent().getSerializableExtra(EXTRA_KEY_VALUE);
        }
    }

    private void initPlayer() {
        mImageCover = new ImageView(this);
        mImageCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
        videoPlayer.getFullscreenButton().setVisibility(View.GONE);
        videoPlayer.getBackButton().setImageResource(R.mipmap.select_image_back);
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
//        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_4_3);
//
//        int width = CommonUtils.getScreenWidth(this);
//        ViewGroup.LayoutParams layoutParams = videoPlayer.getLayoutParams();
//        layoutParams.width = width;
//        layoutParams.height = (int) (width / 1.33);
//        videoPlayer.setLayoutParams(layoutParams);

        videoPlayer.getBackButton().setOnClickListener(v -> {
            onBackPressed();
        });
    }

    class VideoLoader implements MediaLoadListener {

        @Override
        public void loadMediaSuccess(List<MediaFolder> mediaFolderList) {
            runOnUiThread(()->{
                if (!mediaFolderList.isEmpty()) {
                    mediaFileList.clear();
                    mediaFileList.addAll( mediaFolderList.get(0).mediaFiles);

                    albumAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        videoPlayer.onVideoPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        GSYVideoManager.releaseAllVideos();
        super.onDestroy();
    }
}