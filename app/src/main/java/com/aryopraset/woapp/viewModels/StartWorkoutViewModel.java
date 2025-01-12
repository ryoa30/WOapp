package com.aryopraset.woapp.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.aryopraset.woapp.data.ExerciseRepository;
import com.aryopraset.woapp.data.SetRepository;
import com.aryopraset.woapp.data.UserRepository;
import com.aryopraset.woapp.data.WorkoutRepository;
import com.aryopraset.woapp.models.Exercise;
import com.aryopraset.woapp.models.Set;

import java.util.List;

public class StartWorkoutViewModel extends AndroidViewModel {
    public ExerciseRepository exerciseRepository;
    public WorkoutRepository workoutRepository;
    public SetRepository setRepository;

    public LiveData<List<Exercise>> exercises;
    public StartWorkoutViewModel(@NonNull Application application, int workoutId) {
        super(application);
        setRepository = new SetRepository(application);
        workoutRepository = new WorkoutRepository(application);
        exerciseRepository = new ExerciseRepository(application);
        exercises = exerciseRepository.getAllData(workoutId);
    }

    public LiveData<List<Exercise>> getExerciseByWorkoutId(int workoutId){
        return exercises;
    }

    public LiveData<List<Set>> getSets(int exerciseID){
        return setRepository.getSets(exerciseID);
    }

    public void updateSet(Set set){
        setRepository.update(set);
    }
}
