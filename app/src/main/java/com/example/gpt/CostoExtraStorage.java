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

public class CostoExtraStorage {
    private static CostoExtraStorage sCostoExtraStorage;
    private Context mContext;
    private SQLiteDatabase mDataBase;

    public static CostoExtraStorage get(Context context){
        if(sCostoExtraStorage == null){
            sCostoExtraStorage = new CostoExtraStorage(context);
        }
        return sCostoExtraStorage;
    }

    private CostoExtraStorage(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new GPTBaseHelper(mContext).getWritableDatabase();
    }


    public void addCostoExtra(CostoExtra c, Context context){
        ContentValues values= getContentValues(c);
        mDataBase.insert(GPTDbSchema.CostoExtraTable.NAME, null, values);
    }

    public void updateCostoExtra(CostoExtra costoExtra){
        String uuidString = costoExtra.getmCostoExtraId().toString();
        ContentValues values = getContentValues(costoExtra);
        mDataBase.update(GPTDbSchema.CostoExtraTable.NAME, values, GPTDbSchema.CostoExtraTable.Cols.UUID + "= ?", new String[] {uuidString});
    }


    List<CostoExtra> getmCostosExtra(UUID id){
        List <CostoExtra> costos = new ArrayList<>();
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryCostoExtra(GPTDbSchema.CostoExtraTable.Cols.FKUUID_PENSION + " = ? ",
                new String[] {id.toString()});
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                //Envia los datos encontrados para que sean inicializados
                costos.add(cursor.getCostoExtra());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return costos;
    }
    public File getPhotoFile(CostoExtra costoExtra){
        File filesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(filesDir, costoExtra.getPhotoFilename());
    }

    public int getmPrecioTotal (String query){
        Cursor cursor = mDataBase.rawQuery(query,null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        return count;
    }

    public CostoExtra getmCostoExtra(UUID id){
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryCostoExtra(
                GPTDbSchema.CostoExtraTable.Cols.UUID + " = ? ",
                new String[] {id.toString()}
        );

        try {
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            //Regresa el objeto con los datos de la campaña correspondiente
            return cursor.getCostoExtra();
        }
        finally {
            cursor.close();
        }
    }

    public void deleteCostoExtra( String whereClause, String[] whereArgs){
        mDataBase.delete(
                GPTDbSchema.CostoExtraTable.NAME,
                whereClause,
                whereArgs
        );
    }


    private CursorWrapper queryCostoExtra(String whereClause, String[] whereArgs){
        Cursor cursor = mDataBase.query(
                GPTDbSchema.CostoExtraTable.NAME,
                null, // column - null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new GPTCursorWrapper(cursor);
    }


    private static ContentValues getContentValues(CostoExtra costoExtra){
        ContentValues values = new ContentValues();
        values.put(GPTDbSchema.CostoExtraTable.Cols.UUID, costoExtra.getmCostoExtraId().toString());
        values.put(GPTDbSchema.CostoExtraTable.Cols.FKUUID_PENSION, costoExtra.getmPensionId().toString());
        values.put(GPTDbSchema.CostoExtraTable.Cols.FECHA, costoExtra.getmFechaActual().getTime());
        values.put(GPTDbSchema.CostoExtraTable.Cols.DESCRIPCION, costoExtra.getmDescripcion());
        values.put(GPTDbSchema.CostoExtraTable.Cols.CANTIDAD, costoExtra.getmCantidad());
        return values;
    }
}
