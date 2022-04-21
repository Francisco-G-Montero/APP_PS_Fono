package com.example.myapplication.logica;

import static com.example.myapplication.common.utils.UtilsCommon.getRandomIncorrectAnswerText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.myapplication.common.entities.OptionAnswer;
import com.example.myapplication.common.utils.UtilsCommon;
import com.example.myapplication.common.utils.UtilsSound;
import com.example.myapplication.controllers.ReproductorDeAudioController;
import com.example.myapplication.ActivityDetalleResultado;
import com.example.myapplication.R;
import com.example.myapplication.common.constants.Constantes;
import com.example.myapplication.databinding.ActivityEscribirOyoBinding;
import com.example.myapplication.room_database.palabras.Sound;
import com.example.myapplication.room_database.palabras.SoundRepository;
import com.example.myapplication.room_database.resultados.Resultado;
import com.example.myapplication.room_database.resultados.ResultadoRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EjercicioEscribirOyoActivity extends AppCompatActivity {
    private ActivityEscribirOyoBinding binding;
    ReproductorDeAudioController reproductorDeAudioController;
    public SoundRepository sr;
    List<Sound> listaSonidos;
    private final int maxRepetitions = 10;
    int puntajeCorrecto, puntajeIncorrecto, repeticiones;
    private final int wrongAnswersLimit = 2;
    private int incorrectCounterStage = 0;
    private final ArrayList<OptionAnswer> mOptionAnswersList = new ArrayList<>();
    int opcionCorrecta;
    String ruido;
    String modo;
    String subdato;
    String errores = "";
    float intensidad;
    double intensidadPorcentual;
    ImageButton btnPlay;
    private Button aceptar;
    private EditText etNombre;
    private TextView tvPuntajeCorrecto;
    private TextView tvPuntajeIncorrecto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEscribirOyoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        puntajeCorrecto = 0;
        puntajeIncorrecto = 0;
        repeticiones = 0;
        btnPlay = findViewById(R.id.imageButton);
        aceptar = findViewById(R.id.aceptar);
        etNombre = findViewById(R.id.campo_nombre);
        tvPuntajeCorrecto = findViewById(R.id.puntaje);
        tvPuntajeIncorrecto = findViewById(R.id.puntajeIncorrecto);
        //Datos pasados desde la configuración
        subdato = getIntent().getStringExtra("subDato");
        ruido = getIntent().getStringExtra("tipoRuido");
        intensidad = getIntent().getFloatExtra("intensidad", .1f);
        modo = getIntent().getStringExtra("modo");
        intensidadPorcentual = Math.floor(intensidad * 100);
        reproductorDeAudioController = new ReproductorDeAudioController();

        SoundRepository sr = new SoundRepository(getApplication());
        switch (subdato) {
            case Constantes.DIAS_SEMANA:
                sr.getDiasSounds().observe(this, new Observer<List<Sound>>() {
                    @Override
                    public void onChanged(List<Sound> sounds) {
                        listaSonidos = sounds;
                        setup(obtenerNumero());
                    }
                });
                break;
            case Constantes.NUMEROS:
                sr.getNumerosSounds().observe(this, new Observer<List<Sound>>() {
                    @Override
                    public void onChanged(List<Sound> sounds) {
                        listaSonidos = sounds;
                        setup(obtenerNumero());
                    }
                });
                break;
            case Constantes.MESES:
                sr.getMesesSounds().observe(this, new Observer<List<Sound>>() {
                    @Override
                    public void onChanged(List<Sound> sounds) {
                        listaSonidos = sounds;
                        setup(obtenerNumero());
                    }
                });
                break;

            case Constantes.COLORES:
                sr.getColoresSounds().observe(this, new Observer<List<Sound>>() {
                    @Override
                    public void onChanged(List<Sound> sounds) {
                        listaSonidos = sounds;
                        setup(obtenerNumero());
                    }
                });
                break;

            case Constantes.ORACIONES:
                sr.getOracionesSounds().observe(this, new Observer<List<Sound>>() {
                    @Override
                    public void onChanged(List<Sound> sounds) {
                        listaSonidos = sounds;
                        setup(obtenerNumero());
                    }
                });
                break;
        }
    }

    void setup(final int rand) {
        final int respuestaIndex = rand;
        OptionAnswer optionAnswer = new OptionAnswer();
        optionAnswer.setCorrectAnswer(listaSonidos.get(rand).getNombre_sonido());
        mOptionAnswersList.add(optionAnswer);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reproductorDeAudioController.startSoundNoNoise(listaSonidos.get(rand).getRuta_sonido(), getApplicationContext());
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNombre.getText().toString().toLowerCase().equals(listaSonidos.get(respuestaIndex).getNombre_sonido().toLowerCase())) {
                    modificarPuntaje(tvPuntajeCorrecto, respuestaIndex);
                    aceptar.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce));
                    etNombre.setText("");
                    setup(obtenerNumero());
                } else {
                    OptionAnswer optionAnswer = mOptionAnswersList.get(mOptionAnswersList.size() - 1);
                    optionAnswer.addError(etNombre.getText().toString() + " ✖");
                    mOptionAnswersList.set(mOptionAnswersList.size() - 1, optionAnswer);

                    if (!etNombre.getText().toString().isEmpty()) {
                        aceptar.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_animation));
                        modificarPuntaje(tvPuntajeIncorrecto, respuestaIndex);
                        etNombre.setText("");
                    }
                }
            }
        });
    }


    void modificarPuntaje(TextView puntaje, int respuestaIndex) {
        String points = null;

        if (puntaje.getId() == R.id.puntaje) {
            puntajeCorrecto++;
            incorrectCounterStage = 0;
            points = Integer.toString(puntajeCorrecto);
            UtilsSound.announceAnswerSound(binding.getRoot(), true);
        }
        if (puntaje.getId() == R.id.puntajeIncorrecto) {
            incorrectCounterStage++;
            puntajeIncorrecto++;
            points = Integer.toString(puntajeIncorrecto);
            errores = errores + etNombre.getText().toString() + " ✖";
            UtilsSound.announceAnswerSound(binding.getRoot(), false);
            if (incorrectCounterStage >= wrongAnswersLimit) {
                errores += "-";
                incorrectCounterStage = 0;
                UtilsCommon.displayAlertMessage(binding.getRoot(),
                        "¡Te has equivocado más de " + wrongAnswersLimit + " veces!",
                        "La respuesta correcta era: \"" + listaSonidos.get(respuestaIndex).getNombre_sonido() + "\""
                                + "\nPasemos al siguiente.");
                reproductorDeAudioController.startSoundNoNoise(listaSonidos.get(respuestaIndex).getRuta_sonido(), getApplicationContext());
                setup(obtenerNumero());
            } else {
                errores += ",";
                int incorrectAnswerRes = getRandomIncorrectAnswerText();
                UtilsCommon.showSnackbar(binding.getRoot(), getString(incorrectAnswerRes));
            }
        }
        puntaje.setText(points);

        if (modo.equals(Constantes.EVALUACION) && finEjercicio()) {
            //Toast.makeText(Ejercicio_Tres_Opciones.this, "Puntaje Correcto" + puntajeCorrecto + " Puntaje Incorrecto " + puntajeIncorrecto, Toast.LENGTH_SHORT).show();
            Date date = Calendar.getInstance().getTime();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String today = formatter.format(date);
            ResultadoRepository resultadoRepository = new ResultadoRepository(getApplication());
            ArrayList<String> aciertosList = new ArrayList<>();
            for(OptionAnswer optionAnswer : mOptionAnswersList){
                aciertosList.add(optionAnswer.getCorrectAnswer());
            }
            Resultado resultado = new Resultado(today, Constantes.J_ESCRIBIR_LO_QUE_OYO, subdato, ruido, intensidadPorcentual + "%", errores, aciertosList.toString(), puntajeCorrecto + "");
            resultadoRepository.agregarResultado(resultado);
            Intent intent = new Intent(getApplicationContext(), ActivityDetalleResultado.class);
            intent.putExtra("fecha", today);
            intent.putExtra("ejercicio", Constantes.J_ESCRIBIR_LO_QUE_OYO);
            intent.putExtra("categoria", subdato);
            intent.putExtra("ruido", ruido);
            intent.putExtra("intensidad", intensidadPorcentual + "%");
            intent.putExtra("errores", errores);
            intent.putExtra("resultado", puntajeCorrecto + "");
            intent.putExtra(getString(R.string.error_resume), mOptionAnswersList);
            startActivity(intent);
        }
    }

    boolean finEjercicio() {
        repeticiones++;
        return (repeticiones >= maxRepetitions);
    }

    int obtenerNumero() {
        return ThreadLocalRandom.current().nextInt(0, listaSonidos.size());
    }
}
