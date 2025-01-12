package com.aryopraset.woapp.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.aryopraset.woapp.data.ExerciseRepository;
import com.aryopraset.woapp.data.UserRepository;
import com.aryopraset.woapp.data.WorkoutRepository;
import com.aryopraset.woapp.models.Exercise;
import com.aryopraset.woapp.models.User;
import com.aryopraset.woapp.models.Workout;

import java.util.List;

public class HomepageViewModel extends AndroidViewModel {

    public ExerciseRepository exerciseRepository;
    public WorkoutRepository workoutRepository;
    public UserRepository userRepository;

    public HomepageViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        workoutRepository = new WorkoutRepository(application);
        exerciseRepository = new ExerciseRepository(application);
    }
    public void updateUser(User user){
        userRepository.update(user);
    }

    public LiveData<Workout> getDayWorkout(int userId, int day){
        return workoutRepository.getDayWorkout(userId, day);
    }

    public LiveData<List<Exercise>> getAllData(int workoutId){
        return exerciseRepository.getAllData(workoutId);
    }

    public LiveData<User> getUser(int id){
        return userRepository.getUser(id);
    }
}
