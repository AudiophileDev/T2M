package com.audiophile.t2m.music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Harmony {
    /**
     * values of the chords in midi
     */
    private ArrayList<Integer> notesNumber;
    /**
     * the base note as an integer value, ready to use
     */
    private int baseNoteMidi;
    /**
     * if true, the chord is a seventh chord
     */
    private boolean sept;
    /**
     * a value of 3 or 4 indicating the mode
     */
    private Mode mode;

    private static final Map<String, Integer> chords = Collections.unmodifiableMap(
            new HashMap<String, Integer>() {{
                put("C", 60);
                put("D", 62);
                put("E", 64);
                put("F", 65);
                put("G", 67);
                put("A", 69);
                put("B", 70);
                put("H", 71);
            }});

    private static final char[] quintCycle = {
            60,//C
            61,//C#, Db
            62,//D
            63,//D#, Eb
            64,//E, Fb
            65,//F
            66,//F#, Gb
            67,//G
            68,//G# Ab
            69,//A
            70,//A#, B
            71//H
    };

    /**
     * Create a harmony by chord name and modus
     * <p>
     * sets if sept chord or if its the base of a piece
     *
     * @param baseNote tonic of the chord
     * @param mode     mode of the harmony: either "maj" for a major chord or "min" for a minor chord
     * @param sept     sets if it is a sept chord or not
     */
    Harmony(String baseNote, Mode mode, boolean sept) {
        this.sept = sept;
        this.mode = mode;
        this.notesNumber = new ArrayList<>();
        this.baseNoteMidi = maskNote(baseNote);
        buildChord();
    }

    Harmony(Harmony harmony, int pitch) {
        this.sept = harmony.sept;
        this.mode = harmony.mode;
        this.notesNumber = new ArrayList<>();
        this.baseNoteMidi = harmony.baseNoteMidi + pitch;
        buildChord();
    }

    /**
     * builds a chord in numbers ready for direct usage in midi
     */

    private void buildChord() {
        this.notesNumber.add(0, baseNoteMidi);
        int n = this.sept ? 4 : 3;
        for (int i = 1; i < n; i++) {
            switch (i) {
                case 1:
                    this.notesNumber.add(i, this.baseNoteMidi + this.mode.third);
                    break;
                case 2:
                    this.notesNumber.add(i, this.baseNoteMidi + 7);
                    break;
                case 3:
                    this.notesNumber.add(i, this.baseNoteMidi + 10);
                default:
            }
        }
    }

    /**
     * masks any letter to a basenote between a and h
     * this limits thnotee music output to three different base keys
     *
     * @param baseNote tonic of the chord
     * @return the masked basenote
     */
    private int maskNote(String baseNote) {
        String note = "CDEFGAHBC";
        if (!note.contains(baseNote))
            baseNote = String.valueOf(note.toCharArray()[baseNote.toCharArray()[0] % note.length()]);
        return chords.get(baseNote);
    }

    Mode getMode() {
        return mode;
    }

    public boolean getSept() {
        return sept;
    }

    int getBaseNoteMidi() {
        return baseNoteMidi;
    }

    ArrayList<Integer> getNotesNumber() {
        return notesNumber;
    }

    public void setBaseNoteMidi(int baseNoteMidi) {
        this.baseNoteMidi = baseNoteMidi;
    }
}

