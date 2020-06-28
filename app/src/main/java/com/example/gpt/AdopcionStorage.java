package com.example.gpt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdopcionStorage {
    private static AdopcionStorage sAdopcionStorage;
    private Context mContext;
    private SQLiteDatabase mDataBase;


    public static AdopcionStorage get(Context context){
        if(sAdopcionStorage == null){
            sAdopcionStorage = new AdopcionStorage(context);
        }
        return sAdopcionStorage;
    }

    private AdopcionStorage(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new GPTBaseHelper(mContext).getWritableDatabase();
    }


    public void addAdopcion(Adopcion a, Context context){
        ContentValues values= getContentValues(a);
        mDataBase.insert(GPTDbSchema.AdopcionTable.NAME, null, values);
    }

    public void updateAdopcion(Adopcion adopcion){
        String uuidString = adopcion.getmAdopcionId().toString();
        ContentValues values = getContentValues(adopcion);
        mDataBase.update(GPTDbSchema.AdopcionTable.NAME, values, GPTDbSchema.AdopcionTable.Cols.FKUUID_GATO + "= ?", new String[] {uuidString});
    }


    List<Adopcion> getmAdopciones(String query){
        List <Adopcion> adopciones = new ArrayList<>();
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryAdopciones(query);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                //Envia los datos encontrados para que sean inicializados
                adopciones.add(cursor.getAdopcion());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return adopciones;
    }


    public void deleteAdopcion( String whereClause, String[] whereArgs){
        mDataBase.delete(
                GPTDbSchema.AdopcionTable.NAME,
                whereClause,
                whereArgs
        );
    }


    public Adopcion getmAdopcion(UUID id){
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryAdopcion(
                GPTDbSchema.AdopcionTable.Cols.FKUUID_GATO + " = ? ",
                new String[] {id.toString()}
        );

        try {
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            //Regresa el objeto con los datos de la campaña correspondiente
            return cursor.getAdopcion();
        }
        finally {
            cursor.close();
        }
    }

    private CursorWrapper queryAdopciones(String query){
        Cursor cursor = mDataBase.rawQuery(query, null);
        return new GPTCursorWrapper(cursor) {
        };
    }

    private CursorWrapper queryAdopcion(String whereClause, String[] whereArgs){
        Cursor cursor = mDataBase.query(
                GPTDbSchema.AdopcionTable.NAME,
                null, // column - null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new GPTCursorWrapper(cursor);
    }


    private static ContentValues getContentValues(Adopcion adopcion){
        ContentValues values = new ContentValues();
        values.put(GPTDbSchema.AdopcionTable.Cols.FKUUID_GATO, adopcion.getmAdopcionId().toString());
        return values;
    }
}
