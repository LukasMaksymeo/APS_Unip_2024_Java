package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

    Clip[] clips = new Clip[30]; // Armazena todos os clipes de áudio
    URL[] soundURL = new URL[30];
    float currentVolume = -18;
    FloatControl[] volumeControls = new FloatControl[30]; // Controle de volume para cada clipe de áudio

    public Sound() {
        soundURL[0] = getClass().getResource("/sound/inicio.wav");
        soundURL[1] = getClass().getResource("/sound/mover.wav");
        soundURL[2] = getClass().getResource("/sound/select.wav");
        soundURL[3] = getClass().getResource("/sound/som.wav");
        soundURL[4] = getClass().getResource("/sound/texto.wav");
        soundURL[5] = getClass().getResource("/sound/moveringame.wav");
        soundURL[6] = getClass().getResource("/sound/certo.wav");
        soundURL[7] = getClass().getResource("/sound/plasticoinicio.wav");
        soundURL[8] = getClass().getResource("/sound/plasticoloop.wav");
        soundURL[9] = getClass().getResource("/sound/papelinicio.wav");
        soundURL[10] = getClass().getResource("/sound/papelloop.wav");
        soundURL[11] = getClass().getResource("/sound/vidroinicio.wav");
        soundURL[12] = getClass().getResource("/sound/vidroloop.wav");
        soundURL[13] = getClass().getResource("/sound/metalinicio.wav");
        soundURL[14] = getClass().getResource("/sound/metalloop.wav");
        soundURL[15] = getClass().getResource("/sound/bossinicio.wav");
        soundURL[16] = getClass().getResource("/sound/bossloop.wav");
        soundURL[17] = getClass().getResource("/sound/bossfinal.wav");
        soundURL[18] = getClass().getResource("/sound/cutscene.wav");
        soundURL[19] = getClass().getResource("/sound/win.wav");
        soundURL[20] = getClass().getResource("/sound/hahaha.wav");

        for (int i = 0; i < soundURL.length; i++) {
            if (soundURL[i] != null) {
                setFile(i);
            }
        }
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);   
            clips[i] = AudioSystem.getClip();
            clips[i].open(ais);
            volumeControls[i] = (FloatControl) clips[i].getControl(FloatControl.Type.MASTER_GAIN); // Armazena o controle de volume para este clipe de áudio
            volumeControls[i].setValue(currentVolume); // Define o volume inicial para este clipe de áudio
        } catch (Exception e) {
            // Tratamento de exceção
        }
    }

    public void play(int i) {
        clips[i].start();
    }

    public void loop(int i) {
        clips[i].loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(int i) {
        clips[i].stop();
    }

    public void volumeUp() {
        currentVolume += 8.0f;
        if(currentVolume > 6.0f) {
            currentVolume = 6.0f;
        }
        updateVolume();
    }

    public void volumeDown() {
        currentVolume -= 8.0f;
        if (currentVolume < -80.0f) {
            currentVolume = -80.0f;
        }
        updateVolume();
    }

    private void updateVolume() {
        for (FloatControl fc : volumeControls) {
            if (fc != null) {
                fc.setValue(currentVolume);
            }
        }
    }
    
    public void playMusic(int i) {
        play(i);
        loop(i);
    }

    public void stopMusic(int i) {
        stop(i);
    }

    public void playSE(int i) {
        play(i);
    }
}
