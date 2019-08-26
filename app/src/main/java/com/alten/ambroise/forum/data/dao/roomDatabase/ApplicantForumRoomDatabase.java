package com.alten.ambroise.forum.data.dao.roomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.alten.ambroise.forum.data.dao.ApplicantForumDao;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;


@Database(entities = {ApplicantForum.class}, version = 1, exportSchema = false)
public abstract class ApplicantForumRoomDatabase extends RoomDatabase {
    private static volatile ApplicantForumRoomDatabase INSTANCE;

    public static ApplicantForumRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ApplicantForumRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ApplicantForumRoomDatabase.class, "applicantForum_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ApplicantForumDao applicantForumRepository();

}
