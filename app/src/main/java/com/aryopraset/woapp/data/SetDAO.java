package com.aryopraset.woapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.aryopraset.woapp.models.Set;

import java.util.List;

@Dao
public interface SetDAO {
    @Query("SELECT * FROM sets WHERE sets.exercise_id == :exerciseId ORDER BY sets.set_number ASC")
    LiveData<List<Set>> getSets(int exerciseId);
    @Insert
    void insert(Set set);
    @Update
    void update(Set set);
    @Delete
    void delete(Set set);
}
