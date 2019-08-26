package com.alten.ambroise.forum.data.dao.roomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.alten.ambroise.forum.data.dao.ForumDao;
import com.alten.ambroise.forum.data.model.beans.Forum;

@Database(entities = {Forum.class}, version = 1, exportSchema = false)
public abstract class ForumRoomDatabase extends RoomDatabase {
    private static volatile ForumRoomDatabase INSTANCE;

    public static ForumRoomDatabase getDatabase(final Context context) {
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

    public abstract ForumDao forumRepository();

}
