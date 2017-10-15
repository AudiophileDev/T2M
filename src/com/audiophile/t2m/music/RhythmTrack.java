package com.audiophile.t2m.music;

import com.audiophile.t2m.text.TextAnalyser;

import javax.sound.midi.*;

public class RhythmTrack implements TrackGenerator {
    private MidiChannel midiChannel;
    private Sequence sequence;

    public RhythmTrack(TextAnalyser analaysedText) {
    }


    @Override
    public void writeToTrack(Track track, int channel) {
        /*try {
            int n=0;
            for(int j=0;j<128;j++) {
                MidiUtils.ChangeInstrument(j, track, channel, n);
                for (int i = 40; i < 50; i++)
                    MidiUtils.addNote(track, n+=64, 64, i, 64, channel);
            }
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }*/
    }
}
