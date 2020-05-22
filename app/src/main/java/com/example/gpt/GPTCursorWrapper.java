package com.example.gpt;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

public class GPTCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public GPTCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Campaña getCampaña (){
        String uuidString = getString(getColumnIndex(GPTDbSchema.CampañaTable.Cols.UUID));
        String nombre = getString(getColumnIndex(GPTDbSchema.CampañaTable.Cols.NOMBRE));
        long date = getLong(getColumnIndex(GPTDbSchema.CampañaTable.Cols.FECHA));

        Campaña campaña = new Campaña(UUID.fromString(uuidString));
        campaña.setmNombreCampaña(nombre);
        campaña.setmFechaCampaña(new Date(date));
        return campaña;
    }
}
