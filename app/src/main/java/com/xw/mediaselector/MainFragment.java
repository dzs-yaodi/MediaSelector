package com.xw.mediaselector;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class MainFragment extends Fragment implements View.OnClickListener {

    private String[] permission = {Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private RxPermissions rxPermissions;
    private List<String> list = new ArrayList<>();
    private RecyclerView recycler;
    private ImageGridAdapter gridAdapter;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler = view.findViewById(R.id.recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 3);
        recycler.setLayoutManager(layoutManager);

        gridAdapter = new ImageGridAdapter(list);
        recycler.setAdapter(gridAdapter);
        Router.getInstance().init(this);

        rxPermissions = new RxPermissions(getActivity());

        view.findViewById(R.id.btn_all).setOnClickListener(this);
        view.findViewById(R.id.btn_image).setOnClickListener(this);
        view.findViewById(R.id.btn_video1).setOnClickListener(this);
        view.findViewById(R.id.btn_video2).setOnClickListener(this);
        Button turn = view.findViewById(R.id.turn);
        turn.setText("Activity");
        turn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Disposable d = rxPermissions.request(permission[0],permission[1],permission[2])
                .subscribe(granted -> {
                    if (granted) {
                        if (id == R.id.btn_all) {
                            Router.getInstance()
                                    .choose(MimeType.ofAll())
                                    .showCamera(true)
                                    .captureStrategy(new CaptureStrategy(true,getActivity().getPackageName() + ".MyProvider"))
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
                            Router.getInstance()
                                    .choose(MimeType.ofImage())
                                    .showCamera(true)
                                    .captureStrategy(new CaptureStrategy(true,getActivity().getPackageName() + ".MyProvider"))
                                    .maxSelectable(9)
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
                        } else if (id == R.id.btn_video1) {
                            Router.getInstance()
                                    .choose(MimeType.ofVideo())
                                    .showCamera(true)
                                    .captureStrategy(new CaptureStrategy(true,getActivity().getPackageName() + ".MyProvider"))
                                    .maxSelectable(1)
                                    .addListToSelectPaths(list)
                                    .imageEngine(new GlideEngine())
                                    .startLauncher(result -> {
                                        if (result.getData() != null) {
                                            list.clear();
                                            list.addAll(MediaSelector.obtainPathsResult(result.getData()));

                                            gridAdapter.notifyDataSetChanged();
                                        }
                                    });
                        } else if (id == R.id.btn_video2) {
                            Router.getInstance()
                                    .choose(MimeType.ofVideo())
                                    .showCamera(true)
                                    .captureStrategy(new CaptureStrategy(true,getActivity().getPackageName() + ".MyProvider"))
                                    .maxSelectable(1)
                                    .toCustomVideo(true)
                                    .addListToSelectPaths(list)
                                    .imageEngine(new GlideEngine())
                                    .startLauncher(result -> {
                                        if (result.getData() != null) {
                                            list.clear();
                                            String videoPath = result.getData().getStringExtra(CustomVideoActivity.EXTRA_RESULT_VIDEO_URI);
                                            list.add(videoPath);

                                            gridAdapter.notifyDataSetChanged();
                                        }
                                    });
                        } else if (id == R.id.turn) {
                            getActivity().finish();
                        }
                    }
                });
        disposables.add(d);
    }

    @Override
    public void onDestroyView() {
        disposables.clear();
        super.onDestroyView();
    }
}
