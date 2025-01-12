package com.aryopraset.woapp.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.aryopraset.woapp.models.Set;
import com.aryopraset.woapp.models.Workout;
import com.aryopraset.woapp.utils.WOAppRoomDatabase;

import java.util.List;

public class SetRepository {
    public SetDAO setDAO;

    public SetRepository(Application application) {
        WOAppRoomDatabase db = WOAppRoomDatabase.getInstance(application);
        setDAO = db.setDAO();
    }

    public LiveData<List<Set>> getSets(int exerciseID) {
        return setDAO.getSets(exerciseID);
    }

    public void insert(Set set){
        WOAppRoomDatabase.databaseWriteExecutor.execute(() -> setDAO.insert(set));
    }
    public void update(Set set){
        WOAppRoomDatabase.databaseWriteExecutor.execute(() -> setDAO.update(set));
    }
    public void delete(Set set){
        WOAppRoomDatabase.databaseWriteExecutor.execute(() -> setDAO.delete(set));
    }


}
