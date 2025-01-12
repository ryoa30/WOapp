package com.aryopraset.woapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.aryopraset.woapp.models.Exercise;

import java.util.List;

@Dao
public interface ExerciseDAO {
    @Insert
    long insert(Exercise exercise);
    @Query("SELECT * FROM exercises WHERE exercises.workout_id == :workoutId ORDER BY exercises.`order` ASC")
    LiveData<List<Exercise>> getExercisesByWorkoutId(int workoutId);
    @Delete
    void delete(Exercise exercise);
    @Update
    void update(Exercise exercise);
    @Update
    void updateExercises(List<Exercise> exercises);
    @Query("SELECT * FROM exercises WHERE exercises.id == :exerciseId")
    LiveData<Exercise> getExercise(int exerciseId);
    @Query("SELECT * FROM exercises WHERE exercises.workout_id == :workoutId ORDER BY exercises.`order` DESC LIMIT 1")
    LiveData<Exercise> getLastOrder(int workoutId);

    @Query("SELECT * FROM exercises WHERE exercises.workout_id == :workoutId AND exercises.`order` == :order")
    LiveData<Exercise> getExerciseBasedOnOrder(int workoutId, int order);
}
