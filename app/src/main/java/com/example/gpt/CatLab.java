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


//Singleton para los gatos, para obtener tanto el directorio de la imagen de cada gato, así como obetener los gatos segun sea requerido por medio de una query
public class CatLab {
    private static CatLab sCatLab;
    private Context mContext;
    private SQLiteDatabase mDataBase;


    public static CatLab get(Context context){
        if(sCatLab == null){
            sCatLab = new CatLab(context);
        }
        return sCatLab;
    }

    private CatLab(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new GPTBaseHelper(mContext).getWritableDatabase();
    }


    public void addGato(Gato g, Context context){
        ContentValues values= getContentValues(g);
        mDataBase.insert(GPTDbSchema.GatoTable.NAME, null, values);
    }

    public void updateGato(Gato gato){
        String uuidString = gato.getmIdGato().toString();
        ContentValues values = getContentValues(gato);
        mDataBase.update(GPTDbSchema.GatoTable.NAME, values, GPTDbSchema.GatoTable.Cols.UUID + "= ?", new String[] {uuidString});
    }

    private CursorWrapper queryBusquedaGato(String query){
        Cursor cursor = mDataBase.rawQuery(query,null);
        return new GPTCursorWrapper(cursor);
    }

    List<Gato> getmBusquedaGatos(String query){
        List <Gato> gatos = new ArrayList<>();
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryBusquedaGato(query);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                //Envia los datos encontrados para que sean inicializados
                gatos.add(cursor.getGato());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return gatos;
    }


    public File getPhotoFile(Gato gato){
        //Crea un directorio con el nombre del id del gato para poder encontrarlo y no haya una coalicion de datos
        File filesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(filesDir, gato.getPhotoFilename());
    }


    public Gato getmGato(UUID id){
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryGato(
                GPTDbSchema.GatoTable.Cols.UUID + " = ? ",
                new String[] {id.toString()}
        );

        try {
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            //Regresa el objeto con los datos del gato
            return cursor.getGato();
        }
        finally {
            cursor.close();
        }
    }


    private CursorWrapper queryGato(String whereClause, String[] whereArgs){
        Cursor cursor = mDataBase.query(
                GPTDbSchema.GatoTable.NAME,
                null, // column - null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new GPTCursorWrapper(cursor);
    }


    private static ContentValues getContentValues(Gato gato){
        ContentValues values = new ContentValues();
        values.put(GPTDbSchema.GatoTable.Cols.UUID, gato.getmIdGato().toString());
        values.put(GPTDbSchema.GatoTable.Cols.PESO, gato.getmPeso());
        values.put(GPTDbSchema.GatoTable.Cols.FECHA_NACIMIENTO, gato.getmFechaNacimiento().getTime());
        values.put(GPTDbSchema.GatoTable.Cols.SEXO, gato.ismSexo());
        values.put(GPTDbSchema.GatoTable.Cols.NOMBRE, gato.getmNombreGato());
        values.put(GPTDbSchema.GatoTable.Cols.CONDICION_ESPECIAL, gato.getmCondicionEspecial());
        values.put(GPTDbSchema.GatoTable.Cols.PROCEDENCIA, gato.getmProcedencia());
        return values;
    }
}
