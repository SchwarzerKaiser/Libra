package com.leewilson.libra.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.leewilson.libra.model.Book;

@Database(entities = {Book.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase mInstance;

    public static AppDatabase getInstance(final Context context) {
        if (mInstance == null) {
            synchronized (AppDatabase.class) {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "books_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return mInstance;
    }

    public abstract BookDao getBookDao();
}
