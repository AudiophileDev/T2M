package com.audiophile.t2m.music;

import com.audiophile.t2m.text.Sentence;
import com.audiophile.t2m.text.TextAnalyser;
import com.audiophile.t2m.text.Word;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

public class Composer {
    private TrackGenerator[] trackGenerators;
    private Tempo tempo;
    private String dynamic;
    private Harmony key;
    private MusicData musicData;

    /**
     * This class merges the different MIDI channels of rhythm, melody and sound effects
     * it also calculates the meta data of the music (dynamic, tempo, key)
     */
    public Composer(String text) {

        // Analyse article
        Sentence[] sentences = TextAnalyser.analyseSentences(text);
        float[] avgWordLen = TextAnalyser.getAvgWordLength(sentences);
        Word.Tendency avgTendency = TextAnalyser.getAvgWordTendency(sentences);

        this.key = new Harmony(sentences[0].getWords()[0].getName().substring(0, 1), Modes.major, false);
        this.dynamic = dynamic; //forte, piano, cresc, decresc
        this.tempo = new Tempo(avgWordLen);
        int i = (this.key.getMode() == 3 ? 2 : 1);
        this.tempo.setAverageBpm(this.tempo.getAverageBpm() / i);
        this.musicData = new MusicData(tempo,
                "anyDynamic", /*forte, piano, cresc, decresc*/
                key);

        this.trackGenerators = new TrackGenerator[2];
        this.trackGenerators[0] = new MelodyTrack(musicData, sentences, "noteMapping.csv");
        this.trackGenerators[1] = new RhythmTrack(musicData, avgWordLen);
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
