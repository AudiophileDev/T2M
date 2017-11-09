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
     * Mapping from MidiValue to corresponding value.
     */
    static final Map<Integer, String> quintCycle = Collections.unmodifiableMap(
            new HashMap<Integer, String>() {{
                put(60, "C");
                put(61, "C#");// Db
                put(62, "D");
                put(63, "D#"); //Eb
                put(64, "E"); //Fb
                put(65, "F");
                put(66, "F#");// Gb
                put(67, "G");
                put(68, "G#");//Ab
                put(69, "A");
                put(70, "A#");// B
                put(71, "H");
            }}
    );
    /**
     * Mapping from Letter to MidiValue.
     */
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
    /**
     * The mode of the piece
     */
    private Mode mode;

    /**
     * Create a harmony by chord name and mode
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

    /**
     * @param harmony the preceding harmony
     * @param pitch   pitching the harmony to dominant,
     */
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
        return chords.get(baseNote) + 12;
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

