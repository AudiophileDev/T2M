package com.audiophile.t2m.music;

import com.audiophile.t2m.text.TextAnalyser;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Composer {
    private ArrayList<TrackGenerator> trackGenerators;
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
        this.key = new Harmony(analysedText.getSentences()[0].getWords()[0].getName());
        this.dynamic = dynamic; //forte, piano, cresc, decresc
        this.tempo = new Tempo(analysedText.getAvgWordLength());

        this.trackGenerators = new ArrayList<>(2);
        this.trackGenerators.add(new MelodyTrack(key, analysedText.getSentences()));
        this.trackGenerators.add(new RhythmTrack(analysedText));
    }

    public Sequence getSequence() {
        Sequence sequence = null;
        try {
            sequence = new Sequence(Sequence.PPQ, 144);
            for (TrackGenerator t : trackGenerators)
                t.writeToTrack(sequence.createTrack());
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return sequence;
    }

    public int calcTemp() {
        return new Tempo(this.analysedText.getAvgWordLength()).averageBpm;
    }
}
