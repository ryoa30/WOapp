package com.aryopraset.woapp.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.aryopraset.woapp.data.WorkoutRepository;
import com.aryopraset.woapp.models.Workout;

import java.util.List;

public class WorkoutListViewModel extends AndroidViewModel {
    private WorkoutRepository workoutRepository;
    public LiveData<List<Workout>> workouts;
    public WorkoutListViewModel(@NonNull Application application) {
        super(application);
        workoutRepository = new WorkoutRepository(application);
    }

    public LiveData<List<Workout>> getWorkoutExludeDay(int userId, int day){
        workouts = workoutRepository.getWorkoutExcludeDay(userId, day);
        return workouts;
    }

    public LiveData<Workout> getDayWorkout(int userId, int day){
        return workoutRepository.getDayWorkout(userId, day);
    }

    public void updateWorkout(Workout workout){
        workoutRepository.update(workout);
    }

    public long insertWorkout(Workout workout){
        return workoutRepository.insert(workout);
    }
}
