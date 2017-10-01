package com.audiophile.t2m.musicGenerator;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Sequence;

public class MelodyChannel {
    private MidiChannel midiChannel;
    private Sequence sequence;

    public MelodyChannel() {

    }

    public MidiChannel getMidiChannel() {
        return midiChannel;
    }

    public Sequence getSequence() {
        return sequence;
    }
}
