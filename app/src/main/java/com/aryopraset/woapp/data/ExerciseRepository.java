package com.aryopraset.woapp.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.aryopraset.woapp.models.Exercise;
import com.aryopraset.woapp.utils.WOAppRoomDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExerciseRepository {
    private ExerciseDAO exerciseDAO;

    public ExerciseRepository(Application application) {
        WOAppRoomDatabase db = WOAppRoomDatabase.getInstance(application);
        exerciseDAO = db.exerciseDAO();
    }

    public LiveData<List<Exercise>> getAllData(int workoutID){
        return exerciseDAO.getExercisesByWorkoutId(workoutID);
    }

    public LiveData<Exercise> getExercise(int exerciseId){
        return exerciseDAO.getExercise(exerciseId);
    }

    public LiveData<Exercise> getExerciseBasedOnOrder(int workoutId, int order){
        return  exerciseDAO.getExerciseBasedOnOrder(workoutId, order);
    }

    public LiveData<Exercise> getLastOrder(int workoutId){
        return exerciseDAO.getLastOrder(workoutId);
    }

    public long insert(Exercise exercise) {
        try {
            return WOAppRoomDatabase.databaseWriteExecutor.submit(() -> exerciseDAO.insert(exercise)).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Exercise exercise){
        WOAppRoomDatabase.databaseWriteExecutor.execute(() -> exerciseDAO.update(exercise));
    }
    public void updateExercises(List<Exercise> exercises){
        WOAppRoomDatabase.databaseWriteExecutor.execute(() -> exerciseDAO.updateExercises(exercises));
    }

    public void delete(Exercise exercise){
        WOAppRoomDatabase.databaseWriteExecutor.execute(() -> exerciseDAO.delete(exercise));
    }
}
