package com.example.myapplication.controllers;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

public class ReproductorDeAudioController implements MediaPlayer.OnCompletionListener {

    float intensidad;

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.reset();
    }

    private static class SingletonHolder {
        private static final ReproductorDeAudioController INSTANCE = new ReproductorDeAudioController();
    }

    public static ReproductorDeAudioController getmInstance() {
        return SingletonHolder.INSTANCE;
    }

    public float getIntensidad() {
        return intensidad;
    }

    public void setIntensidad(float intensidad) {
        this.intensidad = intensidad;
    }

    public double getIntensidadPorcentual() {
        return Math.floor(intensidad * 100);
    }

    public void startSoundNoNoise(String filename, Context context) {
        AssetFileDescriptor afd = null;
        try {
            afd = context.getResources().getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaPlayer player = new MediaPlayer();
        try {
            assert afd != null;
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.start();
    }


    public void startSoundWithNoise(String filename, String rutaRuido, float intensidad, Context context) {
        AssetFileDescriptor afd = null;
        try {
            afd = context.getResources().getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AssetFileDescriptor afdRuido = null;
        try {
            afdRuido = context.getResources().getAssets().openFd(rutaRuido);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaPlayer mpSound = new MediaPlayer();
        MediaPlayer mpRuido = new MediaPlayer();


        try {
            assert afdRuido != null;
            mpRuido.setDataSource(afdRuido.getFileDescriptor(), afdRuido.getStartOffset(), afdRuido.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mpRuido.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mpRuido.setVolume(intensidad, intensidad);
        mpRuido.start();

        try {
            assert afd != null;
            mpSound.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mpSound.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mpSound.start();
        try {
            Thread.sleep(mpSound.getDuration() + 200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mpRuido.stop();

    }

    MediaPlayer mpSound = new MediaPlayer();
    MediaPlayer mpConnectorSound = new MediaPlayer();
    MediaPlayer mpEndConnector = new MediaPlayer();

    public void sentenceWithConnectors(String startConnectorPath, String endConnectorPath, String soundPath, boolean connectorAtStart, Context context) {
        try {
            mpSound.setOnCompletionListener(this);
            mpConnectorSound.setOnCompletionListener(this);
            mpEndConnector.setOnCompletionListener(this);
            AssetFileDescriptor afdSound = context.getResources().getAssets().openFd(soundPath);
            AssetFileDescriptor afdConnectorSound = context.getResources().getAssets().openFd(startConnectorPath);
            AssetFileDescriptor afdEndConnectorSound;
            mpConnectorSound.setDataSource(afdConnectorSound.getFileDescriptor(), afdConnectorSound.getStartOffset(), afdConnectorSound.getLength());
            mpConnectorSound.prepare();
            mpSound.setDataSource(afdSound.getFileDescriptor(), afdSound.getStartOffset(), afdSound.getLength());
            mpSound.prepare();
            if (connectorAtStart) {
                mpConnectorSound.start();
                Thread.sleep(mpConnectorSound.getDuration());
                mpSound.start();
                if (endConnectorPath != null) {
                    afdEndConnectorSound = context.getResources().getAssets().openFd(endConnectorPath);
                    mpEndConnector.setDataSource(afdEndConnectorSound.getFileDescriptor(), afdEndConnectorSound.getStartOffset(), afdEndConnectorSound.getLength());
                    mpEndConnector.prepare();
                    Thread.sleep(mpSound.getDuration());
                    mpEndConnector.start();
                }
            } else {
                mpSound.start();
                Thread.sleep(mpSound.getDuration());
                mpConnectorSound.start();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startSoundOraciones(String filename, Context context) {
        AssetFileDescriptor afd = null;
        try {
            afd = context.getResources().getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaPlayer player = new MediaPlayer();
        try {
            assert afd != null;
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int duration = player.getDuration();
        player.start();
        try {
            Thread.sleep(duration / 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        player.stop();
    }


    public void startSoundOracionesNoise(String filename, String rutaRuido, float intensidad, Context context) {
        AssetFileDescriptor afd = null;
        try {
            afd = context.getResources().getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AssetFileDescriptor afdRuido = null;
        try {
            afdRuido = context.getResources().getAssets().openFd(rutaRuido);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaPlayer mpSound = new MediaPlayer();
        MediaPlayer mpRuido = new MediaPlayer();


        try {
            assert afdRuido != null;
            mpRuido.setDataSource(afdRuido.getFileDescriptor(), afdRuido.getStartOffset(), afdRuido.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mpRuido.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mpRuido.setVolume(intensidad, intensidad);
        mpRuido.start();

        try {
            assert afd != null;
            mpSound.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mpSound.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int duration = mpSound.getDuration();
        mpSound.start();
        try {
            Thread.sleep(duration / 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mpRuido.stop();
        mpSound.stop();

    }


}
