package com.aryopraset.woapp.data;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.aryopraset.woapp.models.User;
import com.aryopraset.woapp.utils.WOAppRoomDatabase;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserRepository {
    private UserDAO userDAO;

    public UserRepository(Application application) {
        WOAppRoomDatabase db = WOAppRoomDatabase.getInstance(application);
        this.userDAO = db.userDAO();
    }

    public LiveData<User> getUser(int id){
        return userDAO.getUser(id);
    }

    public LiveData<List<User>> getAllUser(){
        return userDAO.getAllUser();
    }

    public void insert(User user){
        WOAppRoomDatabase.databaseWriteExecutor.execute(() -> userDAO.insert(user));
    }

    public void update(User user){
        WOAppRoomDatabase.databaseWriteExecutor.execute(() -> userDAO.update(user));
    }
}
