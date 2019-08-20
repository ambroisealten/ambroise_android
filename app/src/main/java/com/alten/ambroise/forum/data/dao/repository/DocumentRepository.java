package com.alten.ambroise.forum.data.dao.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.alten.ambroise.forum.data.dao.DocumentDao;
import com.alten.ambroise.forum.data.dao.roomDatabase.DocumentRoomDatabase;
import com.alten.ambroise.forum.data.model.beans.Document;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DocumentRepository {

    private final DocumentDao documentDao;
    private final LiveData<List<Document>> mAllDocuments;

    public DocumentRepository(Application application) {
        DocumentRoomDatabase db = DocumentRoomDatabase.getDatabase(application);
        documentDao = db.documentRepository();
        mAllDocuments = documentDao.getAllDocuments();
    }

    public LiveData<List<Document>> getAllDocuments() {
        return mAllDocuments;
    }


    public Long insert(Document document) {
        try {
            return new insertAsyncTask(documentDao).execute(document).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Document... documents) {
        new DocumentRepository.updateAsyncTask(documentDao).execute(documents);
    }

    public Document getDocument(final Long id) {
        try {
            return new getAsyncTask(documentDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(Document document) {
        new deleteAsyncTask(documentDao).execute(document);
    }

    private static class deleteAsyncTask extends AsyncTask<Document, Void, Void> {

        private final DocumentDao mAsyncTaskDao;

        deleteAsyncTask(DocumentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Document... params) {
            mAsyncTaskDao.deleteDocumentsForum(params);
            return null;
        }
    }

    private static class getAsyncTask extends AsyncTask<Long, Void, Document> {
        private final DocumentDao mAsyncTaskDao;

        getAsyncTask(DocumentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Document doInBackground(Long... id) {
            return mAsyncTaskDao.getDocument(id[0]);
        }
    }

    private static class insertAsyncTask extends AsyncTask<Document, Void, Long> {
        private final DocumentDao mAsyncTaskDao;

        insertAsyncTask(DocumentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Long doInBackground(final Document... params) {
            return mAsyncTaskDao.insert(params[0]);
        }
    }

    private static class updateAsyncTask extends AsyncTask<Document, Void, Void> {

        private final DocumentDao mAsyncTaskDao;

        updateAsyncTask(DocumentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Document... params) {
            mAsyncTaskDao.update(params);
            return null;
        }
    }
}

