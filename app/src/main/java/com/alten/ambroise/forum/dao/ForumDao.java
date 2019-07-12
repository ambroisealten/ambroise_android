package com.alten.ambroise.forum.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.alten.ambroise.forum.beans.Forum;

import java.util.List;

@Dao
public interface ForumDao {

    @Insert
    void insert(Forum forum);

    @Query("DELETE FROM forum_table")
    void deleteAll();

    @Query("SELECT * from forum_table ORDER BY _id ASC")
    LiveData<List<Forum>> getAllForums();

}
