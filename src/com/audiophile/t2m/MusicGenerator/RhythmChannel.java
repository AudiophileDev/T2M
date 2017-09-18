package com.audiophile.t2m.MusicGenerator;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Sequence;

public class RhythmChannel {
    private MidiChannel midiChannel;
    private Sequence sequence;

    public RhythmChannel() {
    }

    public MidiChannel getMidiChannel() {
        return midiChannel;
    }

    public Sequence getSequence() {
        return sequence;
    }
}
