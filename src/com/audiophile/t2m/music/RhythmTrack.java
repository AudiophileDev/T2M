package com.audiophile.t2m.music;

import com.audiophile.t2m.Utils;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Track;
import java.util.Arrays;

//TODO documentation
public class RhythmTrack implements TrackGenerator {
    private Tempo tempo;
    private float[] avgWordLen;
    private MyInstrument instrument;

    RhythmTrack(MusicData musicData, float[] avgWordLen, MyInstrument instrument) {
        this.instrument = instrument;
        this.tempo = musicData.getTempo();
        this.avgWordLen = Utils.BlurData(avgWordLen, 3);


        // Find min and max avg word length in the text
        float max = 0,
                min = 100;
        for (float anAvgWordLen : avgWordLen) {
            if (anAvgWordLen > max)
                max = anAvgWordLen;
            if (anAvgWordLen < min)
                min = anAvgWordLen;
        }
        // Decrease all values by minimum and normalize them between 0 and 3
        max -= min;
        if (max > 0)
            for (int i = 0; i < avgWordLen.length; i++) {
                avgWordLen[i] -= min; // Decrease
                avgWordLen[i] = 3 * avgWordLen[i] / max; // Normalize
            }
    }

    /**
     * Rounds the given value to a full quaver
     *
     * @param val The value to round
     * @return A multiple of {@link MidiUtils#QUAVER}
     */
    private int roundToQuaver(float val) {
        return (((int) val) / MidiUtils.QUAVER) * MidiUtils.QUAVER;
    }


    @Override
    public void writeToTrack(Track track, int channel) {
        int length = ((MidiUtils.QUARTER * tempo.getAverageBpm()) / 60*15);
        int bass, snare, hiHat;
        int vel = 64;
        try {
            // Add bass
            int i = 0;
            for (int n = 0; n < length; n += bass) {
                MidiUtils.addNote(track, n, MidiUtils.QUARTER, 36, vel, channel);
                bass = roundToQuaver(MidiUtils.QUARTER * avgWordLen[(i += 3) % avgWordLen.length]);
            }

            //Add snare
            i = 0;
            for (int n = 0; n < length; n += snare) {
                MidiUtils.addNote(track, n, MidiUtils.QUARTER, 38, vel, channel);
                snare = roundToQuaver(MidiUtils.QUARTER * avgWordLen[(i += 3 + 1) % avgWordLen.length]);
            }

            //Add hi-hat
            i = 0;
            for (int n = 0; n < length; n += hiHat) {
                MidiUtils.addNote(track, n, MidiUtils.QUAVER, 42, vel, channel);
                hiHat = roundToQuaver(MidiUtils.QUARTER * avgWordLen[(i += 3 + 2) % avgWordLen.length]);
            }
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }
}
