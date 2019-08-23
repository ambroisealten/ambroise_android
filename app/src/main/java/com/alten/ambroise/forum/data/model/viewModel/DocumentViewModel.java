package com.alten.ambroise.forum.data.model.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alten.ambroise.forum.data.dao.repository.DocumentRepository;
import com.alten.ambroise.forum.data.model.beans.Document;

import java.util.List;

public class DocumentViewModel extends AndroidViewModel {

    private final DocumentRepository mDocumentRepository;

    private final LiveData<List<Document>> mAllDocuments;

    public DocumentViewModel(Application application) {
        super(application);
        mDocumentRepository = new DocumentRepository(application);
        mAllDocuments = mDocumentRepository.getAllDocuments();
    }

    public LiveData<List<Document>> getAllDocuments() {
        return mAllDocuments;
    }

    public Long insert(Document document) {
        return mDocumentRepository.insert(document);
    }

    public void update(Document document) {
        mDocumentRepository.update(document);
    }

    public Document getDocument(final Long id) {
        return mDocumentRepository.getDocument(id);
    }

    public void delete(Document document) {
        mDocumentRepository.delete(document);
    }

}
