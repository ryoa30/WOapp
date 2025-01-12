package com.aryopraset.woapp.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.aryopraset.woapp.data.ExerciseDAO;
import com.aryopraset.woapp.data.SetDAO;
import com.aryopraset.woapp.data.UserDAO;
import com.aryopraset.woapp.data.WorkoutDAO;
import com.aryopraset.woapp.models.Exercise;
import com.aryopraset.woapp.models.Set;
import com.aryopraset.woapp.models.User;
import com.aryopraset.woapp.models.Workout;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Exercise.class, Set.class, User.class, Workout.class}, version = 1, exportSchema = false)
public abstract class WOAppRoomDatabase extends RoomDatabase {
    public abstract WorkoutDAO workoutDAO();
    public abstract ExerciseDAO exerciseDAO();
    public abstract SetDAO setDAO();
    public abstract UserDAO userDAO();

    public static final int NUMBER_OF_THREADS = 4;

    public static volatile WOAppRoomDatabase INSTANCE;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static WOAppRoomDatabase getInstance(final Context context){
        if(INSTANCE == null){
            synchronized (WOAppRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WOAppRoomDatabase.class, "WOApp_database")
                            .addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    public static final RoomDatabase.Callback sRoomDatabaseCallback =
        new RoomDatabase.Callback(){
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                databaseWriteExecutor.execute(() -> {
                    Log.d("TAGGING", "onCreate: ");
                    SetDAO setDAO = INSTANCE.setDAO();
                    ExerciseDAO exerciseDAO = INSTANCE.exerciseDAO();
                    WorkoutDAO workoutDAO = INSTANCE.workoutDAO();
                    UserDAO userDAO = INSTANCE.userDAO();
                    User user = new User("primaryUser@gmail.com", "User");
                    userDAO.insert(user);
                    Workout workout = new Workout(-1, 1, "Chest");
                    int workoutId = (int) workoutDAO.insert(workout);

                    Exercise exercise1 = new Exercise("Bench Press", 3, 1, 2, 0, 12, 8, workoutId);
                    int exerciseID = (int) exerciseDAO.insert(exercise1);

                    ArrayList<Set> sets = new ArrayList<>();

                    sets.add(new Set(exerciseID, 1, 12.5));
                    sets.add(new Set(exerciseID, 2, 15.0));
                    sets.add(new Set(exerciseID, 3, 17.5));

                    for(Set set : sets){
                        setDAO.insert(set);
                    }

                    exercise1 = new Exercise("Incline Press", 3, 2, 2, 0, 12, 8, workoutId);
                    exerciseID = (int) exerciseDAO.insert(exercise1);

                    sets = new ArrayList<>();

                    sets.add(new Set(exerciseID, 1, 12.5));
                    sets.add(new Set(exerciseID, 2, 15.0));
                    sets.add(new Set(exerciseID, 3, 17.5));

                    for(Set set : sets){
                        setDAO.insert(set);
                    }

                    exercise1 = new Exercise("Chest Fly", 3, 3, 3, 0, 12, 8, workoutId);
                    exerciseID = (int) exerciseDAO.insert(exercise1);

                    sets = new ArrayList<>();

                    sets.add(new Set(exerciseID, 1, 20.0));
                    sets.add(new Set(exerciseID, 2, 25.0));
                    sets.add(new Set(exerciseID, 3, 30.0));

                    for(Set set : sets){
                        setDAO.insert(set);
                    }

                });
            }
        };
}
