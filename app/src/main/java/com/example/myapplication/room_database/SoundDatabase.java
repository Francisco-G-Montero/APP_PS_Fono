package com.example.myapplication.room_database;

import static com.example.myapplication.common.constants.Connectors.FILE_ES_HOY;
import static com.example.myapplication.common.constants.Connectors.FILE_HOY_ES;
import static com.example.myapplication.common.constants.Connectors.FILE_NECESITO;
import static com.example.myapplication.common.constants.Connectors.FILE_VASOS_GRANDES;
import static com.example.myapplication.common.constants.Connectors.FILE_Y_LLOVIENDO;
import static com.example.myapplication.common.constants.Days.FILE_DOMINGO;
import static com.example.myapplication.common.constants.Days.FILE_JUEVES;
import static com.example.myapplication.common.constants.Days.FILE_LUNES;
import static com.example.myapplication.common.constants.Days.FILE_MARTES;
import static com.example.myapplication.common.constants.Days.FILE_MIERCOLES;
import static com.example.myapplication.common.constants.Days.FILE_SABADO;
import static com.example.myapplication.common.constants.Days.FILE_VIERNES;
import static com.example.myapplication.common.constants.FilePaths.PATH_CONNECTORS_FEMALE;
import static com.example.myapplication.common.constants.FilePaths.PATH_DAYS_FEMALE;
import static com.example.myapplication.common.constants.FilePaths.PATH_NUMBERS_FEMALE;
import static com.example.myapplication.common.constants.Numbers.*;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.common.constants.Constantes;
import com.example.myapplication.room_database.palabras.Sound;
import com.example.myapplication.room_database.palabras.SoundDao;
import com.example.myapplication.room_database.resultados.Resultado;
import com.example.myapplication.room_database.resultados.ResultadoDao;

