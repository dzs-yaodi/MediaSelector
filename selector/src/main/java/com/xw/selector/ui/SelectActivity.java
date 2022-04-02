package com.xw.selector.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xw.selector.MimeType;
import com.xw.selector.R;
import com.xw.selector.adapter.CameraBinder;
import com.xw.selector.adapter.ImageBinder;
import com.xw.selector.adapter.VideoBinder;
import com.xw.selector.entity.SelectSpec;
import com.xw.selector.listener.MediaLoadListener;
import com.xw.selector.listener.OnCameraListener;
import com.xw.selector.listener.OnItemClickListener;
import com.xw.selector.pojo.MediaFile;
import com.xw.selector.pojo.MediaFolder;
import com.xw.selector.task.CommonExcutor;
import com.xw.selector.task.ImageLoadTask;
import com.xw.selector.task.MediaLoadTask;
import com.xw.selector.task.VideoLoadTask;
import com.xw.selector.utils.CommonUtils;
import com.xw.selector.utils.DataUtil;
import com.xw.selector.view.CameraItemSelectDialog;
import com.xw.selector.view.FolderPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class SelectActivity extends AppCompatActivity implements OnCameraListener, OnItemClickListener {

    /**
     * 拍照相关
     */
    private String mFilePath;
    private static final int REQUEST_CODE_IMAGE = 0x02;//拍照标识
    /**
     * 录视频
     */
    private String mVideoFilePath;
    private static final int REQUEST_CODE_VIDEO = 0x03;//录视频标识

    private static final int PICTURE_PREVIEW_CODE =  0x04;//预览图片标识
    /**
     * 返回选择媒体资源
     */
    public static final String REQUEST_SELECT_MEDIAS = "select_medias";

    //表示屏幕亮暗
    private static final int LIGHT_OFF = 0;
    private static final int LIGHT_ON = 1;

    private TextView tvBackTitle;
    private TextView tvCommit;
    private RelativeLayout rlBottom;
    private TextView tvImageFolders;
    private RecyclerView mRecycler;
    private GridLayoutManager gridLayoutManager;
    private ProgressDialog mProgressDialog;

    private MultiTypeAdapter multiTypeAdapter;
    //图片数据源
    private FolderPopupWindow mFolderPopupWindow;
    private ImageView imageCrop;
    private List<MediaFile> mediaFileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setTitleBar(getWindow(),"#303030");
        setContentView(R.layout.activity_select);

        if (getActionBar() != null) {
            getActionBar().hide();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initView();
        startScannerTask();
    }

    private void initView() {

        mProgressDialog = ProgressDialog.show(this, null, "正在扫描媒体资源文件");

        tvBackTitle = findViewById(R.id.tv_back_title);
        tvCommit = findViewById(R.id.tv_commit);
        rlBottom = findViewById(R.id.rl_bottom);
        tvImageFolders = findViewById(R.id.tv_imageFolders);
        mRecycler = findViewById(R.id.recycler_media);
        imageCrop = findViewById(R.id.image_crop);

        gridLayoutManager = new GridLayoutManager(this, 4);
        mRecycler.setLayoutManager(gridLayoutManager);
        mRecycler.setHasFixedSize(true);
        mRecycler.setItemViewCacheSize(60);

        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(MediaFile.class)
                .to(
                        new VideoBinder(this),
                        new ImageBinder(this),
                        new CameraBinder(this)
                ).withLinker((position, mediaFile) -> {
            if (mediaFile.mimeType.equals("camera")) {
                return 2;
            } else if (mediaFile.mimeType.contains("image")) {
                return 1;
            }
            return 0;
        });

        Items items = new Items();
        multiTypeAdapter.setItems(items);
        mRecycler.setAdapter(multiTypeAdapter);

        setListener();
    }

    private void setListener() {

        //返回键
        tvBackTitle.setOnClickListener(v -> finish());
        //完成
        tvCommit.setOnClickListener(v -> {
            BackOrNext();
        });

        tvImageFolders.setOnClickListener(v -> {
            if (mFolderPopupWindow != null) {
                setLightMode(LIGHT_OFF);
                mFolderPopupWindow.showAsDropDown(rlBottom, 0, 0);
            }
        });
    }

    /***********************************************/
    /***
     * 查询成功后 渲染页面
     */
    class MediaLoader implements MediaLoadListener {

        @Override
        public void loadMediaSuccess(List<MediaFolder> mediaFolderList) {
            runOnUiThread(() -> {
                if (!mediaFolderList.isEmpty()) {

                    mediaFileList = mediaFolderList.get(0).mediaFiles;
                    Items items = (Items) multiTypeAdapter.getItems();
                    items.clear();
                    if (SelectSpec.getInstance().isShowCamera) {
                        MediaFile mediaFile = new MediaFile();
                        mediaFile.mimeType = "camera";

                        items.add(mediaFile);
                    }

                    ArrayList<String> selectList = SelectSpec.getInstance().getMediaPathList();
                    if (selectList != null && !selectList.isEmpty()) {
                        for (int i = 0; i < selectList.size(); i++) {
                            for (MediaFile file : mediaFileList) {
                                if (selectList.get(i).equals(file.path)) {
                                    file.checked = true;
                                }
                            }
                        }
                    }
                    items.addAll(mediaFileList);
                    multiTypeAdapter.notifyDataSetChanged();

                    //图片文件夹
                    mFolderPopupWindow = new FolderPopupWindow(SelectActivity.this, mediaFolderList);
                    mFolderPopupWindow.setAnimationStyle(R.style.mediaFolderAnimator);

                    mFolderPopupWindow.getAdapter().setOnImageFolderChangeListener((view, position) -> {
                        //切换资源文件夹
                        MediaFolder folder = mediaFolderList.get(position);
                        String folderName = folder.folderName;
                        if (!TextUtils.isEmpty(folderName)) {
                            tvBackTitle.setText(folderName);
                        }

                        Items items1 = (Items) multiTypeAdapter.getItems();
                        items1.clear();

                        items1.addAll(folder.mediaFiles);
                        multiTypeAdapter.notifyDataSetChanged();

                        mFolderPopupWindow.dismiss();
                    });

                    mFolderPopupWindow.setOnDismissListener(() -> setLightMode(LIGHT_ON));
                    updateCommitBtn();
                }
                mProgressDialog.dismiss();
            });
        }
    }

    private void startScannerTask() {
        Runnable mediaLoadTask = null;

        if (SelectSpec.getInstance().mimeType == MimeType.ofAll()) {
            mediaLoadTask = new MediaLoadTask(this, new MediaLoader());
        } else if (SelectSpec.getInstance().mimeType == MimeType.ofImage()) {
            mediaLoadTask = new ImageLoadTask(this, new MediaLoader());
        } else {
            mediaLoadTask = new VideoLoadTask(this, new MediaLoader());
        }

        CommonExcutor.getInstance().excute(mediaLoadTask);
    }

    /**
     * 设置屏幕的亮度模式
     *
     * @param lightMode
     */
    private void setLightMode(int lightMode) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        switch (lightMode) {
            case LIGHT_OFF:
                layoutParams.alpha = 0.7f;
                break;
            case LIGHT_ON:
                layoutParams.alpha = 1.0f;
                break;
        }
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onItemClick(int potision) {
        if (mediaFileList != null) {
            DataUtil.getInstance().setMediaData(mediaFileList);
            Intent intent = new Intent(this,MediaPreActivity.class);
            if (SelectSpec.getInstance().isShowCamera) {
                intent.putExtra(MediaPreActivity.IMAGE_POSITION,potision - 1);
            } else {
                intent.putExtra(MediaPreActivity.IMAGE_POSITION,potision);
            }
            startActivityForResult(intent,PICTURE_PREVIEW_CODE);
        }
    }

    @Override
    public void onChecked(int position) {

        Items items = (Items) multiTypeAdapter.getItems();
        if (items.get(position) instanceof MediaFile) {
            MediaFile mediaFile = (MediaFile) items.get(position);
            if (mediaFile != null) {
                String path = mediaFile.path;

                int type = SelectSpec.getInstance().addStringToSelectPaths(path);

                if (type > 0) {
                    if (type == 1) {
                        mediaFile.checked = true;
                    } else {
                        mediaFile.checked = false;
                    }
                    multiTypeAdapter.notifyDataSetChanged();

                    updateCommitBtn();
                } else {
                    Toast.makeText(this, String.format(getString(R.string.select_image_max),
                            SelectSpec.getInstance().maxSelectable), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void updateCommitBtn() {
        int size = SelectSpec.getInstance().getSelectLenght();
        if (size == 0) {
            tvCommit.setEnabled(false);
            tvCommit.setText(getString(R.string.confirm));
        } else {
            tvCommit.setEnabled(true);
            tvCommit.setText(String.format(getString(R.string.confirm_msg), size, SelectSpec.getInstance().maxSelectable));
        }

    }

    /**********************处理照相机相关的业务*************************/

    //拍照
    @Override
    public void onTakePicture() {
        startCameraImageCapture();
    }

    //拍视频
    @Override
    public void onTakeVideo() {
        startCameraVideo();
    }

    @Override
    public void onTakePicVideo() {
        CameraItemSelectDialog cameraItemSelectDialog = CameraItemSelectDialog.newInstance();
        cameraItemSelectDialog.setOnItemClickListener(position -> {
            if (position == 0) {
                startCameraImageCapture();
            } else if (position == 1) {
                startCameraVideo();
            }
        });

        cameraItemSelectDialog.setOnDismissListener((isCancel, dialog) -> {
            if (isCancel) {

            }
        });

        cameraItemSelectDialog.show(getSupportFragmentManager(), "select");

    }

    /**
     * 录视频
     */
    private void startCameraVideo() {

        File fileDir = new File(Environment.getExternalStorageDirectory(), "videos");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        mVideoFilePath = fileDir.getAbsolutePath() + "/video_" + System.currentTimeMillis() + ".mp4";
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(this, SelectSpec.getInstance().captureStrategy.authority, new File(mVideoFilePath));
        } else {
            uri = Uri.fromFile(new File(mVideoFilePath));
        }
        //跳转到录视频界面
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        //录制视频的画质，0-1， 越大质量越好
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.2);
        //录制完成后保存的地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //设置录制视频的最长时间
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, SelectSpec.getInstance().videoDuraionLimit);
        startActivityForResult(intent, REQUEST_CODE_VIDEO);
    }

    /**
     * 调用系统拍照
     */
    private void startCameraImageCapture() {
        //拍照存放路径
        File fileDir = new File(Environment.getExternalStorageDirectory(), "Pictures");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        mFilePath = fileDir.getAbsolutePath() + "/IMG_" + System.currentTimeMillis() + ".jpg";

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(this, SelectSpec.getInstance().captureStrategy.authority, new File(mFilePath));
        } else {
            uri = Uri.fromFile(new File(mFilePath));
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //拍照
        if (requestCode == REQUEST_CODE_IMAGE) {
            //通知媒体库刷新
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mFilePath)));
            //添加到选中集合
            SelectSpec.getInstance().addStringToSelectPaths(mFilePath);

            BackOrNext();
        } else if (requestCode == REQUEST_CODE_VIDEO) {
            //录视频
            //通知媒体库刷新
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mVideoFilePath)));
            //添加到选中集合
            SelectSpec.getInstance().addStringToSelectPaths(mVideoFilePath);

            BackOrNext();
        } else if (requestCode == PICTURE_PREVIEW_CODE) {
            BackOrNext();
        }
    }

    private void BackOrNext() {
        Intent intent = new Intent();
        ArrayList<String> list = new ArrayList<>(SelectSpec.getInstance().getMediaPathList());
        intent.putStringArrayListExtra(REQUEST_SELECT_MEDIAS, list);

        if (TextUtils.isEmpty(SelectSpec.getInstance().className)) {
            setResult(RESULT_OK, intent);
            finish();
        } else {
            try {
                ComponentName comp = new ComponentName(this,SelectSpec.getInstance().className);
                intent.setComponent(comp);
                intent.setAction("android.intent.action.VIEW");
                if (SelectSpec.getInstance().map != null) {
                    for (Map.Entry<String, String> entry : SelectSpec.getInstance().map.entrySet()) {
                        intent.putExtra(entry.getKey(),entry.getValue());
                    }
                }
                startActivity(intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}