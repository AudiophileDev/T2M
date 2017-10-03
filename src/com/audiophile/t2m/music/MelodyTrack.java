package com.audiophile.t2m.music;

import com.audiophile.t2m.text.Sentence;
import com.audiophile.t2m.writer.TestWriter;
import com.sun.media.sound.MidiUtils;

import javax.sound.midi.*;

public class MelodyTrack implements TrackGenerator {
    private Sequence sequence;

    public MelodyTrack(Harmony tone, Sentence[] text) {

    }
    @Override
    public void writeToTrack(Track track) {
        TestWriter.writeTestMusic(track);
    }
}
