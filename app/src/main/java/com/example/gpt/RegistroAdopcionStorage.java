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

public class RegistroAdopcionStorage {
    private static RegistroAdopcionStorage sRegistroAdopcionStorage;
    private Context mContext;
    private SQLiteDatabase mDataBase;


    public static RegistroAdopcionStorage get(Context context){
        if(sRegistroAdopcionStorage == null){
            sRegistroAdopcionStorage = new  RegistroAdopcionStorage(context);
        }
        return sRegistroAdopcionStorage;
    }

    private  RegistroAdopcionStorage(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new GPTBaseHelper(mContext).getWritableDatabase();
    }


    public void addRegistroAdopcion(RegistroAdopcion a, Context context){
        ContentValues values= getContentValues(a);
        mDataBase.insert(GPTDbSchema. RegistroAdopcionTable.NAME, null, values);
    }

    public void updateRegistroAdopcion( RegistroAdopcion registroAdopcion){
        String uuidString = registroAdopcion.getmRegistroAdopcionId().toString();
        ContentValues values = getContentValues(registroAdopcion);
        mDataBase.update(GPTDbSchema. RegistroAdopcionTable.NAME, values, GPTDbSchema. RegistroAdopcionTable.Cols.UUID + "= ?", new String[] {uuidString});
    }


    List<RegistroAdopcion> getmRegistroAdopciones(){
        List <RegistroAdopcion> registroAdopciones = new ArrayList<>();
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryRegistroAdopcion(null,null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                //Envia los datos encontrados para que sean inicializados
                registroAdopciones.add(cursor.getRegistroAdopcion());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return registroAdopciones;
    }

    public File getPhotoFile(RegistroAdopcion registroAdopcion){
        File filesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(filesDir, registroAdopcion.getPhotoFilename());
    }
    public void deleteRegistroAdopcion( String whereClause, String[] whereArgs){
        mDataBase.delete(
                GPTDbSchema.RegistroAdopcionTable.NAME,
                whereClause,
                whereArgs
        );
    }


    public  RegistroAdopcion getmRegistroAdopcion(UUID id){
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryRegistroAdopcion(
                GPTDbSchema.RegistroAdopcionTable.Cols.UUID + " = ? ",
                new String[] {id.toString()}
        );

        try {
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            //Regresa el objeto con los datos de la campaña correspondiente
            return cursor.getRegistroAdopcion();
        }
        finally {
            cursor.close();
        }
    }


    private CursorWrapper queryRegistroAdopcion(String whereClause, String[] whereArgs){
        Cursor cursor = mDataBase.query(
                GPTDbSchema.RegistroAdopcionTable.NAME,
                null, // column - null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new GPTCursorWrapper(cursor);
    }


    private static ContentValues getContentValues(RegistroAdopcion registroAdopcion){
        ContentValues values = new ContentValues();
        values.put(GPTDbSchema.RegistroAdopcionTable.Cols.UUID, registroAdopcion.getmRegistroAdopcionId().toString());
        values.put(GPTDbSchema.RegistroAdopcionTable.Cols.ESTATUS, registroAdopcion.getmEstatus());
        values.put(GPTDbSchema.RegistroAdopcionTable.Cols.FKUUID_GATO, registroAdopcion.getmGatoId().toString());
        values.put(GPTDbSchema.RegistroAdopcionTable.Cols.FKUUID_ADOPTANTE, registroAdopcion.getmAdoptanteId().toString());
        return values;
    }
}
