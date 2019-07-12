package com.alten.ambroise.forum.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.alten.ambroise.forum.beans.Forum;

@Database(entities = {Forum.class}, version = 1)
public abstract class ForumRoomDatabase extends RoomDatabase {
    public abstract ForumDao forumRepository();

    private static volatile ForumRoomDatabase INSTANCE;

    static ForumRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ForumRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ForumRoomDatabase.class, "forum_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
