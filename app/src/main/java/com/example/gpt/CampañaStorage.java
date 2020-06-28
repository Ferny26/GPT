package com.example.gpt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.gpt.GPTDbSchema.*;

public class CampañaStorage {

    private static CampañaStorage sCampañaStorage;
    private Context mContext;
    private SQLiteDatabase mDataBase;


    public static CampañaStorage get(Context context){
        if(sCampañaStorage == null){
            sCampañaStorage = new CampañaStorage(context);
        }
        return sCampañaStorage;
    }

    private CampañaStorage(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new GPTBaseHelper(mContext).getWritableDatabase();
    }


    public void addCampaña(Campaña c, Context context){
        ContentValues values= getContentValues(c);
        mDataBase.insert(CampañaTable.NAME, null, values);
    }

    public void updateCampaña(Campaña campaña){
        String uuidString = campaña.getmIdCampaña().toString();
        ContentValues values = getContentValues(campaña);
        mDataBase.update(CampañaTable.NAME, values, CampañaTable.Cols.UUID + "= ?", new String[] {uuidString});
    }

    List<Campaña> getmCampañas(){
        List <Campaña> campañas = new ArrayList<>();
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryCampaña(null,null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                //Envia los datos encontrados para que sean inicializados
                campañas.add(cursor.getCampaña());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return campañas;
    }

    public void deleteCampaña( String whereClause, String[] whereArgs){
        mDataBase.delete(
                CampañaTable.NAME,
                whereClause,
                whereArgs
        );
    }

    public Campaña getCampaña(UUID id){
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryCampaña(
                CampañaTable.Cols.UUID + " = ? ",
                new String[] {id.toString()}
        );

        try {
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            //Regresa el objeto con los datos de la campaña correspondiente
            return cursor.getCampaña();
        }
        finally {
            cursor.close();
        }
    }

    private CursorWrapper queryCampaña(String whereClause, String[] whereArgs){
        Cursor cursor = mDataBase.query(
                CampañaTable.NAME,
                null, // column - null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new GPTCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Campaña campaña){
        ContentValues values = new ContentValues();
        values.put(CampañaTable.Cols.UUID, campaña.getmIdCampaña().toString());
        values.put(CampañaTable.Cols.NOMBRE, campaña.getmNombreCampaña());
        values.put(CampañaTable.Cols.FECHA, campaña.getmFechaCampaña().getTime());
        return values;
    }
}
