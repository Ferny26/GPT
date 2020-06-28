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

public class MaterialCampañaStorage {
    private static MaterialCampañaStorage sMaterialCampañaStorage;
    private Context mContext;
    private SQLiteDatabase mDataBase;


    public static MaterialCampañaStorage get(Context context){
        if(sMaterialCampañaStorage == null){
            sMaterialCampañaStorage = new MaterialCampañaStorage(context);
        }
        return sMaterialCampañaStorage;
    }

    private MaterialCampañaStorage(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new GPTBaseHelper(mContext).getWritableDatabase();
    }


    public void addMaterial(MaterialCampaña m, Context context){
        ContentValues values= getContentValues(m);
        mDataBase.insert(GPTDbSchema.MaterialCampañaTable.NAME, null, values);
    }

    public File getPhotoFile(Material material){
        File filesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(filesDir, material.getmPhotoFile());
    }

    List<MaterialCampaña> getmMateriales(){
        List <MaterialCampaña> materiales = new ArrayList<>();
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryMaterial(null, null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                //Envia los datos encontrados para que sean inicializados
                materiales.add(cursor.getMaterialCampaña());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return materiales;
    }

    public void updateMaterial(MaterialCampaña material){
        String uuidString = material.getmMaterialId().toString();
        ContentValues values = getContentValues(material);
        mDataBase.update(GPTDbSchema.MaterialCampañaTable.NAME, values, GPTDbSchema.MaterialCampañaTable.Cols.FKUUID_MATERIAL + "= ?", new String[] {uuidString});
    }

    private CursorWrapper queryBusquedaMaterialCampaña(String query){
        Cursor cursor = mDataBase.rawQuery(query,null);
        return new GPTCursorWrapper(cursor);
    }

    List<MaterialCampaña> getmBusquedaMaterialesCampaña(String query){
        List <MaterialCampaña> materiales = new ArrayList<>();
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryBusquedaMaterialCampaña(query);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                //Envia los datos encontrados para que sean inicializados
                materiales.add(cursor.getMaterialCampaña());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return materiales;
    }

    public void deleteMaterial( String query){
        mDataBase.execSQL(query);
    }

    public MaterialCampaña getmMaterial(UUID id){
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryMaterial(
                GPTDbSchema.MaterialCampañaTable.Cols.FKUUID_MATERIAL + " = ? ",
                new String[] {id.toString()}
        );
        try {
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            //Regresa el objeto con los datos de la campaña correspondiente
            return cursor.getMaterialCampaña();
        }
        finally {
            cursor.close();
        }
    }






    private CursorWrapper queryMaterial(String whereClause, String[] whereArgs){
        Cursor cursor = mDataBase.query(
                GPTDbSchema.MaterialCampañaTable.NAME,
                null, // column - null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new GPTCursorWrapper(cursor);
    }


    private static ContentValues getContentValues(MaterialCampaña material){
        ContentValues values = new ContentValues();
        values.put(GPTDbSchema.MaterialCampañaTable.Cols.FKUUID_MATERIAL, material.getmMaterialId().toString());
        values.put(GPTDbSchema.MaterialCampañaTable.Cols.CANTIDAD_GASTADA, material.getmCantidadGastada());
        values.put(GPTDbSchema.MaterialCampañaTable.Cols.FKUUID_CAMPAÑA, material.getmCampañaId().toString());
        return values;
    }
}
