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

public class MaterialStorage {
    private static MaterialStorage sMaterialStorage;
    private Context mContext;
    private SQLiteDatabase mDataBase;


    public static MaterialStorage get(Context context){
        if(sMaterialStorage == null){
            sMaterialStorage = new MaterialStorage(context);
        }
        return sMaterialStorage;
    }

    private MaterialStorage(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new GPTBaseHelper(mContext).getWritableDatabase();
    }


    public void addMaterial(Material m, Context context){
        ContentValues values= getContentValues(m);
        mDataBase.insert(GPTDbSchema.MaterialTable.NAME, null, values);
    }

    public File getPhotoFile(Material material){
        File filesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(filesDir, material.getmPhotoFile());
    }

    private CursorWrapper queryBusquedaMaterial(String query){
        Cursor cursor = mDataBase.rawQuery(query,null);
        return new GPTCursorWrapper(cursor);
    }

    List<Material> getmBusquedaMateriales(String query){
        List <Material> materiales = new ArrayList<>();
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryBusquedaMaterial(query);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                //Envia los datos encontrados para que sean inicializados
                materiales.add(cursor.getMaterial());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return materiales;
    }







    List<Material> getmMateriales(){
        List <Material> materiales = new ArrayList<>();
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryMaterial(null, null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                //Envia los datos encontrados para que sean inicializados
                materiales.add(cursor.getMaterial());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return materiales;
    }

    public void updateMaterial(Material material){
        String uuidString = material.getmMaterialId().toString();
        ContentValues values = getContentValues(material);
        mDataBase.update(GPTDbSchema.MaterialTable.NAME, values, GPTDbSchema.MaterialTable.Cols.UUID + "= ?", new String[] {uuidString});
    }


    public void deleteMaterial( String whereClause, String[] whereArgs){
        mDataBase.delete(
                GPTDbSchema.MaterialTable.NAME,
                whereClause,
                whereArgs
        );
    }

    public Material getmMaterial(UUID id){
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryMaterial(
                GPTDbSchema.MaterialTable.Cols.UUID + " = ? ",
                new String[] {id.toString()}
        );
        try {
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            //Regresa el objeto con los datos de la campaña correspondiente
            return cursor.getMaterial();
        }
        finally {
            cursor.close();
        }
    }

    private CursorWrapper queryMaterial(String whereClause, String[] whereArgs){
        Cursor cursor = mDataBase.query(
                GPTDbSchema.MaterialTable.NAME,
                null, // column - null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new GPTCursorWrapper(cursor);
    }


    private static ContentValues getContentValues(Material material){
        ContentValues values = new ContentValues();
        values.put(GPTDbSchema.MaterialTable.Cols.UUID, material.getmMaterialId().toString());
        values.put(GPTDbSchema.MaterialTable.Cols.NOMBRE, material.getmNombre());
        values.put(GPTDbSchema.MaterialTable.Cols.CANTIDAD, material.getmCantidad());
        values.put(GPTDbSchema.MaterialTable.Cols.CATEGORIA, material.getmCategoria());
        values.put(GPTDbSchema.MaterialTable.Cols.PRESENTACION, material.getmPresentacion());
        values.put(GPTDbSchema.MaterialTable.Cols.TIPO_INVENTARIO, material.getmTipoInventario());
        return values;
    }
}
