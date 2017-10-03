package com.audiophile.t2m.music;

import java.util.Arrays;

/**
 * creates chords
 * stores array of notesName and the modus of the chords
 */
public class Harmony {
    String[] notesName;
    int[] notesNumber;
    int modus;

    /**
     * Create a harmony by chord name
     *
     * @param chordName
     */
    public Harmony(String chordName) {
        this.notesName = new String[5];
        this.notesName[0] = chordName.substring(0, 1);
        this.notesNumber = new int[5];
        this.notesNumber[0] = (int) this.notesName[0].toCharArray()[0];
        for (int i = 1; i < chordName.length(); i++) {
            try {
                this.notesName[i] = Character.toString((char) (notesNumber[0] + 2));
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        System.out.println(chordName + "" + Arrays.toString(this.notesName));
    }

    /**
     * create harmony by single notesName
     *
     * @param notesName
     */
    public Harmony(String[] notesName) {
        this.notesName = notesName;
    }

    public void intervall(String baseNote, String intervall) { }
}
