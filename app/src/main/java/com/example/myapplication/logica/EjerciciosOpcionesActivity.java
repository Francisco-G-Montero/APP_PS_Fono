package com.example.myapplication.logica;

import static com.example.myapplication.common.constants.Constantes.NEXT_SEPARATOR_ERROR;
import static com.example.myapplication.common.constants.Constantes.SAME_EXERCISE_ERROR;
import static com.example.myapplication.common.utils.UtilsCommon.getRandomCorrectAnswerText;
import static com.example.myapplication.common.utils.UtilsCommon.getRandomIncorrectAnswerText;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.common.adapters.OnOptionClickListener;
import com.example.myapplication.common.adapters.OptionsAdapter;
import com.example.myapplication.common.entities.OptionAnswer;
import com.example.myapplication.controllers.ReproductorDeAudioController;
import com.example.myapplication.ActivityDetalleResultado;
import com.example.myapplication.R;
import com.example.myapplication.common.utils.UtilsCommon;
import com.example.myapplication.common.utils.UtilsSound;
import com.example.myapplication.databinding.ActivityEjerciciosOpcionesBinding;
import com.example.myapplication.common.constants.Constantes;
import com.example.myapplication.room_database.palabras.Sound;
import com.example.myapplication.room_database.palabras.SoundRepository;
import com.example.myapplication.room_database.resultados.Resultado;
import com.example.myapplication.room_database.resultados.ResultadoRepository;
import com.google.android.material.button.MaterialButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class EjerciciosOpcionesActivity extends AppCompatActivity implements OnOptionClickListener {
    public SoundRepository soundRepository;
    private List<Sound> listaSonidos;
    private final Random randomBoolean = new Random();
    private final ReproductorDeAudioController audioController = new ReproductorDeAudioController();
    private Sound necesitoSound, vasosGrandesSound, esHoySound, hoyEsSound, lloviendoSound;
    double intensidadPorcentual;
    private int puntajeCorrecto;
    private int puntajeIncorrecto;
    private int repeticiones;
    private int incorrectCounterStage = 0;
    private int optionsQuantity = 0;
    private final int maxRepetitions = 10;
    private String tipoEjercicio = "";
    private int opcionCorrecta;
    private final int wrongAnswersLimit = 2;
    private String ruido, subdato, errores = "", categoriaSeleccionada = "", modo;
    private final ArrayList<OptionAnswer> mOptionAnswersList = new ArrayList<>();
    private final ArrayList<String> mOptions = new ArrayList<>();
    private ArrayList<Integer> optionList;
    private OptionsAdapter mAdapter;
    private ReproductorDeAudioController mReproductorDeAudioController;
    private ActivityEjerciciosOpcionesBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityEjerciciosOpcionesBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        puntajeCorrecto = 0;
        puntajeIncorrecto = 0;
        repeticiones = 0;

        //Datos pasados desde la configuración
        categoriaSeleccionada = getIntent().getStringExtra("categoriaSeleccionada");
        subdato = getIntent().getStringExtra("subDato");
        ruido = getIntent().getStringExtra("tipoRuido");
        float intensidad = getIntent().getFloatExtra("intensidad", .1f);
        modo = getIntent().getStringExtra("modo");
        intensidadPorcentual = Math.floor(intensidad * 100);
        mReproductorDeAudioController = ReproductorDeAudioController.getmInstance();
        mReproductorDeAudioController.setIntensidad(intensidad);

        mBinding.btnSettings.setOnClickListener(v -> UtilsCommon.displayNoiseSettingsAlert(mBinding.getRoot()));
        soundRepository = new SoundRepository(getApplication());
        switch (subdato) {
            case Constantes.DIAS_SEMANA:
                soundRepository.getDiasSounds().observe(this, sounds -> {
                    soundRepository.getHoyEsSound().observe(this, hoyEs -> {
                        hoyEsSound = hoyEs;
                        soundRepository.getEsHoySound().observe(this, esHoy -> {
                            esHoySound = esHoy;
                            soundRepository.getyLLoviendoSound().observe(this, lloviendo -> {
                                lloviendoSound = lloviendo;
                                listaSonidos = sounds;
                                setup();
                            });
                        });
                    });
                });
                break;
            case Constantes.NUMEROS:
                soundRepository.getNumerosSounds().observe(this, sounds -> {
                    if (categoriaSeleccionada.equals(Constantes.ORACIONES)) {
                        soundRepository.getNecesitoSound().observe(this, necesito -> {
                            soundRepository.getVasosGrandes().observe(this, vasosGrandes -> {
                                vasosGrandesSound = vasosGrandes;
                                necesitoSound = necesito;
                                listaSonidos = sounds;
                                setup();
                            });
                        });
                    } else {
                        listaSonidos = sounds;
                        setup();
                    }
                });
                break;
            case Constantes.MESES:
                soundRepository.getMesesSounds().observe(this, sounds -> {
                    listaSonidos = sounds;
                    setup();
                });
                break;

            case Constantes.COLORES:
                soundRepository.getColoresSounds().observe(this, sounds -> {
                    listaSonidos = sounds;
                    setup();
                });
                break;
        }
    }

    void setup() {
        mOptions.clear();
        tipoEjercicio = getIntent().getStringExtra(getString(R.string.tipo_ejercicio));
        if (tipoEjercicio.equals(Constantes.J_DISCRIMINAR))
            optionsQuantity = 2;
        else if (tipoEjercicio.equals(Constantes.J_IDENTIFICAR_TRES_OPCIONES))
            optionsQuantity = 3;
        else if (tipoEjercicio.equals(Constantes.J_IDENTIFICAR_CINCO_OPCIONES))
            optionsQuantity = 5;
        else if (tipoEjercicio.equals(Constantes.J_TODA_LA_CATEGORIA))
            optionsQuantity = listaSonidos.size();
        optionList = obtenerNumero(optionsQuantity);
        setTexts(optionList);
        mAdapter = new OptionsAdapter(mOptions, this);
        mBinding.recyclerView.setAdapter(mAdapter);
        Random rand = new Random();
        opcionCorrecta = rand.nextInt(optionList.size());
        OptionAnswer optionAnswer = new OptionAnswer();
        optionAnswer.setCorrectAnswer(listaSonidos.get(optionList.get(opcionCorrecta)).getNombre_sonido());
        mOptionAnswersList.add(optionAnswer);

        mBinding.btnPlay.setOnClickListener(v -> {
            if (activarSonido()) {
                soundRepository.getRutaSonido(ruido).observe(this, sounds -> {
                    audioController.startSoundWithNoise(
                            listaSonidos.get(optionList.get(opcionCorrecta)).getRuta_sonido(),
                            sounds.get(0).getRuta_sonido(),
                            mReproductorDeAudioController.getIntensidad(),
                            getApplicationContext()
                    );
                });
            } else if (categoriaSeleccionada.equals(Constantes.ORACIONES) && subdato.equals(Constantes.NUMEROS)){
                boolean complexSentence = getRandomBoolean();
                if(complexSentence){
                    audioController.sentenceWithConnectors(
                            necesitoSound.getRuta_sonido(),
                            vasosGrandesSound.getRuta_sonido(),
                            listaSonidos.get(optionList.get(opcionCorrecta)).getRuta_sonido(),
                            true,
                            getApplicationContext());
                }else{
                    audioController.sentenceWithConnectors(
                            necesitoSound.getRuta_sonido(),
                            null,
                            listaSonidos.get(optionList.get(opcionCorrecta)).getRuta_sonido(),
                            getRandomBoolean(),
                            getApplicationContext());
                }
            } else if (categoriaSeleccionada.equals(Constantes.ORACIONES) && subdato.equals(Constantes.DIAS_SEMANA)){
                boolean complexSentence = getRandomBoolean();
                if(complexSentence){
                    audioController.sentenceWithConnectors(
                            hoyEsSound.getRuta_sonido(),
                            lloviendoSound.getRuta_sonido(),
                            listaSonidos.get(optionList.get(opcionCorrecta)).getRuta_sonido(),
                            true,
                            getApplicationContext());
                }else{
                    boolean connectorAtStart = getRandomBoolean();
                    Sound connector;
                    if (connectorAtStart){
                        connector = hoyEsSound;
                    } else {
                        connector = esHoySound;
                    }
                    audioController.sentenceWithConnectors(
                            connector.getRuta_sonido(),
                            null,
                            listaSonidos.get(optionList.get(opcionCorrecta)).getRuta_sonido(),
                            connectorAtStart,
                            getApplicationContext());
                }
            } else {
                audioController.startSoundNoNoise(listaSonidos.get(optionList.get(opcionCorrecta)).getRuta_sonido(), getApplicationContext());
            }
        });
    }

    void setTexts(ArrayList<Integer> optionList) {
        for (int i = 0; i < optionList.size(); i++)
            mOptions.add(listaSonidos.get(optionList.get(i)).getNombre_sonido());
    }

    private ArrayList<Integer> obtenerNumero(int optionQuantity) {
        ArrayList<Integer> randoms = new ArrayList<>();
        for (int i = 0; i < listaSonidos.size(); i++) randoms.add(i);
        Collections.shuffle(randoms);
        ArrayList<Integer> optionsList = new ArrayList<>();
        for (int i = 0; i < optionQuantity; i++)
            optionsList.add(randoms.get(i));
        return optionsList;
    }

    void modificarPuntaje(Boolean isCorrect, String btnText) {
        String points;

        boolean finishExercise = finEjercicio();
        if (isCorrect) {
            errores += NEXT_SEPARATOR_ERROR;
            puntajeCorrecto++;
            incorrectCounterStage = 0;
            points = Integer.toString(puntajeCorrecto);
            UtilsSound.announceAnswerSound(mBinding.getRoot(), true);
            int correctAnswerRes = getRandomCorrectAnswerText();
            UtilsCommon.showSnackbar(mBinding.getRoot(), getString(correctAnswerRes));
            mBinding.tvPuntajeCorrecto.setText(points);
        } else {
            OptionAnswer optionAnswer = mOptionAnswersList.get(mOptionAnswersList.size() - 1);
            optionAnswer.addError(btnText+" ✖");
            mOptionAnswersList.set(mOptionAnswersList.size() - 1, optionAnswer);
            puntajeIncorrecto++;
            incorrectCounterStage++;
            points = Integer.toString(puntajeIncorrecto);
            UtilsSound.announceAnswerSound(mBinding.getRoot(), false);
            errores = errores + btnText + " ✖";
            if (incorrectCounterStage >= wrongAnswersLimit) {
                errores += NEXT_SEPARATOR_ERROR;
                incorrectCounterStage = 0;
                if(!finishExercise) {
                    UtilsCommon.displayAlertMessage(mBinding.getRoot(),
                        "¡Te has equivocado más de " + wrongAnswersLimit + " veces!",
                        "La respuesta correcta era: \"" + listaSonidos.get(optionList.get(opcionCorrecta)).getNombre_sonido() + "\""
                                + "\nPasemos al siguiente.");
                    ReproductorDeAudioController.getmInstance().startSoundNoNoise(listaSonidos.get(optionList.get(opcionCorrecta)).getRuta_sonido(), getApplicationContext());
                    setup();
                }
            } else {
                errores += SAME_EXERCISE_ERROR;
                int incorrectAnswerRes = getRandomIncorrectAnswerText();
                UtilsCommon.showSnackbar(mBinding.getRoot(), getString(incorrectAnswerRes));
                if(finishExercise) {
                    optionAnswer.addError("——");
                    errores += "——" + NEXT_SEPARATOR_ERROR;
                }
            }
            mBinding.tvPuntajeIncorrecto.setText(points);
        }


        if (modo.equals(Constantes.EVALUACION) && finishExercise) {
            Date date = Calendar.getInstance().getTime();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dateToday = formatter.format(date);
            ResultadoRepository resultadoRepository = new ResultadoRepository(getApplication());
            ArrayList<String> aciertosList = new ArrayList<>();
            for(OptionAnswer optionAnswer : mOptionAnswersList){
                aciertosList.add(optionAnswer.getCorrectAnswer());
            }
            Resultado resultado = new Resultado(dateToday, tipoEjercicio, subdato, ruido, intensidadPorcentual + "%", errores, aciertosList.toString(), puntajeCorrecto + "");
            resultadoRepository.agregarResultado(resultado);
            Intent intent = new Intent(getApplicationContext(), ActivityDetalleResultado.class);
            intent.putExtra("fecha", dateToday);
            intent.putExtra("ejercicio", tipoEjercicio);
            intent.putExtra("categoria", subdato);
            intent.putExtra("ruido", ruido);
            intent.putExtra("intensidad", mReproductorDeAudioController.getIntensidadPorcentual() + "%");
            intent.putExtra("resultado", puntajeCorrecto + "");
            intent.putExtra("errores", errores);
            intent.putExtra(getString(R.string.error_resume), mOptionAnswersList);
            startActivity(intent);
        }
    }

    boolean activarSonido() {
        return !ruido.equals("Sin Ruido");
    }

    boolean finEjercicio() {
        repeticiones++;
        return (repeticiones >= maxRepetitions);
    }

    @Override
    public void onClickListener(MaterialButton btnOption, int index) {
        if (listaSonidos.get(optionList.get(opcionCorrecta)).getNombre_sonido() == btnOption.getText()) {
            modificarPuntaje(true, null);
            btnOption.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce));
            setup();
        } else {
            modificarPuntaje(false, btnOption.getText().toString());
            btnOption.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_animation));
        }
    }

    boolean getRandomBoolean(){
        return randomBoolean.nextBoolean();
    }
}