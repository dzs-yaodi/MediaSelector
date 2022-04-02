package com.xw.selector.utils;

import com.xw.selector.pojo.MediaFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存搜索的媒体资源，避免预览图片的时候重新查询
 */
public class DataUtil {

    private static volatile DataUtil mDataUtilInstance;
    private List<MediaFile> mData = new ArrayList<>();

    private DataUtil() {
    }

    public static DataUtil getInstance() {
        if (mDataUtilInstance == null) {
            synchronized (DataUtil.class) {
                if (mDataUtilInstance == null) {
                    mDataUtilInstance = new DataUtil();
                }
            }
        }
        return mDataUtilInstance;
    }

    public List<MediaFile> getMediaData() {
        return mData;
    }

    public void setMediaData(List<MediaFile> data) {
        this.mData = data;
    }


}
