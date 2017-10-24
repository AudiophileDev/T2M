package com.audiophile.t2m.music;

import com.audiophile.t2m.text.TextAnalyser;

import javax.sound.midi.*;
import java.util.Random;

public class RhythmTrack implements TrackGenerator {
    private MidiChannel midiChannel;
    private Sequence sequence;
    private Tempo tempo;

    public RhythmTrack(MusicData musicData, TextAnalyser analaysedText) {
        this.tempo = musicData.getTempo();
    }


    @Override
    public void writeToTrack(Track track, int channel) {
        int length =((128 * tempo.getAverageBpm()) / 4);
        Random ran = new Random(0);
        int bass = ran.nextInt(4)*MidiUtils.QUAVER;
        int snare = ran.nextInt(4)*MidiUtils.QUAVER;
        int hiHat = ran.nextInt(4)*MidiUtils.QUAVER;
        try {
            for (int n = bass; n < length; n += MidiUtils.QUARTER) {
                MidiUtils.addNote(track, n, MidiUtils.QUARTER, 36, 64, channel);
                n += MidiUtils.QUARTER;
            }
            for (int n = snare; n < length; n += MidiUtils.QUARTER) {
                MidiUtils.addNote(track, n, MidiUtils.QUARTER, 38, 64, channel);
                n += MidiUtils.QUARTER;
            }
            for (int n = hiHat; n < length; n += MidiUtils.QUAVER) {
                MidiUtils.addNote(track, n, MidiUtils.QUAVER, 42, 64, channel);
                n += MidiUtils.QUAVER;
            }
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }
}
