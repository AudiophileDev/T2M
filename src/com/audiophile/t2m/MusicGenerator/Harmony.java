package com.audiophile.t2m.MusicGenerator;

/**
 * creates chords
 * stores array of notes and the modus of the chords
 */
public class Harmony {
    String[] notes;
    int modus;

    /**
     * Create a harmony by chord name
     *
     * @param chordName
     */
    public Harmony(String chordName) {

    }

    /**
     * create harmony by single notes
     *
     * @param notes
     */
    public Harmony(String[] notes) {
        this.notes = notes;
    }

    public void intervall(String baseNote, String intervall) {

    }
}
