package com.audiophile.t2m.music;

import com.audiophile.t2m.text.TextAnalyser;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

public class Composer {
    private RhythmChannel rhythmChannel;
    private MelodyChannel melodyChannel;
    private MusicData musicData;
    private TextAnalyser analysedText;

    /**
     * This class merges the different MIDI channels of rhythm, melody and sound effects
     * it also calculates the meta data of the music (dynamic, tempo, key)
     */
    public Composer(TextAnalyser analysedText) {
        this.analysedText = analysedText;

        //set music metadata: key, tempo, dynamic
        this.musicData = new MusicData(new Tempo(analysedText.getAvgWordLength()),
                "anyDynamic", /*forte, piano, cresc, decresc*/
                new Harmony(analysedText.getSentences()[0].getWords()[0].getName().substring(0, 1)));
        //TODO how many different keys? how will they be set
        //TODO clarifying: just base tempo or absolute siz

        //create rhythm, melody and effect tracks
        this.rhythmChannel = new RhythmChannel(musicData, analysedText);
        this.melodyChannel = new MelodyChannel(musicData, analysedText);
        //TODO which arguments do the music channels need


        //TODO add effects
        testSynth(0, musicData.getKey().getNotesNumber()[0]);
    }

    public  void testSynth(int instrNumber, int noteNumber) {
        Synthesizer s = null;
        try {
            s = MidiSystem.getSynthesizer();
            s.open();
            MidiChannel[] mc = s.getChannels();
            Instrument[] instruments = s.getDefaultSoundbank().getInstruments();
            mc[instrNumber % 17].noteOn(noteNumber, 600);
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
