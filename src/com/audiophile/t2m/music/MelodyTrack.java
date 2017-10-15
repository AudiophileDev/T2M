package com.audiophile.t2m.music;

import com.audiophile.t2m.Utils;
import com.audiophile.t2m.io.CSVTools;
import com.audiophile.t2m.text.Sentence;
import com.audiophile.t2m.text.Word;

import javax.sound.midi.*;
import java.io.IOException;

public class MelodyTrack implements TrackGenerator {
    private int[] toneMapping;
    private Sentence[] sentences;

    private static final int numOfChars = 255, numOfNotes = 128;

    public MelodyTrack(Harmony tone, Sentence[] text, String noteMappingFile) {
        this.sentences = text;
        this.loadToneMapping(noteMappingFile);
    }

    @Override
    public void writeToTrack(Track track, int channel) {
        try {
            MidiUtils.ChangeInstrument(0, track, channel, 0);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        int n = 0; // Marks position an track
        int len = 64; // Length of the notes
        int vel = 64; // Loudness
        try {
            for (Sentence s : sentences) {
                // Increase loudness for exclamation sentences
                if (s.getSentenceType() == Sentence.SentenceType.Exclamation)
                    vel = 100;
                else vel = 64;
                for (Word w : s.getWords())
                    for (char c : Utils.normalizeText(w.getName()).toCharArray()) {
                        int tone = c >= toneMapping.length ? getClosestTone(c) : c;
                        MidiUtils.addNote(track, n, len, toneMapping[tone], vel, channel);
                        if (c % 3 == 0)
                            MidiUtils.addNote(track, n, len * 2, toneMapping[tone / 2], vel, channel);
                        n += len;
                        // Make next note longer if character is vocal
                        if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u')
                            len = 128;
                        else
                            len = 64;
                        //TODO Only input as much text as needed (remove filler words)
                        if (n > (128 * 30)) // Sets fixed track length of 15sec
                            return;
                    }

            }
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * Finds closest tone in {@link MelodyTrack#toneMapping}.
     * Should be used for tones, which received no mapping.
     * </p>
     * <p>
     * If <code>c</code> if bigger than {@link MelodyTrack#numOfChars}, c%numOfChars is used
     * </p>
     *
     * @param c The character to get the closest tone for
     * @return The closest tone in the mapping
     */
    private int getClosestTone(char c) {
        int ch = c % numOfChars;
        for (int i = 0; i < toneMapping.length; i++) {
            if (ch + i < toneMapping.length && toneMapping[ch + i] != -1)
                return toneMapping[ch + i];
            if (ch - i > 0 && toneMapping[ch - i] != -1)
                return toneMapping[ch - i];
        }
        // Fallback
        return toneMapping[ch];
    }

    /**
     * <p>
     * Loads the character to tone mapping from the given csv file.
     * First column in csv represents character, second one the tone as integer
     * </p>
     * <p>
     * For all characters which have no mapping in the given file, to closest character is taken
     * {@link MelodyTrack#getClosestTone(char)}<br>
     * If no valid mapping is provided, the ascii code is uses for mapping.
     * </p>
     *
     * @param file The csv file containing the mapping
     */
    private void loadToneMapping(String file) {
        toneMapping = new int[numOfChars];
        // Set all values to -1 to check later if a value is set
        for (int i = 0; i < toneMapping.length; i++)
            toneMapping[i] = -1;
        try {
            String mapping[][] = CSVTools.ReadFile(file);
            for (int i = 0; i < mapping.length; i++) {
                String[] entry = mapping[i];
                try {
                    int tone = Integer.parseInt(entry[1]);
                    int clamped = Utils.clamp(tone, 0, numOfNotes - 1);
                    // Check if tone in range
                    if (clamped != tone) {
                        System.err.println("Notes must be between 0 and " + (numOfNotes - 1) + " (tone mapping)");
                        tone = clamped;
                    }
                    toneMapping[entry[0].charAt(0)] = tone;
                } catch (NumberFormatException e) {
                    System.err.println("Error in line " + i + " in file \"" + file + "\"");
                    char ch = entry[0].charAt(0);
                    toneMapping[ch] = ch % numOfNotes; // Map tone to ascii value as fallback
                }
            }
            // Use closest tone for chars with no mapping
            for (int j = 0; j < toneMapping.length; j++)
                if (toneMapping[j] == -1)
                    toneMapping[j] = getClosestTone((char) j);
        } catch (IOException e) {
            e.printStackTrace();
            for (int i = 0; i < toneMapping.length; i++)
                if (toneMapping[i] == -1)
                    toneMapping[i] = i; // Use char mapping as fallback
        }
    }
}
