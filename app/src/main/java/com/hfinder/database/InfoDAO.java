package com.hfinder.database;


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
public interface InfoDAO {

    @Query("SELECT * FROM info")
    List<Info> findAll();

    @Query("SELECT * FROM info WHERE name = :name")
    Info find(String name);

    @Delete
    void delete(Info info);

    @Query("DELETE FROM info")
    void deleteAll();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Info info);

    @Insert
    void insert(Info info);
}
