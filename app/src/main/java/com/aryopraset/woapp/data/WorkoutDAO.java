package com.aryopraset.woapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.aryopraset.woapp.models.Workout;

import java.util.List;

@Dao
public interface WorkoutDAO {
    @Query("SELECT * FROM workouts WHERE workouts.user_id == :userId ORDER BY workouts.day_of_week ASC")
    LiveData<List<Workout>> getAllWorkout(int userId);
    @Query("SELECT * FROM workouts WHERE workouts.user_id == :userId AND workouts.day_of_week == :day ORDER BY workouts.day_of_week ASC")
    LiveData<Workout> getDayWorkout(int userId, int day);
    @Query("SELECT * FROM workouts WHERE workouts.user_id == :userId AND workouts.day_of_week != :day ORDER BY workouts.day_of_week ASC")
    LiveData<List<Workout>> getWorkoutExcludeDay(int userId, int day);
    @Query("SELECT workout_name FROM workouts WHERE user_id == :userId AND day_of_week == :day LIMIT 1")
    String getWorkoutForToday(int userId, int day);
    @Insert
    long insert(Workout workout);
    @Update
    void update(Workout workout);
    @Delete
    void delete(Workout workout);
}
