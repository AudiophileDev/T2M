package com.audiophile.t2m.music;

import com.audiophile.t2m.text.TextAnalyser;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Sequence;

public class RhythmChannel extends MusicChannel{
    private MidiChannel midiChannel;
    private Sequence sequence;

    public RhythmChannel(MusicData musicData, TextAnalyser textBase) {
        super(musicData, textBase);
    }

    public MidiChannel getMidiChannel() {
        return midiChannel;
    }

    public Sequence getSequence() {
        return sequence;
    }
}
