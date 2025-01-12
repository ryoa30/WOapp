package com.aryopraset.woapp.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercises",
foreignKeys = @ForeignKey(
        entity = Workout.class,
        parentColumns = "id",
        childColumns = "workout_id",
        onDelete = ForeignKey.CASCADE
))
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int workout_id;
    private String exercise_name;
    private int number_of_sets;
    private int rest_time_minutes;
    private int rest_time_seconds;
    private int target_reps_min;
    private int target_reps_max;
    private int order;

    public Exercise() {
    }

    public Exercise(String exercise_name, int number_of_sets, int order, int rest_time_minutes, int rest_time_seconds, int target_reps_max, int target_reps_min, int workout_id) {
        this.exercise_name = exercise_name;
        this.number_of_sets = number_of_sets;
        this.order = order;
        this.rest_time_minutes = rest_time_minutes;
        this.rest_time_seconds = rest_time_seconds;
        this.target_reps_max = target_reps_max;
        this.target_reps_min = target_reps_min;
        this.workout_id = workout_id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getExercise_name() {
        return exercise_name;
    }

    public void setExercise_name(String exercise_name) {
        this.exercise_name = exercise_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber_of_sets() {
        return number_of_sets;
    }

    public void setNumber_of_sets(int number_of_sets) {
        this.number_of_sets = number_of_sets;
    }

    public int getRest_time_minutes() {
        return rest_time_minutes;
    }

    public void setRest_time_minutes(int rest_time_minutes) {
        this.rest_time_minutes = rest_time_minutes;
    }

    public int getRest_time_seconds() {
        return rest_time_seconds;
    }

    public void setRest_time_seconds(int rest_time_seconds) {
        this.rest_time_seconds = rest_time_seconds;
    }

    public int getTarget_reps_max() {
        return target_reps_max;
    }

    public void setTarget_reps_max(int target_reps_max) {
        this.target_reps_max = target_reps_max;
    }

    public int getTarget_reps_min() {
        return target_reps_min;
    }

    public void setTarget_reps_min(int target_reps_min) {
        this.target_reps_min = target_reps_min;
    }

    public int getWorkout_id() {
        return workout_id;
    }

    public void setWorkout_id(int workout_id) {
        this.workout_id = workout_id;
    }
}
