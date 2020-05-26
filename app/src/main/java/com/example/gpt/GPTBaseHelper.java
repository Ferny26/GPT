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

        db.execSQL("CREATE TABLE " +
                GatoHogarTable.NAME +
                "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                GatoHogarTable.Cols.FKUUID_GATO + ", " +
                GatoHogarTable.Cols.FKUUID_PERSONA + ", " +
                "FOREIGN KEY ("+ GatoHogarTable.Cols.FKUUID_GATO + ") REFERENCES "+ GatoTable.NAME+"("+GatoTable.Cols.UUID+")," +
                "FOREIGN KEY ("+ GatoHogarTable.Cols.FKUUID_PERSONA + ") REFERENCES "+ PersonaTable.NAME+"("+PersonaTable.Cols.UUID+")" +
                ")"
        );

        db.execSQL("CREATE TABLE " +
                PersonaTable.NAME +
                "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                PersonaTable.Cols.UUID + ", " +
                PersonaTable.Cols.NOMBRE + ", " +
                PersonaTable.Cols.APELLIDO_MATERNO + ", " +
                PersonaTable.Cols.APELLIDO_PATERNO + ", " +
                PersonaTable.Cols.CELULAR + ", " +
                PersonaTable.Cols.DOMICILIO + ", " +
                PersonaTable.Cols.EMAIL +
                ")"
        );

        db.execSQL("CREATE TABLE " +
                MaterialTable.NAME +
                "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                MaterialTable.Cols.UUID + ", " +
                MaterialTable.Cols.NOMBRE + ", " +
                MaterialTable.Cols.TIPO_INVENTARIO + ", " +
                MaterialTable.Cols.PRESENTACION + ", " +
                MaterialTable.Cols.CANTIDAD + ", " +
                MaterialTable.Cols.CATEGORIA + ", " +
                MaterialTable.Cols.FOTO +
                ")"
        );

        db.execSQL("CREATE TABLE " +
                MaterialCampañaTable.NAME +
                "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                MaterialCampañaTable.Cols.FKUUID_CAMPAÑA + ", " +
                MaterialCampañaTable.Cols.FKUUID_MATERIAL+ ", " +
                MaterialCampañaTable.Cols.CANTIDAD_GASTADA+ ", " +
                "FOREIGN KEY ("+ MaterialCampañaTable.Cols.FKUUID_CAMPAÑA + ") REFERENCES "+ CampañaTable.NAME+"("+CampañaTable.Cols.UUID+")," +
                "FOREIGN KEY ("+ MaterialCampañaTable.Cols.FKUUID_MATERIAL + ") REFERENCES "+ MaterialTable.NAME+"("+MaterialTable.Cols.UUID+")" +
                ")"
        );

        db.execSQL("CREATE TABLE " +
                EsterilizacionTable.NAME +
                "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                EsterilizacionTable.Cols.UUID + ", " +
                EsterilizacionTable.Cols.PRECIO + ", " +
                EsterilizacionTable.Cols.FAJA + ", " +
                EsterilizacionTable.Cols.ANTICIPO + ", " +
                EsterilizacionTable.Cols.PAGADO + ", " +
                EsterilizacionTable.Cols.FKUUID_CAMPAÑA + ", " +
                EsterilizacionTable.Cols.FKUUID_GATO + "," +
                "FOREIGN KEY ("+ EsterilizacionTable.Cols.FKUUID_CAMPAÑA + ") REFERENCES "+ CampañaTable.NAME+"("+CampañaTable.Cols.UUID+")," +
                "FOREIGN KEY ("+ EsterilizacionTable.Cols.FKUUID_GATO + ") REFERENCES "+ GatoTable.NAME+"("+GatoTable.Cols.UUID+")" +
                ")"
        );


    }
    @Override

    //Esta funcion no hace nada aun jaja10
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
