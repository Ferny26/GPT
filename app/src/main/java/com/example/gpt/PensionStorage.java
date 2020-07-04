package com.example.gpt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//Singleton para las pensiones
public class PensionStorage {

    private static PensionStorage sPensionStorage;
    private Context mContext;
    private SQLiteDatabase mDataBase;


    public static PensionStorage get(Context context){
        if(sPensionStorage == null){
            sPensionStorage = new PensionStorage(context);
        }
        return sPensionStorage;
    }

    private PensionStorage(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new GPTBaseHelper(mContext).getWritableDatabase();
    }


    public void addPension(Pension p){
        ContentValues values= getContentValues(p);
        mDataBase.insert(GPTDbSchema.PensionTable.NAME, null, values);
    }

    List<Pension> getmPensiones(){
        List <Pension> pensiones = new ArrayList<>();
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryPension(null,null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                //Envia los datos encontrados para que sean inicializados
                pensiones.add(cursor.getPension());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return pensiones;
    }

    public void updatePension(Pension pension){
        String uuidString = pension.getmIdPension().toString();
        ContentValues values = getContentValues(pension);
        mDataBase.update(GPTDbSchema.PensionTable.NAME, values, GPTDbSchema.PensionTable.Cols.UUID + "= ?", new String[] {uuidString});
    }


    public void deletePension( String whereClause, String[] whereArgs){
        mDataBase.delete(
                GPTDbSchema.PensionTable.NAME,
                whereClause,
                whereArgs
        );
    }

    public Pension getmPension(UUID id){
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryPension(
                GPTDbSchema.PensionTable.Cols.UUID + " = ? ",
                new String[] {id.toString()}
        );
        try {
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            //Regresa el objeto con los datos de la campaña correspondiente
            return cursor.getPension();
        }
        finally {
            cursor.close();
        }
    }

    private CursorWrapper queryPension(String whereClause, String[] whereArgs){
        Cursor cursor = mDataBase.query(
                GPTDbSchema.PensionTable.NAME,
                null, // column - null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new GPTCursorWrapper(cursor);
    }


    private static ContentValues getContentValues(Pension pension){
        ContentValues values = new ContentValues();
        values.put(GPTDbSchema.PensionTable.Cols.UUID, pension.getmIdPension().toString());
        values.put(GPTDbSchema.PensionTable.Cols.FKUUID_GATO, pension.getmGatoId().toString());
        values.put(GPTDbSchema.PensionTable.Cols.PRECIO_DIA, pension.getmPrecioDia());
        values.put(GPTDbSchema.PensionTable.Cols.FECHA_INGRESO, pension.getmFechaIngreso().getTime());
        values.put(GPTDbSchema.PensionTable.Cols.FECHA_SALIDA, pension.getmFechaSalida().getTime());
        values.put(GPTDbSchema.PensionTable.Cols.PAGADA, pension.ismPagada() ? 1 : 0);
        values.put(GPTDbSchema.PensionTable.Cols.TIPO, pension.getmTipoPension());

        return values;
    }
}
