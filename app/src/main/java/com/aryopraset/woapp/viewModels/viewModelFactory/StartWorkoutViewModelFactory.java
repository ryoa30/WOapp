package com.aryopraset.woapp.viewModels.viewModelFactory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.aryopraset.woapp.viewModels.StartWorkoutViewModel;

public class StartWorkoutViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final int workoutId;

    public StartWorkoutViewModelFactory(Application application, int workoutId) {
        this.application = application;
        this.workoutId = workoutId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StartWorkoutViewModel.class)) {
            return (T) new StartWorkoutViewModel(application, workoutId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
