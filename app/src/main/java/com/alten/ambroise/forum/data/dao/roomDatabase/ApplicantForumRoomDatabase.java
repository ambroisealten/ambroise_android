package com.alten.ambroise.forum.data.dao.roomDatabase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.alten.ambroise.forum.data.dao.ApplicantForumDao;
import com.alten.ambroise.forum.data.model.Mobility;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;

import java.util.Arrays;


@Database(entities = {ApplicantForum.class}, version = 1, exportSchema = false)
public abstract class ApplicantForumRoomDatabase extends RoomDatabase {
    private static volatile ApplicantForumRoomDatabase INSTANCE;
    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

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

    public abstract ApplicantForumDao applicantForumRepository();

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ApplicantForumDao mDao;

        PopulateDbAsync(ApplicantForumRoomDatabase db) {
            mDao = db.applicantForumRepository();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            for (int i = 0; i < 10; i++) {
                ApplicantForum applicant = new ApplicantForum();
                applicant.setMail("mail" + i + "@mail.com");
                applicant.setName("name" + i);
                applicant.setSurname("surname" + i);
                applicant.setPersonInChargeMail("inchargeMail@mail.com");
                applicant.setHighestDiploma("ALTEN SCHOOL");
                applicant.setHighestDiplomaYear("23/07/2019");
                applicant.setGrade("+++");
                applicant.setStartAt("01/01/2020");
                applicant.setMobilities(Arrays.asList(new Mobility(),new Mobility()));
                mDao.insert(applicant);
            }
            return null;
        }
    }
}
