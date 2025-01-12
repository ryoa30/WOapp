package com.aryopraset.woapp.utils;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class LiveDataUtils {
    public static <T> void observeOnce(LiveData<T> liveData, LifecycleOwner lifecycleOwner, Observer<T> observer) {
        liveData.observe(lifecycleOwner, new Observer<T>() {
            @Override
            public void onChanged(T t) {
                observer.onChanged(t);
                liveData.removeObserver(this); // Remove observer after first change
            }
        });
    }
}
