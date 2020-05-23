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

        db.execSQL("CREATE TABLE " +
                EsterilizacionesTable.NAME +
                "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                EsterilizacionesTable.Cols.UUID + ", " +
                EsterilizacionesTable.Cols.FECHA + ", " +
                EsterilizacionesTable.Cols.PRECIO + ", " +
                EsterilizacionesTable.Cols.FAJA + ", " +
                EsterilizacionesTable.Cols.UUID + ", " +
                EsterilizacionesTable.Cols.ANTICIPO + ", " +
                EsterilizacionesTable.Cols.FKUUID_CAMPAÑA + ", " +
                EsterilizacionesTable.Cols.FKUUID_GATO + ", " +
                "FOREIGN KEY("+EsterilizacionesTable.Cols.FKUUID_CAMPAÑA+") REFERENCES CampañaTable("+CampañaTable.Cols.UUID+")" +
                "FOREIGN KEY("+EsterilizacionesTable.Cols.FKUUID_GATO+")REFERENCES GatosTable("+GatosTable.Cols.UUID+")" +
                ")"
        );

        db.execSQL("CREATE TABLE " +
                GatosTable.NAME +
                "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                GatosTable.Cols.UUID + ", " +
                GatosTable.Cols.PESO + ", " +
                GatosTable.Cols.FOTO + ", " +
                GatosTable.Cols.FECHA_NACIMIENTO + ", " +
                GatosTable.Cols.NOMBRE + ", " +
                GatosTable.Cols.SEXO + ", " +
                GatosTable.Cols.CONDICION_ESPECIAL + ", " +
                GatosTable.Cols.PROCEDENCIA +
                ")"
        );

    }
    @Override

    //Esta funcion no hace nada aun jaja10
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
