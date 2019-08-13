package com.alten.ambroise.forum.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.alten.ambroise.forum.data.model.beans.Forum;

import java.util.List;

@Dao
public interface ForumDao {

    @Insert
    void insert(Forum forum);

    @Query("DELETE FROM forum_table")
    void deleteAll();

    @Query("SELECT * from forum_table ORDER BY _id ASC")
    LiveData<List<Forum>> getAllForums();

    @Query("SELECT * from forum_table WHERE _id = :id")
    Forum getForum(Long id);

    @Update
    void update(Forum... forum);
}
