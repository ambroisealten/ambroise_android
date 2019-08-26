package com.alten.ambroise.forum.data.dao.roomDatabase;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.alten.ambroise.forum.App;
import com.alten.ambroise.forum.data.dao.DocumentDao;
import com.alten.ambroise.forum.data.model.beans.Document;

import java.io.IOException;


@Database(entities = {Document.class}, version = 1, exportSchema = false)
public abstract class DocumentRoomDatabase extends RoomDatabase {
    private static volatile DocumentRoomDatabase INSTANCE;
    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    public static DocumentRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DocumentRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DocumentRoomDatabase.class, "document_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract DocumentDao documentRepository();

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final DocumentDao mDao;

        PopulateDbAsync(DocumentRoomDatabase db) {
            mDao = db.documentRepository();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            final AssetManager assets = App.getContext().getAssets();
            try {
                final String[] documents = assets.list("documents");
                for (final String document : documents) {
                    final Document doc = new Document();
                    doc.setTitle(document);
                    doc.setUri("file:///android_asset/documents/" + document);
                    mDao.insert(doc);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
