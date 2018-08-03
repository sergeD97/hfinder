package com.hfinder.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.hfinder.database.converter.DateConverter;

/**
 * Created by root on 02/06/18.
 */
@Database(entities = {Message.class, Info.class, Conversation.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class DataBase extends RoomDatabase {

    public static final String MESSAGE_STATUS_VUE = "vue";
    public static final String MESSAGE_STATUS_NON_VUE = "non_vue";
    public static final String MESSAGE_ME = "moi";
    private static DataBase dataBase;
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "hfdatabase";

    public static DataBase getInstance(Context context){
        if(dataBase == null){
            synchronized (LOCK){
                dataBase = Room.databaseBuilder(context.getApplicationContext(),
                        DataBase.class, DataBase.DATABASE_NAME)
                        .build();
            }

        }
        return dataBase;
    }

    public abstract MessageDAO messageDAO();
    public  abstract  InfoDAO infoDAO();
    public abstract  ConversationDAO conversationDAO();


}
