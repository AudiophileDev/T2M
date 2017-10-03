package com.audiophile.t2m.music;

import com.audiophile.t2m.text.TextAnalyser;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

public class RhythmTrack implements TrackGenerator{
    private MidiChannel midiChannel;
    private Sequence sequence;

    public RhythmTrack(TextAnalyser analaysedText) {
    }


    @Override
    public void writeToTrack(Track track) {

    }
}
