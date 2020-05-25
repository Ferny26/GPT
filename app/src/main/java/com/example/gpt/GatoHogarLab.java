package com.example.gpt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.util.UUID;

public class GatoHogarLab {
    private static GatoHogarLab sGatoHogarLab;
    private Context mContext;
    private SQLiteDatabase mDataBase;


    public static GatoHogarLab get(Context context){
        if(sGatoHogarLab == null){
            sGatoHogarLab = new GatoHogarLab(context);
        }
        return sGatoHogarLab;
    }

    private GatoHogarLab(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new GPTBaseHelper(mContext).getWritableDatabase();
    }


    public void addGatoHogar(GatoHogar gH, Context context){
        ContentValues values= getContentValues(gH);
        mDataBase.insert(GPTDbSchema.GatoHogarTable.NAME, null, values);
    }

    public void updateGatoHogar(GatoHogar gato){
        String uuidString = gato.getmGatoHogarId().toString();
        ContentValues values = getContentValues(gato);
        mDataBase.update(GPTDbSchema.GatoHogarTable.NAME, values, GPTDbSchema.GatoHogarTable.Cols.UUID + "= ?", new String[] {uuidString});
    }


    public void deleteGatoHogar( String whereClause, String[] whereArgs){
        mDataBase.delete(
                GPTDbSchema.GatoTable.NAME,
                whereClause,
                whereArgs
        );
    }

    public GatoHogar getmGatoHogar(UUID id){
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryGatoHogar(
                GPTDbSchema.GatoHogarTable.Cols.FKUUID_GATO + " = ? ",
                new String[] {id.toString()}
        );
        try {
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            //Regresa el objeto con los datos de la campaña correspondiente
            return cursor.getGatoHogar();
        }
        finally {
            cursor.close();
        }
    }

    private CursorWrapper queryGatoHogar(String whereClause, String[] whereArgs){
        Cursor cursor = mDataBase.query(
                GPTDbSchema.GatoHogarTable.NAME,
                null, // column - null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new GPTCursorWrapper(cursor);
    }


    private static ContentValues getContentValues(GatoHogar gato){
        ContentValues values = new ContentValues();
        values.put(GPTDbSchema.GatoHogarTable.Cols.UUID, gato.getmGatoHogarId().toString());
        values.put(GPTDbSchema.GatoHogarTable.Cols.FKUUID_GATO, gato.getmGatoId().toString());
        values.put(GPTDbSchema.GatoHogarTable.Cols.FKUUID_PERSONA, gato.getmPersonaId().toString());
        return values;
    }
}
