package com.hfinder.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by root on 28/05/18.
 */

@Entity(tableName = "info")
public class Info {
    @Ignore
    public static final String USER_NAME = "user-name";
    @Ignore
    public static final String USER_EMAIL = "user-email";
    @Ignore
    public static final String USER_PHONE = "user-phone";
    @Ignore
    public static final String USER_SURNAME = "user-surname";
    @Ignore
    public static final String USER_ID = "user-id";


    @NonNull
    @PrimaryKey
    private String name;
    private String values;

    @Ignore
    public Info(){
    }

    public Info(@NonNull String name, String values) {
        this.name = name;
        this.values = values;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }
}
