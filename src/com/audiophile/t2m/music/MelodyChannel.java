package com.audiophile.t2m.music;

import com.audiophile.t2m.text.Sentence;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Sequence;

public class MelodyChannel {
    private MidiChannel midiChannel;
    private Sequence sequence;

    public MelodyChannel(Harmony tone, Sentence[] text) {

    }

    public MidiChannel getMidiChannel() {
        return midiChannel;
    }

    public Sequence getSequence() {
        return sequence;
    }
}
