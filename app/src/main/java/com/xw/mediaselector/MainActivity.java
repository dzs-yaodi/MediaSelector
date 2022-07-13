package com.xw.mediaselector;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xw.selector.MediaSelector;
import com.xw.selector.MimeType;
import com.xw.selector.Router;
import com.xw.selector.entity.CaptureStrategy;
import com.xw.selector.ui.CustomVideoActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_MEDIA = 10111;
    private static final int REQUEST_VIDEO = 10112;

    private String[] permission = {Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private RxPermissions rxPermissions;
    private List<String> list = new ArrayList<>();
    private RecyclerView recycler;
    private ImageGridAdapter gridAdapter;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recycler.setLayoutManager(layoutManager);

        gridAdapter = new ImageGridAdapter(list);
        recycler.setAdapter(gridAdapter);
        Router.getInstance().init(this);

        rxPermissions = new RxPermissions(this);

        findViewById(R.id.btn_all).setOnClickListener(this);
        findViewById(R.id.btn_image).setOnClickListener(this);
        findViewById(R.id.btn_video1).setOnClickListener(this);
        findViewById(R.id.btn_video2).setOnClickListener(this);
        findViewById(R.id.turn).setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)return;

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_MEDIA) {
                list.clear();
                list.addAll(MediaSelector.obtainPathsResult(data));
                if (list.size() > 0) {
                    gridAdapter.notifyDataSetChanged();
                }
            } else if (requestCode == REQUEST_VIDEO) {
                String videoPath = data.getStringExtra(CustomVideoActivity.EXTRA_RESULT_VIDEO_URI);
                list.add(videoPath);

                gridAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Disposable d = rxPermissions.request(permission[0],permission[1],permission[2])
                .subscribe(granted -> {
                    if (granted) {
                        if (id == R.id.btn_all) {
//                            MediaSelector
//                                    .create(this)
//                                    .choose(MimeType.ofAll())
//                                    .showCamera(true)
//                                    .captureStrategy(new CaptureStrategy(true,"com.xw.mediaselector.MyProvider"))
//                                    .maxSelectable(9)
//                                    .addListToSelectPaths(list)
//                                    .imageEngine(new GlideEngine())
//                                    .start(REQUEST_MEDIA);
                            Router.getInstance()
                                    .choose(MimeType.ofAll())
                                    .showCamera(true)
                                    .captureStrategy(new CaptureStrategy(true,getPackageName() + ".MyProvider"))
                                    .maxSelectable(9 - list.size())
                                    .addListToSelectPaths(list)
                                    .imageEngine(new GlideEngine())
                                    .startLauncher(result -> {
                                        if (result.getData() != null) {
                                            list.clear();
                                            list.addAll(MediaSelector.obtainPathsResult(result.getData()));
                                            if (list.size() > 0) {
                                                gridAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                        } else if (id == R.id.btn_image) {
                            MediaSelector
                                    .create(this)
                                    .choose(MimeType.ofImage())
                                    .showCamera(true)
                                    .captureStrategy(new CaptureStrategy(true,"com.xw.mediaselector.MyProvider"))
                                    .maxSelectable(9)
                                    .addListToSelectPaths(list)
                                    .imageEngine(new GlideEngine())
                                    .start(REQUEST_MEDIA);
                        } else if (id == R.id.btn_video1) {
                            MediaSelector
                                    .create(this)
                                    .choose(MimeType.ofVideo())
                                    .showCamera(true)
                                    .captureStrategy(new CaptureStrategy(true,"com.xw.mediaselector.MyProvider"))
                                    .maxSelectable(1)
                                    .addListToSelectPaths(list)
                                    .imageEngine(new GlideEngine())
                                    .start(REQUEST_MEDIA);
                        } else if (id == R.id.btn_video2) {
                            MediaSelector
                                    .create(this)
                                    .choose(MimeType.ofVideo())
                                    .showCamera(true)
                                    .captureStrategy(new CaptureStrategy(true,"com.xw.mediaselector.MyProvider"))
                                    .maxSelectable(1)
                                    .toCustomVideo(true)
                                    .addListToSelectPaths(list)
                                    .imageEngine(new GlideEngine())
                                    .start(REQUEST_VIDEO);
                        } else if (id == R.id.turn) {
                            startActivity(new Intent(this,MainActivity2.class));
                        }
                    }
                });
        disposables.add(d);
    }

    @Override
    protected void onDestroy() {
        disposables.clear();
        super.onDestroy();
    }
}