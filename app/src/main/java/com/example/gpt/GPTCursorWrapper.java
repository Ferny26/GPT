package com.example.gpt;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.sql.Blob;
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
    public Gato getGato (){
        String uuidString = getString(getColumnIndex(GPTDbSchema.GatoTable.Cols.UUID));
        //int edad = getInt(getColumnIndex(GPTDbSchema.GatoTable.Cols.EDAD));
        //Blob foto = getBlob(getColumnIndex(GPTDbSchema.GatoTable.Cols.FOTO));
        float peso = getFloat(getColumnIndex(GPTDbSchema.GatoTable.Cols.PESO));
        String sexo = getString(getColumnIndex(GPTDbSchema.GatoTable.Cols.SEXO));
        String nombre = getString(getColumnIndex(GPTDbSchema.GatoTable.Cols.NOMBRE));
        String condicion_especial = getString(getColumnIndex(GPTDbSchema.GatoTable.Cols.CONDICION_ESPECIAL));
        String procedencia = getString(getColumnIndex(GPTDbSchema.GatoTable.Cols.PROCEDENCIA));

        Gato gato = new Gato(UUID.fromString(uuidString));
        //gato.setmFechaNacimiento(new Date(fecha_nacimiento));
        gato.setmPeso(peso);
        gato.setmSexo(sexo);
        gato.setmNombreGato(nombre);
        gato.setmCondicionEspecial(condicion_especial);
        gato.setmProcedencia(procedencia);
        return gato;
    }
    public Esterilizacion getEsterilizacion (){
        String uuidString = getString(getColumnIndex(GPTDbSchema.EsterilizacionTable.Cols.UUID));
        String uuidStringCamp = getString(getColumnIndex(GPTDbSchema.EsterilizacionTable.Cols.FKUUID_CAMPAÑA));
        String uuidStringGato = getString(getColumnIndex(GPTDbSchema.EsterilizacionTable.Cols.FKUUID_GATO));
        int precio = getInt(getColumnIndex(GPTDbSchema.EsterilizacionTable.Cols.PRECIO));
        int anticipo = getInt(getColumnIndex(GPTDbSchema.EsterilizacionTable.Cols.ANTICIPO));
        int pagado = getInt(getColumnIndex(GPTDbSchema.EsterilizacionTable.Cols.PAGADO));
        int faja = getInt(getColumnIndex(GPTDbSchema.EsterilizacionTable.Cols.FAJA));

        Esterilizacion esterilizacion = new Esterilizacion(UUID.fromString(uuidString));
        esterilizacion.setmPrecio(precio);
        esterilizacion.setmAnticipo(anticipo);
        esterilizacion.setmFaja(faja != 0);
        esterilizacion.setmPagado(pagado != 0);
        esterilizacion.setmIdCampaña(UUID.fromString(uuidStringCamp));
        esterilizacion.setmIdGato(UUID.fromString(uuidStringGato));
        return esterilizacion;
    }

}
