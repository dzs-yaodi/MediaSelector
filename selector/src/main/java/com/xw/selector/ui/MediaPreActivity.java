package com.xw.selector.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xw.selector.R;
import com.xw.selector.adapter.MediaPreAdapter;
import com.xw.selector.entity.SelectSpec;
import com.xw.selector.pojo.MediaFile;
import com.xw.selector.utils.CommonUtils;
import com.xw.selector.utils.DataUtil;

import java.util.ArrayList;
import java.util.List;

public class MediaPreActivity extends AppCompatActivity {

    public static final String IMAGE_POSITION = "imagePosition";
    private List<MediaFile> mMediaFileList;
    private int mPosition = 0;

    private TextView mTvTitle;
    private TextView mTvCommit;
    private ViewPager mViewPager;
    private CheckBox mCheckBox;
    private ImageView mImageBack;

    private MediaPreAdapter mMediaPreAdapter;
    private ImageView mImageVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setTitleBar(getWindow(),"#303030");
        setContentView(R.layout.activity_media_pre);

        mImageBack = findViewById(R.id.image_back);
        mTvTitle = findViewById(R.id.tv_title);
        mTvCommit = findViewById(R.id.tv_commit);
        mCheckBox = findViewById(R.id.checkbox);
        mViewPager = findViewById(R.id.viewPager);
        mImageVideo = findViewById(R.id.image_video);

        getDatas();
        setListener();
    }

    private void getDatas() {
        mMediaFileList = DataUtil.getInstance().getMediaData();
        mPosition = getIntent().getIntExtra(IMAGE_POSITION,0);
        mTvTitle.setText(String.format("%d/%d", mPosition + 1, mMediaFileList.size()));

        mMediaPreAdapter = new MediaPreAdapter(mMediaFileList,this);
        mViewPager.setAdapter(mMediaPreAdapter);
        mViewPager.setCurrentItem(mPosition);

        checkImgOrVideo(mMediaFileList.get(mPosition));
        mCheckBox.setChecked(mMediaFileList.get(mPosition).checked);
        updateCommitBtn();
    }

    private void setListener() {
        mImageBack.setOnClickListener(v -> finish());

        mCheckBox.setOnClickListener(v -> {

            String path = mMediaFileList.get(mViewPager.getCurrentItem()).path;
            int type = SelectSpec.getInstance().addStringToSelectPaths(path);

            if (type > 0) {
                if (type == 1) {
                    mCheckBox.setChecked(true);
                } else {
                    mCheckBox.setChecked(false);
                }
                updateCommitBtn();
            } else {
                mCheckBox.setChecked(false);
                Toast.makeText(this, String.format(getString(R.string.select_image_max),
                        SelectSpec.getInstance().maxSelectable), Toast.LENGTH_SHORT).show();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTvTitle.setText(String.format("%d/%d", position + 1, mMediaFileList.size()));
                checkImgOrVideo(mMediaFileList.get(position));
                mCheckBox.setChecked(mMediaFileList.get(position).checked);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTvCommit.setOnClickListener(v -> {
            setResult(RESULT_OK, new Intent());
            finish();
        });
    }

    private void checkImgOrVideo(MediaFile mediaFile) {
        if (mediaFile.duration > 0) {
            mImageVideo.setVisibility(View.VISIBLE);
        } else {
            mImageVideo.setVisibility(View.GONE);
        }
    }

    private void updateCommitBtn() {
        int size = SelectSpec.getInstance().getSelectLenght();
        if (size == 0) {
            mTvCommit.setEnabled(false);
            mTvCommit.setText(getString(R.string.confirm));
        } else {
            mTvCommit.setEnabled(true);
            mTvCommit.setText(String.format(getString(R.string.confirm_msg), size, SelectSpec.getInstance().maxSelectable));
        }

    }
}