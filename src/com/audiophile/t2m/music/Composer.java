package com.audiophile.t2m.musicGenerator;

import com.audiophile.t2m.text.TextAnalyser;

import javax.sound.midi.Sequence;

public class Composer {
    private RhythmChannel rhythmChannel;
    private MelodyChannel melodyChannel;
    private Tempo tempo;
    private String dynamic;
    private Harmony key;
    private TextAnalyser analysedText;

    /**
     * This class merges the different MIDI channels of rhythm, melody and sound effects
     * it also calculates the meta data of the music (dynamic, tempo, key)
     */
    public Composer(TextAnalyser analysedText) {
        this.analysedText = analysedText;
        this.rhythmChannel = new RhythmChannel(analysedText);
        this.key = new Harmony(analysedText.getSentences()[0].getWords()[0].getName());
        this.melodyChannel = new MelodyChannel(key, analysedText.getSentences());
        this.dynamic = dynamic; //forte, piano, cresc, decresc
        this.tempo = new Tempo(analysedText.getAvgWordLength());
    }

    public Sequence[] mergeAudioTracks() {
        return new Sequence[]{melodyChannel.getSequence(), rhythmChannel.getSequence()};
    }

    public int calcTemp() {
        return new Tempo(this.analysedText.getAvgWordLength()).averageBpm;
    }
}
