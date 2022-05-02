package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import com.example.myapplication.common.constants.Constantes;
import com.example.myapplication.databinding.ActivityConfiguracionBinding;
import com.example.myapplication.logica.EjercicioEscribirOyoActivity;
import com.example.myapplication.logica.EjercicioCompletarOracionSinOpActivity;
import com.example.myapplication.logica.EjerciciosOpcionesActivity;
import com.example.myapplication.room_database.palabras.Sound;
import com.example.myapplication.room_database.palabras.SoundRepository;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityConfig extends AppCompatActivity {

    String confSubDato;
    String confTipoEjercio;
    String confRuido;
    String modo;
    float confIntensidad = 0;
    private String categoriaSeleccionada = "";
    private ActivityConfiguracionBinding binding = null;

    ArrayAdapter<String> adapterCategoria, adapterRuido, adapterSubCategoria, adapterEjercicio;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfiguracionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final Toolbar toolbar = findViewById(R.id.toolbar_configuracion);
        toolbar.setTitle(R.string.config_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapterCategoria = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, Constantes.CATEGORIAS);
        adapterSubCategoria = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, Constantes.SUBCATEGORIAS_PALABRAS);
        binding.spinnerCategoria.setAdapter(adapterCategoria);
        binding.spinnerSubcategoria.setAdapter(adapterSubCategoria);
        adapterEjercicio = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, Constantes.TIPOS_EJERCICIOS_PALABRA);
        binding.spinnerExercise.setAdapter(adapterEjercicio);

        modo = getIntent().getStringExtra("Modo");

        final ArrayList<String> ruidosList = new ArrayList<>();
        adapterRuido = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, ruidosList);
        binding.spinnerNoise.setAdapter(adapterRuido);

        SoundRepository sr = new SoundRepository(getApplication());
        sr.getRuidosSounds().observe(this, new Observer<List<Sound>>() {
            @Override
            public void onChanged(List<Sound> sounds) {
                for (int i = 0; i < sounds.size(); i++) {
                    ruidosList.add(sounds.get(i).getNombre_sonido());
                }
                adapterRuido.notifyDataSetChanged();

            }
        });

        binding.spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                if (i != 0){
                    categoriaSeleccionada = adapterView.getItemAtPosition(i).toString();
                    binding.spinnerSubcategoria.setEnabled(true);
                    binding.spinnerSubcategoria.setSelection(0);
                    binding.spinnerExercise.setSelection(0);
                    switch (categoriaSeleccionada) {
                        //FONEMA, PALABRA, ORACIONES, CANCIONES, INSTRUMENTOS, ESTILOS_MUSICALES, VOCES_FAMILIARES
                        case (Constantes.PALABRA):
                            adapterSubCategoria = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, Constantes.SUBCATEGORIAS_PALABRAS);
                            adapterEjercicio = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, Arrays.asList(Constantes.TIPOS_EJERCICIOS_PALABRA));
                            break;

                        case (Constantes.ORACIONES):
                            adapterEjercicio = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, Constantes.TIPOS_EJERCICIOS_ORACIONES);
                            confSubDato = Constantes.ORACIONES;
                            break;
                    }
                    adapterEjercicio.notifyDataSetChanged();
                    binding.spinnerExercise.setAdapter(adapterEjercicio);
                } else {
                    binding.spinnerSubcategoria.setSelection(0);
                    binding.spinnerSubcategoria.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        binding.spinnerSubcategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                if (i != 0) {
                    confSubDato = adapterView.getItemAtPosition(i).toString();
                    binding.spinnerExercise.setEnabled(true);
                } else {
                    binding.spinnerExercise.setSelection(0);
                    binding.spinnerExercise.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.spinnerExercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                confTipoEjercio = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        binding.spinnerNoise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                confRuido = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        //RUIDO

        final Button btnStart = findViewById(R.id.btn_ejercicio);
        final TextView tvIntensidad = findViewById(R.id.tvIntensidad);
        final SeekBar seekBarIntensidad = findViewById(R.id.seekBar);

        final SwitchCompat switchRuido = findViewById(R.id.switchRuido);
        switchRuido.setChecked(false);

        switchRuido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.spinnerNoise.setVisibility(View.VISIBLE);
                    tvIntensidad.setVisibility(View.VISIBLE);
                    seekBarIntensidad.setVisibility(View.VISIBLE);
                    binding.svContainer.post(() -> binding.svContainer.fullScroll(View.FOCUS_DOWN));
                } else {
                    binding.spinnerNoise.setVisibility(View.GONE);
                    tvIntensidad.setVisibility(View.GONE);
                    seekBarIntensidad.setVisibility(View.GONE);
                    binding.svContainer.post(() -> binding.svContainer.fullScroll(View.FOCUS_UP));
                }
            }
        });

        seekBarIntensidad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private float getConverted(int intVal) {
                float floatVal = 0;
                floatVal = .1f * intVal;
                return floatVal;
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                confIntensidad = getConverted(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        //FINALIZADA LA CONFIGURACIÓN INICIO LA EJERCITACIÓN
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    Intent intent = new Intent(getApplicationContext(), EjerciciosOpcionesActivity.class);
                    if (!switchRuido.isChecked()) confRuido = "Sin Ruido";

                    if (confTipoEjercio.equals(Constantes.J_DISCRIMINAR)
                            || confTipoEjercio.equals(Constantes.J_IDENTIFICAR_TRES_OPCIONES)
                            || confTipoEjercio.equals(Constantes.J_IDENTIFICAR_CINCO_OPCIONES)
                            || confTipoEjercio.equals(Constantes.J_TODA_LA_CATEGORIA)) {
                        intent.putExtra("categoriaSeleccionada", categoriaSeleccionada);
                        intent.putExtra("subDato", confSubDato);
                        intent.putExtra("tipoRuido", confRuido);
                        intent.putExtra("intensidad", confIntensidad);
                        intent.putExtra("modo", modo);
                        intent.putExtra(getString(R.string.tipo_ejercicio), confTipoEjercio);
                        startActivity(intent);
                    }
                    switch (confTipoEjercio) {

                        case Constantes.J_ESCRIBIR_LO_QUE_OYO:
                            Intent intent2 = new Intent(getApplicationContext(), EjercicioEscribirOyoActivity.class);
                            if (!switchRuido.isChecked()) {
                                confRuido = "Sin Ruido";
                            }
                            intent2.putExtra("subDato", confSubDato);
                            intent2.putExtra("tipoRuido", confRuido);
                            intent2.putExtra("intensidad", confIntensidad);
                            intent2.putExtra("modo", modo);
                            startActivity(intent2);
                            break;


                        case Constantes.COMPLETAR_ORACION_SIN_OPCIONES:
                            Intent intent4 = new Intent(getApplicationContext(), EjercicioCompletarOracionSinOpActivity.class);
                            if (!switchRuido.isChecked()) {
                                confRuido = "Sin Ruido";
                            }
                            intent4.putExtra("subDato", confSubDato);
                            intent4.putExtra("tipoRuido", confRuido);
                            intent4.putExtra("intensidad", confIntensidad);
                            intent4.putExtra("modo", modo);
                            startActivity(intent4);
                            break;
                    }
                }

            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean validate() {
        boolean isValid = true;
        String message = "";
        if (binding.spinnerCategoria.getSelectedItemPosition() == 0) {
            message = "Error: debe seleccionar una categoría";
            isValid = false;
        }

        if (binding.spinnerSubcategoria.getSelectedItemPosition() == 0) {
            message = "Error: debe seleccionar una subcategoría";
        }

        if (binding.spinnerExercise.getSelectedItemPosition() == 0) {
            message = "Error: debe seleccionar un tipo de ejercicio";
            isValid = false;
        }
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        return isValid;
    }
}
