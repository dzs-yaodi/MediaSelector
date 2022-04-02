package com.xw.selector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xw.selector.ui.SelectActivity;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Set;

public class MediaSelector {

    private WeakReference<Activity> mActivity;
    private WeakReference<Fragment> mFragment;

    private MediaSelector(Activity activity) {
        this(activity,null);
    }

    private MediaSelector(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    private MediaSelector(Activity activity,Fragment fragment) {
        mActivity = new WeakReference<>(activity);
        mFragment = new WeakReference<>(fragment);
    }

    public static MediaSelector create(Context context) {
        return new MediaSelector((Activity) context);
    }

    public static MediaSelector create(Activity activity) {
        return new MediaSelector(activity);
    }

    public static MediaSelector create(Fragment fragment) {
        return new MediaSelector(fragment);
    }

    public SelectCreator choose(int mediaType) {
        return new SelectCreator(this,mediaType);
    }

    public static List<String> obtainPathsResult(Intent intent) {
        return intent.getStringArrayListExtra(SelectActivity.REQUEST_SELECT_MEDIAS);
    }

    @Nullable
    Activity getActivity() {
        return mActivity.get();
    }

    @Nullable
    Fragment getFragment() {
        return mFragment != null ? mFragment.get() : null;
    }
}
