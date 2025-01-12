package com.aryopraset.woapp.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Update;

import com.aryopraset.woapp.models.Workout;
import com.aryopraset.woapp.utils.WOAppRoomDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

import kotlinx.coroutines.scheduling.WorkQueueKt;

public class WorkoutRepository {
    public WorkoutDAO workoutDAO;

    public WorkoutRepository(Application application) {
        WOAppRoomDatabase db = WOAppRoomDatabase.getInstance(application);
        workoutDAO = db.workoutDAO();
    }

    public LiveData<List<Workout>> getAllWorkouts(int userId){
        return workoutDAO.getAllWorkout(userId);
    }

    public LiveData<Workout> getDayWorkout(int userId, int day){
        return workoutDAO.getDayWorkout(userId, day);
    }

    public LiveData<List<Workout>> getWorkoutExcludeDay(int userID, int day){
        return workoutDAO.getWorkoutExcludeDay(userID, day);
    }

    public long insert(Workout workout){
        try {
            return WOAppRoomDatabase.databaseWriteExecutor.submit(() -> workoutDAO.insert(workout)).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(Workout workout){
        WOAppRoomDatabase.databaseWriteExecutor.execute(() -> workoutDAO.update(workout));
    }
    public void delete(Workout workout){
        WOAppRoomDatabase.databaseWriteExecutor.execute(() -> workoutDAO.delete(workout));
    }


}
