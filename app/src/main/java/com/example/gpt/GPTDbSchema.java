package com.example.gpt;

public class GPTDbSchema {
    public static final class CampañaTable {
        public static final String NAME = "campañas";

        public static final class Cols {
            public static String UUID = "uuid";
            public static String NOMBRE = "nombre";
            public static String FECHA = "fecha";
        }

    }

    public static final class GatoTable {
        public static final String NAME = "gatos";
        public static final class Cols{
            public static String UUID = "uuid";
            public static String PESO = "peso";
            public static String FECHA_NACIMIENTO = "fecha_nacimiento";
            public static String NOMBRE = "nombre";
            public static String SEXO = "sexo";
            public static String CONDICION_ESPECIAL = "condicion_especial";
            public static String PROCEDENCIA = "procedencia";
        }
    }

    public static final class GatoHogarTable {
        public static final String NAME = "gatos_hogar";
        public static final class Cols{
            public static String FKUUID_GATO = "gato_id";
            public static String FKUUID_PERSONA = "persona_id";
        }
    }

    public static final class PersonaTable {
        public static final String NAME = "personas";
        public static final class Cols{
            public static String UUID = "uuid";
            public static String NOMBRE = "nombre";
            public static String APELLIDO_MATERNO = "apellido_materno";
            public static String APELLIDO_PATERNO = "apellido_paterno";
            public static String DOMICILIO = "domicilio";
            public static String CELULAR = "celular";
            public static String EMAIL = "email";
        }
    }

    public static final class MaterialTable {
        public static final String NAME = "material";
        public static final class Cols{
            public static String UUID = "uuid";
            public static String TIPO_INVENTARIO = "tipo_inventario";
            public static String PRESENTACION = "presentacion";
            public static String CATEGORIA = "categoria";
            public static String NOMBRE = "nombre";
            public static String CANTIDAD = "cantidad";
        }
    }

    public static final class MaterialCampañaTable {
        public static final String NAME = "material_campaña";
        public static final class Cols{
            public static String FKUUID_CAMPAÑA = "campaña_id";
            public static String CANTIDAD_GASTADA = "cantidad";
            public static String FKUUID_MATERIAL = "material_id";
        }
    }

    public static final class EsterilizacionTable {
        public static final String NAME="esterilizaciones";
        public static final class Cols{
            public static String UUID = "uuid";
            public static String PRECIO = "precio";
            public static String FAJA = "faja";
            public static String COSTO_EXTRA = "costo_extra";
            public static String ANTICIPO = "anticipo";
            public static String PAGADO = "pagado";
            public static String FKUUID_CAMPAÑA= "campaña_id";
            public static String FKUUID_GATO= "gato_id";
        }
    }

    public static final class IngresoTable {
        public static final String NAME="ingresos";
        public static final class Cols{
            public static String UUID = "uuid";
            public static String MOTIVO = "motivo";
            public static String FECHA = "fecha";
            public static String CANTIDAD = "cantidad";
            public static String AUTOMATICO = "automatico";
        }
    }

    public static final class AdoptanteTable {
        public static final String NAME = "adoptantes";
        public static final class Cols{
            public static String UUID = "uuid";
            public static String NOMBRE = "nombre";
            public static String APELLIDO_MATERNO = "apellido_materno";
            public static String APELLIDO_PATERNO = "apellido_paterno";
            public static String DOMICILIO = "domicilio";
            public static String CELULAR = "celular";
            public static String EMAIL = "email";
        }
    }

    public static final class RegistroAdopcionTable {
        public static final String NAME="registro_adopciones";
        public static final class Cols{
            public static String UUID = "uuid";
            public static String FKUUID_ADOPTANTE= "adoptante_id";
            public static String FKUUID_GATO= "gato_id";
            public static String ESTATUS= "estatus";
        }
    }
    public static final class AdopcionTable {
        public static final String NAME="adopciones";
        public static final class Cols{
            public static String FKUUID_GATO= "gato_id";
        }
    }
}
