package com.audiophile.t2m.io;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 * @author Simon
 * Created on 03.10.2017.
 */
public class TestWriter {



    private static final int DAMPER_PEDAL = 64;

    private static final int DAMPER_ON = 127;

    private static final int DAMPER_OFF = 0;

    private static final int[] offsets = { // add these amounts to the base value
            // A B C D E F G
            -4, -2, 0, 1, 3, 5, 7};

    public static void writeTestMusic(Track track){

        ShortMessage sm = new ShortMessage();
        try {
            sm.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 1, 0);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
            return;
        }
        track.add(new MidiEvent(sm, 0));

        char[] notes = "C D E F G A H H H H ".toCharArray();

        int n = 0; // current character in notes[] array
        int t = 0; // time in ticks for the composition

        // These values persist and apply to all notes 'till changed
        int notelength = 64; // default to quarter notes
        int velocity = 64; // default to middle volume
        int basekey = 60; // 60 is middle C. Adjusted up and down by octave
        boolean sustain = false; // is the sustain pedal depressed?
        int numnotes = 0; // How many notes in current chord?

        while (n < notes.length) {
            char c = notes[n++];

            if (c == '+')
                basekey += 12; // increase octave
            else if (c == '-')
                basekey -= 12; // decrease octave
            else if (c == '>')
                velocity += 16; // increase volume;
            else if (c == '<')
                velocity -= 16; // decrease volume;
            else if (c == '/') {
                char d = notes[n++];
                if (d == '2')
                    notelength = 32; // half note
                else if (d == '4')
                    notelength = 16; // quarter note
                else if (d == '8')
                    notelength = 8; // eighth note
                else if (d == '3' && notes[n++] == '2')
                    notelength = 2;
                else if (d == '6' && notes[n++] == '4')
                    notelength = 1;
                else if (d == '1') {
                    if (n < notes.length && notes[n] == '6')
                        notelength = 4; // 1/16th note
                    else
                        notelength = 64; // whole note
                }
            } else if (c == 's') {
                sustain = !sustain;
                // Change the sustain setting for channel 0
                ShortMessage m = new ShortMessage();
                try {
                    m.setMessage(ShortMessage.CONTROL_CHANGE, 0, DAMPER_PEDAL, sustain ? DAMPER_ON
                            : DAMPER_OFF);
                } catch (InvalidMidiDataException e) {
                    e.printStackTrace();
                }
                track.add(new MidiEvent(m, t));
            } else if (c >= 'A' && c <= 'G') {
                int key = basekey + offsets[c - 'A'];
                if (n < notes.length) {
                    if (notes[n] == 'b') { // flat
                        key--;
                        n++;
                    } else if (notes[n] == '#') { // sharp
                        key++;
                        n++;
                    }
                }

                try {
                    addNote(track, t, notelength, key, velocity);
                } catch (InvalidMidiDataException e) {
                    e.printStackTrace();
                }
                numnotes++;
            } else if (c == ' ') {
                // Spaces separate groups of notes played at the same time.
                // But we ignore them unless they follow a note or notes.
                if (numnotes > 0) {
                    t += notelength;
                    numnotes = 0;
                }
            } else if (c == '.') {
                // Rests are like spaces in that they force any previous
                // note to be output (since they are never part of chords)
                if (numnotes > 0) {
                    t += notelength;
                    numnotes = 0;
                }
                // Now add additional rest time
                t += notelength;
            }

        }
    }

    // A convenience method to add a note to the track on channel 0
    private static void addNote(Track track, int startTick, int tickLength, int key, int velocity)
            throws InvalidMidiDataException {
        ShortMessage on = new ShortMessage();
        on.setMessage(ShortMessage.NOTE_ON, 0, key, velocity);
        ShortMessage off = new ShortMessage();
        off.setMessage(ShortMessage.NOTE_OFF, 0, key, velocity);
        track.add(new MidiEvent(on, startTick));
        track.add(new MidiEvent(off, startTick + tickLength));
    }
}