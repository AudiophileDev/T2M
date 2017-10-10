package com.audiophile.t2m.music;

import com.audiophile.t2m.text.Sentence;

import javax.sound.midi.*;

public class MelodyTrack implements TrackGenerator {
    public MelodyTrack(Harmony tone, Sentence[] text) {
    }
    @Override
    public void writeToTrack(Track track,int channel) {
        try {
            MidiUtils.ChangeInstrument(0, track,channel,0);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        //MidiUtils.writeTestMusic(track,channel,"CEG FAC GHD C D E F G G A A A G F F E E D D ");

    }
}
