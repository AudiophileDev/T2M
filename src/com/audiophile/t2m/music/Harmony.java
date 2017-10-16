package com.audiophile.t2m.music;

import java.util.ArrayList;
import java.util.Objects;


public class Harmony {
    private ArrayList<String> notesName;
    private ArrayList<Integer> notesNumber;
    private String chord;
    private char baseNoteChar;

    private int baseNoteMidi;

    private boolean sept;

    private String mode;

    /**
     * Create a harmony by chord name and modus
     * <p>
     * sets if sept chord or if its the base of a piece
     *
     * @param baseNote tonic of the chord
     * @param mode     mode of the harmony: either "maj" for a major chord or "min" for a minor chord
     * @param sept     sets if it is a sept chord or not
     * @param base     sets if it is the base key of the piece
     */
    public Harmony(char baseNote, String mode, boolean sept, boolean base) {
        this.sept = sept;
        this.mode = mode;
        this.notesName = new ArrayList<String>();
        this.notesNumber = new ArrayList<Integer>();
        //if (base)
        this.baseNoteChar = maskNote(baseNote);
        //TODO add masking for base chords
        //else this.baseNoteChar = baseNote;
        this.baseNoteMidi = this.baseNoteChar - 7; // annotation: -7 only works for c
        //TODO prove compatibility with noteMapping.csv
        this.chord = "";
        buildChord();
        //System.out.println(this.chord);
    }

    /**
     * builds a chord in numbers ready for direct usage in midi
     */

    //TODO: clear if unnecessary
    public void buildChord() {
        this.notesNumber.add(0, (int) baseNoteMidi);
        this.notesName.add(0, String.valueOf(baseNoteChar));
        chord = lookUpChord();
        for (int i = 1; i < 3; i++) {
            try {
                this.notesNumber.add(i, notesNumber.get(i - 1) + 2);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * builds the chord in a string which could be used for the track writer
     *
     * @return the chord in the form of: basenote concatenated with its third and fifth and optional seventh
     */
    public String lookUpChord() {
        String chord = String.valueOf(baseNoteChar);
        String threaAndFive = "";
        switch (baseNoteChar) {
            case 'C':
                threaAndFive = (Objects.equals(this.mode, "maj") ? "E" : "Eb") + "G";
                if (this.sept) threaAndFive = threaAndFive.concat("Hb");
                break;
            case 'F':
                threaAndFive = (Objects.equals(this.mode, "maj") ? "A" : "Ab") + "C";
                if (this.sept) threaAndFive = threaAndFive.concat("Eb");
                break;
            case 'G':
                threaAndFive = (Objects.equals(this.mode, "maj") ? "H" : "Hb") + "D";
                if (this.sept) threaAndFive = threaAndFive.concat("F");
                break;
            case 'D':
                threaAndFive = (Objects.equals(this.mode, "maj") ? "F" : "F#") + "A";
                if (this.sept) threaAndFive = threaAndFive.concat("C");
                break;
            case 'H':
                threaAndFive = (Objects.equals(this.mode, "maj") ? "D" : "D#") + "F#";
                if (this.sept) threaAndFive = threaAndFive.concat("A");
                break;
            default:
                break;

        }
        return chord.concat(threaAndFive);
    }

    /**
     * masks any letter to a basenote
     * basenote is either c, f or g for simplicity reasons
     * this limits the music output to three different base keys
     *
     * @param baseNote tonic of the chord
     * @return the masked basenote
     */
    public char maskNote(char baseNote) {
        String note = "CFG";
        if (!note.contains(String.valueOf(baseNote)))
            baseNote = (note.toCharArray()[baseNote % note.length()]);
        return (char) (baseNote);
    }

    public String getChord() {
        return chord;
    }

    public char getBaseNoteChar() {
        return baseNoteChar;
    }

    public String getMode() {
        return mode;
    }

    public boolean getSept() {
        return sept;
    }

    public int getBaseNoteMidi() {
        return baseNoteMidi;
    }
}
