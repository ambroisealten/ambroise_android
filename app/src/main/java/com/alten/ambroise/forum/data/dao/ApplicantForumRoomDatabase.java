package com.alten.ambroise.forum.data.dao;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.alten.ambroise.forum.data.beans.ApplicantForum;


@Database(entities = {ApplicantForum.class}, version = 1)
public abstract class ApplicantForumRoomDatabase extends RoomDatabase {
    public abstract ApplicantForumDao applicantForumRepository();

    private static volatile ApplicantForumRoomDatabase INSTANCE;

    public static ApplicantForumRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ApplicantForumRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ApplicantForumRoomDatabase.class, "applicantForum_database")
                            .addCallback(sRoomDatabaseCallback) //TODO DELETE ON PROD
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ApplicantForumDao mDao;

        PopulateDbAsync(ApplicantForumRoomDatabase db) {
            mDao = db.applicantForumRepository();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            for(int i = 0;i<100;i++){
                ApplicantForum applicant = new ApplicantForum();
                applicant.setMail("mail"+i+"@mail.com");
                applicant.setName("name"+i);
                applicant.setSurname("surname"+i);
                applicant.setPersonInChargeMail("inchargeMail@mail.com");
                mDao.insert(applicant);
            }
            return null;
        }
    }
}