@Database(entities = {Sound.class, Resultado.class}, exportSchema = false, version = 1)
public abstract class SoundDatabase extends RoomDatabase {
    private static final String DB_NAME = "app_db";
    private static SoundDatabase instance;
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new LlenarDBAsyncTasck(instance).execute();
        }
    };

    public static synchronized SoundDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), SoundDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    public abstract SoundDao soundDao();

    public abstract ResultadoDao resultadoDao();

    private static class LlenarDBAsyncTasck extends AsyncTask<Void, Void, Void> {
        private SoundDao soundDao;
        private ResultadoDao resultadoDao;

        private LlenarDBAsyncTasck(SoundDatabase db) {
            soundDao = db.soundDao();
            resultadoDao = db.resultadoDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Meses
            soundDao.agregarSonido(new Sound("Enero", Constantes.MESES, "enero.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Febrero", Constantes.MESES, "febrero.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Marzo", Constantes.MESES, "marzo.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Abril", Constantes.MESES, "abril.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Mayo", Constantes.MESES, "mayo.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Junio", Constantes.MESES, "junio.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Julio", Constantes.MESES, "julio.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Agosto", Constantes.MESES, "agosto.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Septiembre", Constantes.MESES, "septiembre.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Octubre", Constantes.MESES, "octubre.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Noviembre", Constantes.MESES, "noviembre.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Diciembre", Constantes.MESES, "diciembre.mp3", Constantes.NO_PERSONALIZADO));

            //Colores
            soundDao.agregarSonido(new Sound("Negro", Constantes.COLORES, "negro.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Azul", Constantes.COLORES, "azul.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Blanco", Constantes.COLORES, "blanco.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Marrón", Constantes.COLORES, "marron.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Amarillo", Constantes.COLORES, "amarillo.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Rojo", Constantes.COLORES, "rojo.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Rosa", Constantes.COLORES, "rosa.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Verde", Constantes.COLORES, "verde.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Violeta", Constantes.COLORES, "violeta.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Naranja", Constantes.COLORES, "naranja.mp3", Constantes.NO_PERSONALIZADO));

            //Ruidos
            soundDao.agregarSonido(new Sound("Multitud de personas", Constantes.RUIDO, "ruido_personas.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Recreo de niños", Constantes.RUIDO, "ruido_recreo.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Sirena Ambulancia", Constantes.RUIDO, "ruido_ambulancia.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Tráfico Intenso", Constantes.RUIDO, "ruido_trafico.mp3", Constantes.NO_PERSONALIZADO));

            /*soundDao.agregarSonido(new Sound("Al que madruga, Dios lo ayuda", Constantes.ORACIONES, "al que madruga.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("En boca cerrada no entran moscas", Constantes.ORACIONES, "en boca cerrada.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("No por mucho madrugar, se amanece más temprano", Constantes.ORACIONES, "no por mucho madrugar.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("No hay mal que por bien no venga", Constantes.ORACIONES, "no hay mal que.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("En casa de herrero, cuchillo de palo", Constantes.ORACIONES, "en casa de herrero.mp3", Constantes.NO_PERSONALIZADO));
            */

            //NUMEROS MUJER 1-30
            soundDao.agregarSonido(new Sound("1", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_UNO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("2", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_DOS, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("3", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_TRES, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("4", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CUATRO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("5", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CINCO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("6", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_SEIS, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("7", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_SIETE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("8", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_OCHO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("9", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_NUEVE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("10", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_DIEZ, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("11", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_ONCE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("12", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_DOCE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("13", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_TRECE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("14", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CATORCE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("15", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_QUINCE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("16", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_DIECISEIS, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("17", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_DIECISIETE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("18", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_DIECIOCHO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("19", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_DIECINUEVE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("20", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_VEINTE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("21", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_VEINTIUNO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("22", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_VEINTIDOS, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("23", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_VEINTITRES, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("24", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_VEINTICUATRO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("25", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_VEINTICINCO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("26", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_VEINTISEIS, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("27", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_VEINTISIETE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("28", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_VEINTIOCHO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("29", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_VEINTINUEVE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("30", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_TREINTA, Constantes.NO_PERSONALIZADO));
            //MUJER NUMEROS 100-130
            soundDao.agregarSonido(new Sound("101", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_UNO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("102", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_DOS, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("103", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_TRES, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("104", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_CUATRO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("105", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_CINCO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("106", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_SEIS, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("107", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_SIETE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("108", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_OCHO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("109", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_NUEVE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("110", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_DIEZ, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("111", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_ONCE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("112", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_DOCE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("113", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_TRECE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("114", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_CATORCE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("115", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_QUINCE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("116", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_DIECISEIS, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("117", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_DIECISIETE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("118", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_DIECIOCHO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("119", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_DIECINUEVE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("120", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_VEINTE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("121", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_VEINTIUNO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("122", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_VEINTIDOS, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("123", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_VEINTITRES, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("124", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_VEINTICUATRO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("125", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_VEINTICINCO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("126", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_VEINTISEIS, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("127", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_VEINTISIETE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("128", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_VEINTIOCHO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("129", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_VEINTINUEVE, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("130", Constantes.NUMEROS, PATH_NUMBERS_FEMALE + FILE_CIENTO_TREINTA, Constantes.NO_PERSONALIZADO));
            //CONECTOR NECESITO MUJER
            soundDao.agregarSonido(new Sound("necesito", Constantes.VARIOS, PATH_CONNECTORS_FEMALE + FILE_NECESITO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("vasos grandes", Constantes.VARIOS, PATH_CONNECTORS_FEMALE + FILE_VASOS_GRANDES, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("es hoy", Constantes.VARIOS, PATH_CONNECTORS_FEMALE + FILE_ES_HOY, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("hoy es", Constantes.VARIOS, PATH_CONNECTORS_FEMALE + FILE_HOY_ES, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("y esta lloviendo", Constantes.VARIOS, PATH_CONNECTORS_FEMALE + FILE_Y_LLOVIENDO, Constantes.NO_PERSONALIZADO));
            //DIAS MUJER
            soundDao.agregarSonido(new Sound("Lunes", Constantes.DIAS_SEMANA, PATH_DAYS_FEMALE + FILE_LUNES, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Martes", Constantes.DIAS_SEMANA, PATH_DAYS_FEMALE + FILE_MARTES, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Miércoles", Constantes.DIAS_SEMANA, PATH_DAYS_FEMALE + FILE_MIERCOLES, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Jueves", Constantes.DIAS_SEMANA, PATH_DAYS_FEMALE + FILE_JUEVES, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Viernes", Constantes.DIAS_SEMANA, PATH_DAYS_FEMALE + FILE_VIERNES, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Sábado", Constantes.DIAS_SEMANA, PATH_DAYS_FEMALE + FILE_SABADO, Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("Domingo", Constantes.DIAS_SEMANA, PATH_DAYS_FEMALE + FILE_DOMINGO, Constantes.NO_PERSONALIZADO));

            //ORACIONES MUJER
            soundDao.agregarSonido(new Sound("Al que madruga, Dios lo ayuda", Constantes.ORACIONES, "al que madruga.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("En boca cerrada no entran moscas", Constantes.ORACIONES, "en boca cerrada.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("No por mucho madrugar, se amanece más temprano", Constantes.ORACIONES, "no por mucho madrugar.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("No hay mal que por bien no venga", Constantes.ORACIONES, "no hay mal que.mp3", Constantes.NO_PERSONALIZADO));
            soundDao.agregarSonido(new Sound("En casa de herrero, cuchillo de palo", Constantes.ORACIONES, "en casa de herrero.mp3", Constantes.NO_PERSONALIZADO));
            return null;
        }
    }
}
