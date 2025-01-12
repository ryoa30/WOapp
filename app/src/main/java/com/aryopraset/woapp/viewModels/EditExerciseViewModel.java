package com.aryopraset.woapp.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.RoomDatabase;

import com.aryopraset.woapp.data.ExerciseRepository;
import com.aryopraset.woapp.data.SetRepository;
import com.aryopraset.woapp.data.WorkoutRepository;
import com.aryopraset.woapp.models.Exercise;
import com.aryopraset.woapp.models.Set;
import com.aryopraset.woapp.models.Workout;

import java.util.List;

public class EditExerciseViewModel extends AndroidViewModel {
    WorkoutRepository workoutRepository;
    ExerciseRepository exerciseRepository;
    SetRepository setRepository;

    public EditExerciseViewModel(@NonNull Application application) {
        super(application);
        workoutRepository = new WorkoutRepository(application);
        exerciseRepository = new ExerciseRepository(application);
        setRepository = new SetRepository(application);
    }

    public LiveData<Workout> getDayWorkout(int userId, int day){
        return workoutRepository.getDayWorkout(userId, day);
    }

    public LiveData<List<Exercise>> getAllData(int workoutId){
        return exerciseRepository.getAllData(workoutId);
    }

    public LiveData<List<Set>> getSets(int exerciseID){
        return setRepository.getSets(exerciseID);
    }

    public LiveData<Exercise> getLastOrder(int workoutId){
        return exerciseRepository.getLastOrder(workoutId);
    }

    public void updateExercise(Exercise exercise){
        exerciseRepository.update(exercise);
    }

    public void updateSet(Set set){
        setRepository.update(set);
    }

    public long insertExercise(Exercise exercise){
        return exerciseRepository.insert(exercise);
    }

    public void insertSet(Set set){
        setRepository.insert(set);
    }

    public long insertWorkout(Workout workout){
        return workoutRepository.insert(workout);
    }

    public void deleteExercise(Exercise exercise){
        exerciseRepository.delete(exercise);
    }

    public void deleteSet(Set set){
        setRepository.delete(set);
    }

    public LiveData<Exercise> getExerciseById(int exerciseId){
        return exerciseRepository.getExercise(exerciseId);
    }

    public int swapExercises(List<Exercise> exercises, int pos1, int pos2) {

        if(pos1 < 0 || pos1>=exercises.size() || pos2 < 0 || pos2>=exercises.size() ){
            return 1;
        }
        Exercise exercise1 = exercises.get(pos1);
        Exercise exercise2 = exercises.get(pos2);


        // Swap their order
        int tempOrder = exercise1.getOrder();
        exercise1.setOrder(exercise2.getOrder());
        exercise2.setOrder(tempOrder);

        // Update the list after swapping
        exercises.set(pos1, exercise2);
        exercises.set(pos2, exercise1);

        return 0;
    }

    public void swapAndUpdateExercises(List<Exercise> exercises, int pos1, int pos2) {
        // Swap the exercises in the list
        int flag = swapExercises(exercises, pos1, pos2);

        if(flag == 1){
            return;
        }

        exerciseRepository.updateExercises(exercises);
    }


    public LiveData<Exercise> getExerciseBasedOnOrder(int workoutId, int order){
        return exerciseRepository.getExerciseBasedOnOrder(workoutId, order);
    }

}
