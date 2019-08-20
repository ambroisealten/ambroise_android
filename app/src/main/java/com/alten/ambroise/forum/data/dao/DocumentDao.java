package com.alten.ambroise.forum.data.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.alten.ambroise.forum.data.model.beans.Document;

import java.util.List;

@Dao
public interface DocumentDao {

    @Insert
    long insert(Document document);

    @Update
    void update(Document... documents);

    @Delete
    void deleteDocumentsForum(Document... documents);

    @Query("DELETE FROM document_table")
    void deleteAll();

    @Query("SELECT * from document_table ORDER BY _id ASC")
    LiveData<List<Document>> getAllDocuments();

    @Query("SELECT * FROM document_table WHERE _id = :id ")
    Document getDocument(Long id);

}
