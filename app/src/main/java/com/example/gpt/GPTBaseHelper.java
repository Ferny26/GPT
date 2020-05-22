package com.example.gpt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.gpt.GPTDbSchema.*;

public class GPTBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION= 1;
    private static final String DATABASE_NAME = "gptBase.db";

    public GPTBaseHelper(Context context){
        super (context, DATABASE_NAME,null,VERSION);
    }


    @Override
    //Creacion de la tabla de la BD para almacenar los crimenes
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +
                CampañaTable.NAME +
                "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                CampañaTable.Cols.UUID + ", " +
                CampañaTable.Cols.NOMBRE + ", " +
                CampañaTable.Cols.FECHA +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
