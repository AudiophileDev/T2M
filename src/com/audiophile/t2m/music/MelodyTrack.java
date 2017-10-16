package com.audiophile.t2m.music;

import com.audiophile.t2m.Utils;
import com.audiophile.t2m.io.CSVTools;
import com.audiophile.t2m.text.Sentence;
import com.audiophile.t2m.text.Word;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Track;
import java.io.IOException;
import java.util.Objects;

public class MelodyTrack implements TrackGenerator {
    private int[] toneMapping;
    private Sentence[] sentences;
    private Harmony baseKey;
    private Harmony currentKey;

    private static final int numOfChars = 255, numOfNotes = 128;

    public MelodyTrack(Harmony baseKey, Sentence[] text, String noteMappingFile) {
        this.sentences = text;
        this.loadToneMapping(noteMappingFile);
        this.baseKey = baseKey;
        this.currentKey = baseKey;
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
        int wordCount = 1;
        try {
            for (Sentence s : sentences) {
                // Increase loudness for exclamation sentences
                if (s.getSentenceType() == Sentence.SentenceType.Exclamation)
                    vel = 128;
                else vel = 64;
                for (Word w : s.getWords())
                    for (char c : Utils.normalizeText(w.getName()).toCharArray()) {
                        int tone = c >= toneMapping.length ? getClosestTone(c) : c;
                        int playable = toneMapping[tone];
                        playable = inScale(playable);
                        playable = catchOutliers(playable);
                        MidiUtils.addNote(track, n, len, playable, vel, channel);
                        if (c % 3 == 0)
                            MidiUtils.addNote(track, n, len * 2, playable + (currentKey.getMode().equals("maj") ? 4 : 3), vel, channel);
                        if (c % 8 == 0)
                            MidiUtils.addNote(track, n, len * 2, playable + 7, vel, channel);
                        n += len;
                        // Make next note longer if character is vocal
                        if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u')
                            len = 128;
                        else
                            len = 64;
                        //TODO Only input as much text as needed (remove filler words)
                        if (n > (128 * 30)) // Sets fixed track length of 15sec
                            return;
                        //TODO Make sensible chord switches
                       /* if (wordCount % 15 == 0) changeChord(4);
                        else if (wordCount % 10 == 0) changeChord(5);
                        else if (wordCount % 5 == 0) {
                            changeChord(0);
                        }
                        wordCount++;*/
                    }
            }
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    /**
     * changes the current chord to either the fourth or the fifth pitch in the scale
     *
     * @param pitch sets the pitch in the scale
     */
    public void changeChord(int pitch) {
        currentKey = new Harmony((char) (baseKey.getBaseNoteChar() + (pitch == 4 ? 5 : pitch == 5 ? 7 : 0)), currentKey.getMode(), currentKey.getSept(), false);

    }

    /**
     * First the method checks if a tone is in the current scale
     * Secont it adjusts the tone upwards to fit in the scale
     *
     * @param tone the tone which should be adjusted
     * @return the adjusted tone as an integer value
     */
    public int inScale(int tone) {
        String mode = currentKey.getMode();
        int baseNote = currentKey.getBaseNoteMidi();
        int toneToCalc = (tone - baseNote) % 12; // get tone to one octave
        int[] compNotes = {0, 2, (Objects.equals(mode, "min") ? 3 : 4), 5, 7, (Objects.equals(mode, "min") ? 8 : 9), (Objects.equals(mode, "min") ? 10 : 11)}; // min: 0,2,3,5,7,8,10,12 ; maj : 0,2,4,5,7,9,11,12
        for (int i : compNotes)
            if (i == toneToCalc) { //tone is in the scale
                return tone;
            }
        return ++tone;
    }

    /**
     * If a tone differs from the basenote by two ocatves (equal to 24 half-tone steps) it is rescaled to the cotave above or below the basenote
     *
     * @param tone the tone which should be adjusted
     * @return the adjusted tone
     */
    public int catchOutliers(int tone) {
        int baseNote = baseKey.getBaseNoteMidi();
        if (Math.abs(tone - baseNote) >= 24)
            if (tone > baseNote) tone = +baseNote + (tone % 12);
            else tone = baseNote - (tone % 12);
        return tone;
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
