package com.example.gpt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EsterilizacionStorage {
    private static EsterilizacionStorage sEsterilizacionStorage;
    private Context mContext;
    private SQLiteDatabase mDataBase;


    public static EsterilizacionStorage get(Context context){
        if(sEsterilizacionStorage == null){
            sEsterilizacionStorage = new EsterilizacionStorage(context);
        }
        return sEsterilizacionStorage;
    }

    private EsterilizacionStorage(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new GPTBaseHelper(mContext).getWritableDatabase();
    }


    public void addEsterilizacion(Esterilizacion e, Context context){
        ContentValues values= getContentValues(e);
        mDataBase.insert(GPTDbSchema.EsterilizacionTable.NAME, null, values);
    }

    public void updateEsterilizacion(Esterilizacion esterilizacion){
        String uuidString = esterilizacion.getmIdEsterilizacion().toString();
        ContentValues values = getContentValues(esterilizacion);
        mDataBase.update(GPTDbSchema.EsterilizacionTable.NAME, values, GPTDbSchema.EsterilizacionTable.Cols.UUID + "= ?", new String[] {uuidString});
    }

    List<Esterilizacion> getmEsterilizaciones(UUID campañaId){
        List <Esterilizacion> esterilizaciones = new ArrayList<>();
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryEsterilizacion(GPTDbSchema.EsterilizacionTable.Cols.FKUUID_CAMPAÑA + " = ? ",
                new String[] {campañaId.toString()});
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                //Envia los datos encontrados para que sean inicializados
                esterilizaciones.add(cursor.getEsterilizacion());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return esterilizaciones;
    }

    public void deleteEsterilizaciones( String whereClause, String[] whereArgs){
        mDataBase.delete(
                GPTDbSchema.EsterilizacionTable.NAME,
                whereClause,
                whereArgs
        );
    }

    public Esterilizacion getEsterilizacion(UUID id){
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryEsterilizacion(
                GPTDbSchema.EsterilizacionTable.Cols.UUID + " = ? ",
                new String[] {id.toString()}
        );

        try {
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            //Regresa el objeto con los datos de la campaña correspondiente
            return cursor.getEsterilizacion();
        }
        finally {
            cursor.close();
        }
    }

    private CursorWrapper queryEsterilizacion(String whereClause, String[] whereArgs){
        Cursor cursor = mDataBase.query(
                GPTDbSchema.EsterilizacionTable.NAME,
                null, // column - null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new GPTCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Esterilizacion esterilizacion){
        ContentValues values = new ContentValues();
        values.put(GPTDbSchema.EsterilizacionTable.Cols.UUID, esterilizacion.getmIdEsterilizacion().toString());
        values.put(GPTDbSchema.EsterilizacionTable.Cols.FKUUID_CAMPAÑA, esterilizacion.getmIdCampaña().toString());
        values.put(GPTDbSchema.EsterilizacionTable.Cols.FKUUID_GATO, esterilizacion.getmIdGato().toString());
        values.put(GPTDbSchema.EsterilizacionTable.Cols.PRECIO, esterilizacion.getmPrecio());
        values.put(GPTDbSchema.EsterilizacionTable.Cols.ANTICIPO, esterilizacion.getmAnticipo());
        values.put(GPTDbSchema.EsterilizacionTable.Cols.FAJA, esterilizacion.ismFaja() ? 1 : 0);
        return values;
    }

}
