package com.audiophile.t2m.music;

import com.audiophile.t2m.Utils;
import com.audiophile.t2m.text.TextAnalyser;

import javax.sound.midi.*;
import java.util.Arrays;
import java.util.Random;

public class RhythmTrack implements TrackGenerator {
    private MidiChannel midiChannel;
    private Sequence sequence;
    private Tempo tempo;
    private float[] test;

    public RhythmTrack(MusicData musicData, TextAnalyser analaysedText) {
        this.tempo = musicData.getTempo();
        test = analaysedText.getAvgWordLength();
        Utils.BlurData(test, 3);
        float max = 0,
                min = 100;
        for (int i = 0; i < test.length; i++) {
            if (test[i] > max)
                max = test[i];
            if (test[i] < min)
                min = test[i];
        }
        max -= min;
        System.out.println(min + " " + max);
        if (max > 0)
            for (int i = 0; i < test.length; i++) {
                test[i] -= min;
                test[i] = 3 * test[i] / max;

            }
    }

    private int roundToQuaver(float val) {
        int res = (((int) val) / MidiUtils.QUAVER) * MidiUtils.QUAVER;
        System.out.println(res);
        return res;
    }


    @Override
    public void writeToTrack(Track track, int channel) {
        int length = ((128 * tempo.getAverageBpm()) / 4);
        Random ran = new Random();
        int bass, snare, hiHat;
        int vel = 32;
        try {
            int i = 0;
            for (int n = 0; n < length; n += bass) {
                MidiUtils.addNote(track, n, MidiUtils.QUARTER, 36, vel, channel);
                bass = roundToQuaver(MidiUtils.QUARTER * test[(i += 3) % test.length]);
            }
            i = 0;
            for (int n = 0; n < length; n += snare) {
                MidiUtils.addNote(track, n, MidiUtils.QUARTER, 38, vel, channel);
                snare = roundToQuaver(MidiUtils.QUARTER * test[(i += 3 + 1) % test.length]);
            }
            i = 0;
            for (int n = 0; n < length; n += hiHat) {
                MidiUtils.addNote(track, n, MidiUtils.QUAVER, 42, vel, channel);
                hiHat = roundToQuaver(MidiUtils.QUARTER * test[(i += 3 + 2) % test.length]);
            }
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }
}
