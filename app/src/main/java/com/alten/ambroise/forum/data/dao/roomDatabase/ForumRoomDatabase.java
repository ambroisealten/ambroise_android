package com.alten.ambroise.forum.data.dao.roomDatabase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.alten.ambroise.forum.data.dao.ForumDao;
import com.alten.ambroise.forum.data.model.beans.Forum;

@Database(entities = {Forum.class}, version = 1, exportSchema = false)
public abstract class ForumRoomDatabase extends RoomDatabase {
    private static volatile ForumRoomDatabase INSTANCE;
    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    public static ForumRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ForumRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ForumRoomDatabase.class, "forum_database")
                            .addCallback(sRoomDatabaseCallback) //TODO DELETE ON PROD
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ForumDao forumRepository();

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ForumDao mDao;

        PopulateDbAsync(ForumRoomDatabase db) {
            mDao = db.forumRepository();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            for (int i = 0; i < 10; i++) {
                Forum forum = new Forum();
                forum.setDate("10/07/2019");
                forum.setName("name" + i);
                forum.setPlace("place" + i);
                mDao.insert(forum);
            }
            return null;
        }
    }
}
