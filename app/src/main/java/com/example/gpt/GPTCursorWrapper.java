package com.example.gpt;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import static com.example.gpt.GPTDbSchema.*;

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
        String uuidString = getString(getColumnIndex(CampañaTable.Cols.UUID));
        String nombre = getString(getColumnIndex(CampañaTable.Cols.NOMBRE));
        long date = getLong(getColumnIndex(CampañaTable.Cols.FECHA));
        Campaña campaña = new Campaña(UUID.fromString(uuidString));
        campaña.setmNombreCampaña(nombre);
        campaña.setmFechaCampaña(new Date(date));
        return campaña;
    }
    public Gato getGato (){
        String uuidString = getString(getColumnIndex(GatoTable.Cols.UUID));
        //Blob foto = getBlob(getColumnIndex(GPTDbSchema.GatoTable.Cols.FOTO));
        long fecha_nacimiento = getLong(getColumnIndex(GatoTable.Cols.FECHA_NACIMIENTO));
        String peso = getString(getColumnIndex(GatoTable.Cols.PESO));
        String sexo = getString(getColumnIndex(GatoTable.Cols.SEXO));
        String nombre = getString(getColumnIndex(GatoTable.Cols.NOMBRE));
        String condicionEspecial = getString(getColumnIndex(GatoTable.Cols.CONDICION_ESPECIAL));
        int procedencia = getInt(getColumnIndex(GatoTable.Cols.PROCEDENCIA));

        Gato gato = new Gato(UUID.fromString(uuidString));
        gato.setmPeso(peso);
        gato.setmFechaNacimiento(new Date(fecha_nacimiento));
        gato.setmSexo(sexo);
        gato.setmNombreGato(nombre);
        gato.setmCondicionEspecial(condicionEspecial);
        gato.setmProcedencia(procedencia);
        return gato;
    }

    public GatoHogar getGatoHogar (){
        String uuidGato = getString(getColumnIndex(GatoHogarTable.Cols.FKUUID_GATO));
        String uuidPersona = getString(getColumnIndex(GatoHogarTable.Cols.FKUUID_PERSONA));

        GatoHogar gatoHogar = new GatoHogar(UUID.fromString(uuidGato));
        gatoHogar.setmPersonaId(UUID.fromString(uuidPersona));
        return gatoHogar;
    }

    public Persona getPersona (){
        String uuidString = getString(getColumnIndex(PersonaTable.Cols.UUID));
        String nombre = getString(getColumnIndex(PersonaTable.Cols.NOMBRE));
        String apellidoM = getString(getColumnIndex(PersonaTable.Cols.APELLIDO_MATERNO));
        String apellidoP = getString(getColumnIndex(PersonaTable.Cols.APELLIDO_PATERNO));
        String domicilio = getString(getColumnIndex(PersonaTable.Cols.DOMICILIO));
        String celular = getString(getColumnIndex(PersonaTable.Cols.CELULAR));
        String email = getString(getColumnIndex(PersonaTable.Cols.EMAIL));

        Persona persona = new Persona(UUID.fromString(uuidString));
        persona.setmNombre(nombre);
        persona.setmApellidoMaterno(apellidoM);
        persona.setmApellidoPaterno(apellidoP);
        persona.setmCelular(celular);
        persona.setmDomicilio(domicilio);
        persona.setmEmail(email);
        return persona;
    }

    public Material getMaterial (){
        String uuidString = getString(getColumnIndex(MaterialTable.Cols.UUID));
        String nombre = getString(getColumnIndex(MaterialTable.Cols.NOMBRE));
        String presentacion = getString(getColumnIndex(MaterialTable.Cols.PRESENTACION));
        int tipoInventario = getInt(getColumnIndex(MaterialTable.Cols.TIPO_INVENTARIO));
        int categoria = getInt(getColumnIndex(MaterialTable.Cols.CATEGORIA));
        int cantidad = getInt(getColumnIndex(MaterialTable.Cols.CANTIDAD));

        Material material = new Material(UUID.fromString(uuidString));
        material.setmNombre(nombre);
        material.setmPresentacion(presentacion);
        material.setmCantidad(cantidad);
        material.setmTipoInventario(tipoInventario);
        material.setmCategoria(categoria);
        return material;
    }

    public MaterialCampaña getMaterialCampaña (){
        String uuidCampaña = getString(getColumnIndex(MaterialCampañaTable.Cols.FKUUID_CAMPAÑA));
        String uuidMaterial= getString(getColumnIndex(MaterialCampañaTable.Cols.FKUUID_MATERIAL));
        int cantidadGastada = getInt(getColumnIndex(MaterialCampañaTable.Cols.CANTIDAD_GASTADA));

        MaterialCampaña materialCampaña = new MaterialCampaña(UUID.fromString(uuidCampaña));
        materialCampaña.setmMaterialId(UUID.fromString(uuidMaterial));
        materialCampaña.setmCantidadGastada(cantidadGastada);
        return materialCampaña;
    }


    public Esterilizacion getEsterilizacion (){
        String uuidString = getString(getColumnIndex(EsterilizacionTable.Cols.UUID));
        String uuidStringCamp = getString(getColumnIndex(EsterilizacionTable.Cols.FKUUID_CAMPAÑA));
        String uuidStringGato = getString(getColumnIndex(EsterilizacionTable.Cols.FKUUID_GATO));
        int precio = getInt(getColumnIndex(EsterilizacionTable.Cols.PRECIO));
        int anticipo = getInt(getColumnIndex(EsterilizacionTable.Cols.ANTICIPO));
        int costoExtra = getInt(getColumnIndex(EsterilizacionTable.Cols.COSTO_EXTRA));
        int pagado = getInt(getColumnIndex(EsterilizacionTable.Cols.PAGADO));
        int faja = getInt(getColumnIndex(EsterilizacionTable.Cols.FAJA));


        Esterilizacion esterilizacion = new Esterilizacion(UUID.fromString(uuidString));
        esterilizacion.setmPrecio(precio);
        esterilizacion.setmAnticipo(anticipo);
        esterilizacion.setmCostoExtra(costoExtra);
        esterilizacion.setmFaja(faja != 0);
        esterilizacion.setmPagado(pagado != 0);
        esterilizacion.setmIdCampaña(UUID.fromString(uuidStringCamp));
        esterilizacion.setmIdGato(UUID.fromString(uuidStringGato));
        return esterilizacion;
    }

    public Ingreso getIngreso (){
        String uuidString = getString(getColumnIndex(IngresoTable.Cols.UUID));
        int cantidad = getInt(getColumnIndex(IngresoTable.Cols.CANTIDAD));
        String motivo = getString(getColumnIndex(IngresoTable.Cols.MOTIVO));
        long fecha = getLong(getColumnIndex(IngresoTable.Cols.FECHA));
        int automatico = getInt(getColumnIndex(IngresoTable.Cols.AUTOMATICO));


        Ingreso ingreso = new Ingreso(UUID.fromString(uuidString));
        ingreso.setmCantidad(cantidad);
        ingreso.setMotivo(motivo);
        ingreso.setmFecha(new Date(fecha));
        ingreso.setmAutomatico(automatico != 0);
        return ingreso;
    }

}
