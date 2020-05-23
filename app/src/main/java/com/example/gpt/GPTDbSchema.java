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

    public static final class GatosTable {
        public static final String NAME = "gatos";
        public static final class Cols{
            public static String UUID = "uuid";
            public static String PESO = "peso";
            public static String FOTO = "foto";
            public static String FECHA_NACIMIENTO = "fecha nacimiento";
            public static String NOMBRE = "nombre";
            public static String SEXO = "sexo";
            public static String CONDICION_ESPECIAL = "condicion especial";
            public static String PROCEDENCIA = "procedencia";
        }
    }

    public static final class EsterilizacionesTable{
        public static final String NAME="esterilizaciones";
        public static final class Cols{
            public static String UUID = "uuid";
            public static String FECHA = "fecha";
            public static String PRECIO = "precio";
            public static String FAJA = "faja";
            public static String ANTICIPO = "anticipo";
            public static String FKUUID_CAMPAÑA= "fkuuid campaña";
            public static String FKUUID_GATO= "fkuuid gato";
        }
    }
}
