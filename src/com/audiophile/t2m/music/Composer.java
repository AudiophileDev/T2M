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
     * Defines if effects are added to the music
     */
    private boolean noEffects;

    /**
     * The title name of the generated music.
     * Holds various music about the music like ensemble,key and article names
     */
    public String title;

    /**
     * This class merges the different MIDI channels of rhythm, melody and sound effects
     * it also calculates the meta data of the music (dynamic, tempo, key)
     */
    public Composer(Sentence[] sentences, boolean noEffects, Ensemble ensemble) {
        this.noEffects = noEffects;
        float[] avgWordLen = TextAnalyser.getAvgWordLength(sentences);
        Word.Tendency avgTendency = TextAnalyser.getAvgWordTendency(sentences);

        Harmony key = new Harmony(sentences[0].getWords()[0].getName().substring(0, 1), avgTendency.ordinal() < Word.Tendency.Neutral.ordinal() ? Mode.Minor : Mode.Major, false);
        Dynamic dynamic = new Dynamic(avgTendency.ordinal() * 32, Utils.BlurData(avgWordLen, 10));
        this.tempo = new Tempo(avgWordLen);
        int i = (key.getMode() == Mode.Minor ? 2 : 1);
        //this.tempo.averageBpm = this.tempo.averageBpm / 2;

        MusicData musicData = new MusicData(tempo, dynamic, key);
        System.out.println("Tempo: " + tempo.averageBpm + " BPM");
        System.out.println("Resolution: " + tempo.resolution + " PPQ");
        this.trackGenerators = new TrackGenerator[noEffects ? 2 : 3];
        this.trackGenerators[0] = new MelodyTrack(musicData, sentences, "noteMapping.csv", ensemble);
        this.trackGenerators[1] = new RhythmTrack(musicData, avgWordLen);
        if (!noEffects)
            this.trackGenerators[2] = new EffectTrack(sentences, tempo);
        this.title = "in " + Harmony.quintCycle.get(key.getBaseNoteMidi() % 12 + 60) + "-" + key.getMode().toString() + ", played by a " + ensemble.toString() + "-Ensemble";
    }

    /**
     * Merges the tracks of all music generators into one sequence
     *
     * @return A Sequence with all tracks
     */
    public Sequence getSequence() {
        Sequence sequence = null;
        try {
            sequence = new Sequence(Sequence.PPQ, tempo.resolution);
            trackGenerators[0].writeToTrack(sequence.createTrack(), 0);
            trackGenerators[1].writeToTrack(sequence.createTrack(), 9); // Channel 10 are drums
            if (!noEffects)
                trackGenerators[2].writeToTrack(sequence.createTrack(), 2);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return sequence;
    }
}
