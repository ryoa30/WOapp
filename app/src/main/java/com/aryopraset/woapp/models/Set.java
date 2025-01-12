package com.aryopraset.woapp.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "sets",
foreignKeys = @ForeignKey(
        entity = Exercise.class,
        parentColumns = "id",
        childColumns = "exercise_id",
        onDelete = ForeignKey.CASCADE
))
public class Set {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int exercise_id;
    private int set_number;
    private Double weight;

    public Set() {
    }

    public Set(int exercise_id, int set_number, Double weight) {
        this.exercise_id = exercise_id;
        this.set_number = set_number;
        this.weight = weight;
    }

    public int getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(int exercise_id) {
        this.exercise_id = exercise_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSet_number() {
        return set_number;
    }

    public void setSet_number(int set_number) {
        this.set_number = set_number;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
