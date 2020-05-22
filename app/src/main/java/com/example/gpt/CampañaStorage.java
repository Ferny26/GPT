package com.example.gpt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class CampañaStorage {

    private static CampañaStorage sCampañaStorage;
    private List<Campaña> mCampañas;
    private Context mContext;
    private SQLiteDatabase mDatabase;


    public static CampañaStorage get(Context context){
        if(sCampañaStorage == null){
            sCampañaStorage = new CampañaStorage(context);
        }
        return sCampañaStorage;
    }
    //Se inicializa una nueva lista de crimenes y la base de datos para poder ingresar datos y guardarlos
    private CampañaStorage(Context context) {
        mContext = context.getApplicationContext();
    }
}
