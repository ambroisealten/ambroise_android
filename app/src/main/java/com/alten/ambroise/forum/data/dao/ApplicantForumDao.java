package com.alten.ambroise.forum.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.alten.ambroise.forum.data.model.beans.ApplicantForum;

import java.util.List;

@Dao
public interface ApplicantForumDao {

    @Insert
    void insert(ApplicantForum applicant);

    @Update
    void update(ApplicantForum... applicants);

    @Delete
    void deleteApplicantsForum(ApplicantForum... applicants);

    @Query("DELETE FROM applicantForum_table")
    void deleteAll();

    @Query("SELECT * from applicantForum_table ORDER BY _id ASC")
    LiveData<List<ApplicantForum>> getAllApplicants();
}
