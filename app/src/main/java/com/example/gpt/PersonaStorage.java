package com.example.gpt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonaStorage {
    private static PersonaStorage sPersonaStorage;
    private Context mContext;
    private SQLiteDatabase mDataBase;


    public static PersonaStorage get(Context context){
        if(sPersonaStorage == null){
            sPersonaStorage = new PersonaStorage(context);
        }
        return sPersonaStorage;
    }

    private PersonaStorage(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new GPTBaseHelper(mContext).getWritableDatabase();
    }


    public void addPersona(Persona p){
        ContentValues values= getContentValues(p);
        mDataBase.insert(GPTDbSchema.PersonaTable.NAME, null, values);
    }

    List<Persona> getmPersonas(){
        List <Persona> personas = new ArrayList<>();
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryPersona(null,null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                //Envia los datos encontrados para que sean inicializados
                personas.add(cursor.getPersona());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return personas;
    }

    public void updatePersona(Persona persona){
        String uuidString = persona.getmIdPersona().toString();
        ContentValues values = getContentValues(persona);
        mDataBase.update(GPTDbSchema.PersonaTable.NAME, values, GPTDbSchema.PersonaTable.Cols.UUID + "= ?", new String[] {uuidString});
    }


    public void deletePersona( String whereClause, String[] whereArgs){
        mDataBase.delete(
                GPTDbSchema.PersonaTable.NAME,
                whereClause,
                whereArgs
        );
    }

    public Persona getmPersona(UUID id){
        GPTCursorWrapper cursor = (GPTCursorWrapper) queryPersona(
                GPTDbSchema.PersonaTable.Cols.UUID + " = ? ",
                new String[] {id.toString()}
        );
        try {
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            //Regresa el objeto con los datos de la campaña correspondiente
            return cursor.getPersona();
        }
        finally {
            cursor.close();
        }
    }

    private CursorWrapper queryPersona(String whereClause, String[] whereArgs){
        Cursor cursor = mDataBase.query(
                GPTDbSchema.PersonaTable.NAME,
                null, // column - null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new GPTCursorWrapper(cursor);
    }


    private static ContentValues getContentValues(Persona persona){
        ContentValues values = new ContentValues();
        values.put(GPTDbSchema.PersonaTable.Cols.UUID, persona.getmIdPersona().toString());
        values.put(GPTDbSchema.PersonaTable.Cols.NOMBRE, persona.getmNombre());
        values.put(GPTDbSchema.PersonaTable.Cols.APELLIDO_MATERNO, persona.getmApellidoMaterno());
        values.put(GPTDbSchema.PersonaTable.Cols.APELLIDO_PATERNO, persona.getmApellidoPaterno());
        values.put(GPTDbSchema.PersonaTable.Cols.DOMICILIO, persona.getmDomicilio());
        values.put(GPTDbSchema.PersonaTable.Cols.CELULAR, persona.getmCelular());
        values.put(GPTDbSchema.PersonaTable.Cols.EMAIL, persona.getmEmail());
        return values;
    }
}
