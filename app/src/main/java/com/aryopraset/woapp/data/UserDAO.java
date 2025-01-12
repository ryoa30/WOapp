package com.aryopraset.woapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.aryopraset.woapp.models.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUser();
    @Query("SELECT * FROM users WHERE id == :id")
    LiveData<User> getUser(int id);
    @Insert
    void insert(User user);
    @Update
    void update(User user);
}
