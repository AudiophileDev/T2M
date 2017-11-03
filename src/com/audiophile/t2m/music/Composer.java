package com.audiophile.t2m.music;

import com.audiophile.t2m.Utils;
import com.audiophile.t2m.text.Sentence;
import com.audiophile.t2m.text.TextAnalyser;
import com.audiophile.t2m.text.Word;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

/**
 * This class puts together all tracks and composes them to one sequence, which can be exported and played.
 */
public class Composer {
    /**
     * List of music generators (e.g. melody,rhythm)
     */
    private TrackGenerator[] trackGenerators;
    /**
     * The global tempo for the music
     */
    private Tempo tempo;

    /**
     * This class merges the different MIDI channels of rhythm, melody and sound effects
     * it also calculates the meta data of the music (dynamic, tempo, key)
     *
     * @param text The article as plain text
     */
    public Composer(String text) {
        Sentence[] sentences = TextAnalyser.analyseSentences(text);
        float[] avgWordLen = TextAnalyser.getAvgWordLength(sentences);
        Word.Tendency avgTendency = TextAnalyser.getAvgWordTendency(sentences);

        //TODO get key from tendencies
        Harmony key = new Harmony(sentences[0].getWords()[0].getName().substring(0, 1), avgTendency.ordinal() < Word.Tendency.Neutral.ordinal() ? Mode.Minor : Mode.Major, false);
        Dynamic dynamic = new Dynamic(avgTendency.ordinal() * 32, Utils.BlurData(avgWordLen, 10));
        this.tempo = new Tempo(avgWordLen);
        int i = (key.getMode() == Mode.Minor ? 2 : 1);
        this.tempo.setAverageBpm(this.tempo.getAverageBpm() / 2);
        //this.tempo.setAverageBpm(200);

        MusicData musicData = new MusicData(tempo, dynamic, key);
        System.out.println("Tempo: " + tempo.getAverageBpm() + "BPM");
        this.trackGenerators = new TrackGenerator[2];
        this.trackGenerators[0] = new MelodyTrack(musicData, sentences, "noteMapping.csv", MyInstrument.Piano);
        this.trackGenerators[1] = new RhythmTrack(musicData, avgWordLen, MyInstrument.DrumSet);
    }

    /**
     * Merges the tracks of all music generators into one sequence
     *
     * @return A Sequence with all tracks
     */
    public Sequence getSequence() {
        Sequence sequence = null;
        try {
            sequence = new Sequence(Sequence.PPQ, (int) Math.round((144 * 10) / (tempo.getAverageBpm() * 10.0) * 144));
            System.out.println(
                    Math.round((144 * 10) / (tempo.getAverageBpm() * 10.0) * 144)
            );
            trackGenerators[0].writeToTrack(sequence.createTrack(), 0);
            trackGenerators[1].writeToTrack(sequence.createTrack(), 9); // Channel 10 are drums
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return sequence;
    }
}
