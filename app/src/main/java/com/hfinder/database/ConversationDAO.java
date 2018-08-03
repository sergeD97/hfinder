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
public interface ConversationDAO {

    @Query("SELECT * FROM conversation")
    LiveData<List<Conversation>> findAll();

    @Insert
    void insert(Conversation conversation);

    @Delete
    void delete(Conversation conversation);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Conversation conversation);

    @Query("SELECT * FROM conversation WHERE interlocuteur = :inter")
    LiveData<Conversation> find(String inter);

}
