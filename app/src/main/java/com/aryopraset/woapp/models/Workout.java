package com.aryopraset.woapp.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Workouts",
foreignKeys = @ForeignKey(
        entity = User.class,
        parentColumns = "id",
        childColumns = "user_id",
        onDelete = ForeignKey.CASCADE
))
public class Workout {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int user_id;
    private int day_of_week;
    private String workout_name;

    public Workout() {
    }

    public Workout(int day_of_week,  int user_id, String workout_name) {
        this.day_of_week = day_of_week;
        this.user_id = user_id;
        this.workout_name = workout_name;
    }

    public int getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getWorkout_name() {
        return workout_name;
    }

    public void setWorkout_name(String workout_name) {
        this.workout_name = workout_name;
    }
}
