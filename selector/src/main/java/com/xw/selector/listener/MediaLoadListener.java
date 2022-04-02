package com.xw.selector.listener;

import com.xw.selector.pojo.MediaFolder;

import java.util.List;

public interface MediaLoadListener {

    void loadMediaSuccess(List<MediaFolder> mediaFolderList);
}
