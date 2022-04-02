package com.xw.selector.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xw.selector.MimeType;
import com.xw.selector.R;
import com.xw.selector.entity.SelectSpec;
import com.xw.selector.listener.OnCameraListener;
import com.xw.selector.pojo.MediaFile;

import me.drakeet.multitype.ItemViewBinder;

public class CameraBinder extends ItemViewBinder<MediaFile, CameraBinder.CameraViewHolder> {

    private OnCameraListener onCameraListener;

    public CameraBinder(OnCameraListener onCameraListenerl) {
        this.onCameraListener = onCameraListenerl;
    }

    @NonNull
    @Override
    protected CameraViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new CameraViewHolder(inflater.inflate(R.layout.item_media_camera_layout,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull CameraViewHolder holder, @NonNull MediaFile mediaFile) {

    }

    class CameraViewHolder extends RecyclerView.ViewHolder {

        public CameraViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(v -> {
                if (SelectSpec.getInstance().getSelectLenght() < SelectSpec.getInstance().maxSelectable) {
                    if (onCameraListener != null) {
                      if (SelectSpec.getInstance().mimeType == MimeType.ofAll()) {
                          onCameraListener.onTakePicVideo();
                      }  else if (SelectSpec.getInstance().mimeType == MimeType.ofImage()) {
                          onCameraListener.onTakePicture();
                      } else {
                          onCameraListener.onTakeVideo();
                      }
                    }
                }
            });
        }
    }
}
