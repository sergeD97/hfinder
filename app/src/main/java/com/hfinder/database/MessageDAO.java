package com.hfinder.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by root on 02/06/18.
 */
@Dao
public interface MessageDAO {

    @Query("SELECT * FROM message")
    LiveData<List<Message>> findAll();

    @Delete
    void delete(Message message);

    @Insert
    void insert(Message message);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Message message);

    @Query("SELECT * FROM message WHERE interlocuteur = :interlocuteur ORDER BY date DESC")
    LiveData<List<Message>> findByInterlocuteur(String interlocuteur);

}
