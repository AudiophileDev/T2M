package com.audiophile.t2m.music;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A class that holds a collection of static methods for creating midi sequences
 */
class MidiUtils {
    /**
     * Fixed note lengths for music
     */
    static final int WHOLE = 512, HALF = 256, QUARTER = 128, QUAVER = 64, SEMIQUAVER = 32, DREISEMQUAVER = 16;

    /**
     * Adds a note to a track at the given time
     * @param track The track to write to
     * @param startTick The tick when the note should start playing
     * @param tickLength The length of the note
     * @param key The note as int
     * @param velocity The loudness of the note
     * @param channel The channel on which the note is played
     * @throws InvalidMidiDataException If note could not be added to track
     */
    static void addNote(Track track, int startTick, int tickLength, int key, int velocity, int channel) throws InvalidMidiDataException {
        ShortMessage on = new ShortMessage();

        on.setMessage(ShortMessage.NOTE_ON, channel, key, velocity);
        ShortMessage off = new ShortMessage();
        off.setMessage(ShortMessage.NOTE_OFF, channel, key, velocity);
        track.add(new MidiEvent(on, startTick));
        track.add(new MidiEvent(off, startTick + tickLength));
    }

    /**
     * Changes the instrument in a given channel on a specific tick
     * @param instrument The new instrument
     * @param track The track to change the instrument for
     * @param channel The channel to change the instrument on
     * @param tick The time as tick when the instrument should change
     * @throws InvalidMidiDataException If instrument could not change
     */
    static void ChangeInstrument(MyInstrument instrument, Track track, int channel, int tick) throws InvalidMidiDataException {
        ShortMessage sm = new ShortMessage();
        sm.setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument.midiValue, 0); //9 ==> is the channel 10.
        track.add(new MidiEvent(sm, tick));
    }

    /**
     * Converts a resolution to BPMs
     * @param res The resolution to convert
     * @return Beats per minute
     */
    private static int ResToBPM(int res) {
        return (int) Math.sqrt(128 * res);
    }

    /**
     * Converts Beats per minute to a resolution
     * @param bpm The BPMs to convert
     * @return Resolution
     */
    static int BpmToRes(int bpm) {
        return (int) ((bpm / 128.0) * bpm);
    }

    /**
     * Converts ticks into seconds
     * @param ticks The ticks to convert
     * @param resolution The tick resolution
     * @return Amount of seconds
     */
    static double TicksInSecs(int ticks, int resolution) {
        return (ticks * 60.0) / (resolution * ResToBPM(resolution));
    }

    /**
     * Converts seconds into ticks.
     * @param secs The seconds to convert.
     * @param resolution The resolution of the ticks
     * @return The ticks
     */
    static int SecsInTicks(double secs, int resolution) {
        return (int) secs * resolution * ResToBPM(resolution) / 60;
    }
}
