package com.audiophile.t2m.music;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import java.util.ArrayList;
import java.util.Objects;

// TODO documentation
class MidiUtils {
    static final int WHOLE = 512, HALF = 256, QUARTER = 128, QUAVER = 64, SEMIQUAVER = 32, DREISEMQUAVER = 16;

    // A convenience method to add a note to the track on channel 0
    static void addNote(Track track, int startTick, int tickLength, int key, int velocity, int channel) throws InvalidMidiDataException {
        ShortMessage on = new ShortMessage();

        on.setMessage(ShortMessage.NOTE_ON, channel, key, velocity);
        ShortMessage off = new ShortMessage();
        off.setMessage(ShortMessage.NOTE_OFF, channel, key, velocity);
        track.add(new MidiEvent(on, startTick));
        track.add(new MidiEvent(off, startTick + tickLength));
    }

    static void ChangeInstrument(MyInstrument instrument, Track track, int channel, int tick) throws InvalidMidiDataException {

        ShortMessage sm = new ShortMessage();
        sm.setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument.midiValue, 0); //9 ==> is the channel 10.
        track.add(new MidiEvent(sm, tick));
    }

    static int ResToBPM(int res) {
        return (int) (res / 128.0) * 60;
    }

    static int BpmToRes(int bpm) {
        return (int) (bpm / 60.0) * 128;
    }

    static double TicksInSecs(int ticks, int resolution) {
        return (ticks * 60.0) / (resolution * ResToBPM(resolution));
    }

    static int SecsInTicks(double secs, int resolution) {
        return (int) secs * resolution * ResToBPM(resolution) / 60;
    }
}
