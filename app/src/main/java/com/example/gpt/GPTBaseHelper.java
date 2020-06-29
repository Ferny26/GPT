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
                CampañaTable.Cols.UUID + " PRIMARY KEY, " +
                CampañaTable.Cols.NOMBRE + ", " +
                CampañaTable.Cols.FECHA +
                ")"
        );

        db.execSQL("CREATE TABLE " +
                GatoTable.NAME +
                "(" +
                GatoTable.Cols.UUID + " PRIMARY KEY, " +
                GatoTable.Cols.PESO + ", " +
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
                PersonaTable.Cols.UUID + " PRIMARY KEY, " +
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
                MaterialTable.Cols.UUID + " PRIMARY KEY, " +
                MaterialTable.Cols.NOMBRE + ", " +
                MaterialTable.Cols.TIPO_INVENTARIO + ", " +
                MaterialTable.Cols.PRESENTACION + ", " +
                MaterialTable.Cols.CANTIDAD + ", " +
                MaterialTable.Cols.CATEGORIA +
                ")"
        );

        db.execSQL("CREATE TABLE " +
                MaterialCampañaTable.NAME +
                "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MaterialCampañaTable.Cols.FKUUID_CAMPAÑA + ", " +
                MaterialCampañaTable.Cols.FKUUID_MATERIAL+ ", " +
                MaterialCampañaTable.Cols.CANTIDAD_GASTADA+ ", " +
                "FOREIGN KEY ("+ MaterialCampañaTable.Cols.FKUUID_CAMPAÑA + ") REFERENCES "+ CampañaTable.NAME+"("+CampañaTable.Cols.UUID+") ON DELETE CASCADE," +
                "FOREIGN KEY ("+ MaterialCampañaTable.Cols.FKUUID_MATERIAL + ") REFERENCES "+ MaterialTable.NAME+"("+MaterialTable.Cols.UUID+") ON DELETE CASCADE" +
                ")"
        );

        db.execSQL("CREATE TABLE " +
                EsterilizacionTable.NAME +
                "(" +
                EsterilizacionTable.Cols.UUID + " PRIMARY KEY, " +
                EsterilizacionTable.Cols.PRECIO + ", " +
                EsterilizacionTable.Cols.COSTO_EXTRA + ", " +
                EsterilizacionTable.Cols.FAJA + ", " +
                EsterilizacionTable.Cols.ANTICIPO + ", " +
                EsterilizacionTable.Cols.PAGADO + ", " +
                EsterilizacionTable.Cols.FKUUID_CAMPAÑA + "," +
                EsterilizacionTable.Cols.FKUUID_GATO + "," +
                "FOREIGN KEY ("+ EsterilizacionTable.Cols.FKUUID_CAMPAÑA + ") REFERENCES "+ CampañaTable.NAME+"("+CampañaTable.Cols.UUID+") ON DELETE CASCADE, " +
                "FOREIGN KEY ("+ EsterilizacionTable.Cols.FKUUID_GATO + ") REFERENCES "+ GatoTable.NAME+"("+GatoTable.Cols.UUID+") ON DELETE CASCADE" +
                ")"
        );

        db.execSQL("CREATE TABLE " +
                IngresoTable.NAME +
                "(" +
                IngresoTable.Cols.UUID + " PRIMARY KEY, " +
                IngresoTable.Cols.CANTIDAD + ", " +
                IngresoTable.Cols.MOTIVO + ", " +
                IngresoTable.Cols.FECHA + ", " +
                IngresoTable.Cols.AUTOMATICO +
                ")"
        );

        db.execSQL("CREATE TABLE " +
                GastoTable.NAME +
                "(" +
                GastoTable.Cols.UUID + " PRIMARY KEY, " +
                GastoTable.Cols.CANTIDAD + ", " +
                GastoTable.Cols.MOTIVO + ", " +
                GastoTable.Cols.FECHA + ", " +
                GastoTable.Cols.AUTOMATICO +
                ")"
        );

        db.execSQL("CREATE TABLE " +
                AdopcionTable.NAME +
                "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                AdopcionTable.Cols.FKUUID_GATO + ", " +
                "FOREIGN KEY ("+ AdopcionTable.Cols.FKUUID_GATO + ") REFERENCES "+ GatoTable.NAME+"("+GatoTable.Cols.UUID+")" +
                ")"
        );

        db.execSQL("CREATE TABLE " +
                RegistroAdopcionTable.NAME +
                "(" +
                RegistroAdopcionTable.Cols.UUID + " PRIMARY KEY, " +
                RegistroAdopcionTable.Cols.FKUUID_GATO + ", " +
                RegistroAdopcionTable.Cols.FKUUID_ADOPTANTE + ", " +
                "FOREIGN KEY ("+ RegistroAdopcionTable.Cols.FKUUID_GATO + ") REFERENCES "+ GatoTable.NAME+"("+GatoTable.Cols.UUID+")," +
                "FOREIGN KEY ("+ RegistroAdopcionTable.Cols.FKUUID_ADOPTANTE + ") REFERENCES "+ PersonaTable.NAME+"("+PersonaTable.Cols.UUID+")" +
                ")"
        );

        db.execSQL("CREATE TABLE " +
                PensionTable.NAME +
                "(" +
                PensionTable.Cols.UUID + "  PRIMARY KEY, " +
                PensionTable.Cols.FKUUID_GATO + ", " +
                PensionTable.Cols.FECHA_INGRESO + ", " +
                PensionTable.Cols.FECHA_SALIDA + ", " +
                PensionTable.Cols.TIPO + ", " +
                PensionTable.Cols.PRECIO_DIA + ", " +
                "FOREIGN KEY ("+ PensionTable.Cols.FKUUID_GATO + ") REFERENCES "+ GatoTable.NAME+"("+GatoTable.Cols.UUID+")" +
                ")"
        );

        db.execSQL("CREATE TABLE " +
                CostoExtraTable.NAME +
                "(" +
                CostoExtraTable.Cols.UUID + "  PRIMARY KEY, " +
                CostoExtraTable.Cols.CANTIDAD + ", " +
                CostoExtraTable.Cols.DESCRIPCION + ", " +
                CostoExtraTable.Cols.FECHA + ", " +
                CostoExtraTable.Cols.FKUUID_PENSION + ", " +
                "FOREIGN KEY ("+ CostoExtraTable.Cols.FKUUID_PENSION + ") REFERENCES "+ PensionTable.NAME+"("+PensionTable.Cols.UUID+") ON DELETE CASCADE" +
                ")"
        );
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
