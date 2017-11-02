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

    static void addChord(Track track, int startTick, int tickLength, ArrayList<Integer> chordNotes, int octave, int velocity, int channel, boolean arpeggio) throws InvalidMidiDataException {
        for (Integer note : chordNotes) {
            if (arpeggio)
                startTick += 16;
            addNote(track, startTick, tickLength, note + octave * 12, velocity, channel);
        }
    }

    static void addPowerChord(Track track, int startTick, int tickLength, ArrayList<Integer> chordNotes, int octave, int velocity, int channel) throws InvalidMidiDataException {
        for (Integer note : chordNotes) {
            if (!Objects.equals(note, chordNotes.get(2)) || Objects.equals(note, chordNotes.get(0)))
                addNote(track, startTick, tickLength, note + octave * 12, velocity, channel);
        }
    }

    static void ChangeInstrument(int instrument, Track track, int channel, int tick) throws InvalidMidiDataException {

        ShortMessage sm = new ShortMessage();
        sm.setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0); //9 ==> is the channel 10.
        track.add(new MidiEvent(sm, tick));
    }
}
