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
                EsterilizacionTable.NAME +
                "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                EsterilizacionTable.Cols.UUID + ", " +
                EsterilizacionTable.Cols.FECHA + ", " +
                EsterilizacionTable.Cols.PRECIO + ", " +
                EsterilizacionTable.Cols.FAJA + ", " +
                EsterilizacionTable.Cols.UUID + ", " +
                EsterilizacionTable.Cols.ANTICIPO + ", " +
                EsterilizacionTable.Cols.PAGADO + ", " +
                EsterilizacionTable.Cols.FKUUID_CAMPAÑA + ", " +
                EsterilizacionTable.Cols.FKUUID_GATO + ", " +
                "FOREIGN KEY("+ EsterilizacionTable.Cols.FKUUID_CAMPAÑA+") REFERENCES CampañaTable("+CampañaTable.Cols.UUID+")" +
                "FOREIGN KEY("+ EsterilizacionTable.Cols.FKUUID_GATO+")REFERENCES GatosTable("+ GatoTable.Cols.UUID+")" +
                ")"
        );

        db.execSQL("CREATE TABLE " +
                GatoTable.NAME +
                "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                GatoTable.Cols.UUID + ", " +
                GatoTable.Cols.PESO + ", " +
                GatoTable.Cols.FOTO + ", " +
                GatoTable.Cols.FECHA_NACIMIENTO + ", " +
                GatoTable.Cols.NOMBRE + ", " +
                GatoTable.Cols.SEXO + ", " +
                GatoTable.Cols.CONDICION_ESPECIAL + ", " +
                GatoTable.Cols.PROCEDENCIA +
                ")"
        );

    }
    @Override

    //Esta funcion no hace nada aun jaja10
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
