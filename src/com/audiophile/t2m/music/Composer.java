package com.audiophile.t2m.music;

import com.audiophile.t2m.text.Sentence;
import com.audiophile.t2m.text.TextAnalyser;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

public class Composer {
    private TrackGenerator[] trackGenerators;
    private Tempo tempo;

    /**
     * This class merges the different MIDI channels of rhythm, melody and sound effects
     * it also calculates the meta data of the music (dynamic, tempo, key)
     */
    public Composer(String text) {
        Sentence[] sentences = TextAnalyser.analyseSentences(text);
        float[] avgWordLen = TextAnalyser.getAvgWordLength(sentences);
        //TODO get key from tendencies
        Harmony key = new Harmony(sentences[0].getWords()[0].getName().substring(0, 1), Mode.Major, false);
        int dynamic = 64;
        this.tempo = new Tempo(avgWordLen);
        int i = (key.getMode() == Mode.Minor ? 2 : 1);
        this.tempo.setAverageBpm(this.tempo.getAverageBpm() / i);

        MusicData musicData = new MusicData(tempo, dynamic, key);

        this.trackGenerators = new TrackGenerator[2];
        this.trackGenerators[0] = new MelodyTrack(musicData, sentences, "noteMapping.csv", MyInstrument.Piano);
        this.trackGenerators[1] = new RhythmTrack(musicData, avgWordLen, MyInstrument.Drums);
    }

    /**
     * Merges the tracks of all music generators into one sequence
     *
     * @return A Sequence with all tracks
     */
    public Sequence getSequence() {
        Sequence sequence = null;
        try {
            sequence = new Sequence(Sequence.PPQ, tempo.getAverageBpm());
            trackGenerators[0].writeToTrack(sequence.createTrack(), 0);
            trackGenerators[1].writeToTrack(sequence.createTrack(), 9); // Channel 10 are drums
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return sequence;
    }
}
