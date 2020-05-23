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

    public static final class EsterilizacionesTable{
        public static final String NAME="esterilizaciones";
        public static final class Cols{

        }
    }
}
