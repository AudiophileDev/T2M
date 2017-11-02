package com.audiophile.t2m.music;

import com.audiophile.t2m.Utils;
import com.audiophile.t2m.io.CSVTools;
import com.audiophile.t2m.text.Sentence;
import com.audiophile.t2m.text.Word;
import com.audiophile.t2m.text.WordFilter;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Track;
import java.io.IOException;
import java.util.ArrayList;

import static com.audiophile.t2m.music.MidiUtils.WHOLE;
import static com.audiophile.t2m.music.MidiUtils.HALF;
import static com.audiophile.t2m.music.MidiUtils.QUARTER;
import static com.audiophile.t2m.music.MidiUtils.DREISEMQUAVER;
import static com.audiophile.t2m.music.MidiUtils.SEMIQUAVER;
import static com.audiophile.t2m.music.MidiUtils.QUAVER;

public class MelodyTrack implements TrackGenerator {
    private final Harmony baseKey;
    private int[] toneMapping;
    private Sentence[] sentences;
    private Harmony currentKey;
    private Tempo tempo;
    private boolean[][] notes; // initialize in multiples of 64
    private int dynamic;
    private MyInstrument instrument;

    private static final int numOfChars = 255, numOfNotes = 128;

    MelodyTrack(MusicData musicData, Sentence[] text, String noteMappingFile, MyInstrument instrument) {
        this.sentences = text;
        loadToneMapping(noteMappingFile);
        this.baseKey = musicData.getKey();
        this.tempo = musicData.getTempo();
        this.currentKey = new Harmony(baseKey, 0);
        //int dramaLevel = text[baseKey.getBaseNoteMidi() % text.length].getWordCount() % 3;
        this.notes = new boolean[127][12];
        this.dynamic = musicData.getDynamic();
        this.instrument = instrument;
    }

    @Override
    public void writeToTrack(Track track, int channel) {
        int n = 0; // Marks position an track
        int len = QUARTER, prevLen = len; // Length of the notes in 128th per beat
        int part = 0;
        int playable, previous = baseKey.getBaseNoteMidi();
        int chars = 0, words = 0, sen = 0;
        try {
            MidiUtils.ChangeInstrument(instrument.MidiValue, track, channel, 0);
            for (Sentence s : sentences) {
                sen++;
                // Increase loudness for exclamation sentences
                if (s.getSentenceType() == Sentence.SentenceType.Exclamation)
                    dynamic = 127;
                else dynamic = 64;
                for (Word w : s.getWords()) {
                    if (w.isFiller()) // Skip filler words
                        continue;
                    words++;
                    for (char c : Utils.normalizeText(w.getName()).toCharArray()) {
                        chars++;
                        int tone = c >= toneMapping.length ? getClosestTone(c) : c;
                        if (n % (4 * WHOLE) == 0) {
                            currentKey = new Harmony(baseKey, 7);
                        } else if (n % (2 * WHOLE) == 0) {
                            currentKey = new Harmony(baseKey, 5);
                        }
                        playable = toneMapping[tone];
                        playable = catchOutliers(playable, previous); // prevents notes jumping around or going too high or low
                        //TODO add notes off the scale for special effects
                        playable = inScale(playable); //ensures note is in scale

                        if (prevLen == SEMIQUAVER + QUAVER || prevLen == QUARTER + QUAVER) { //handling punctuated notes
                            len = prevLen / 3;
                        } else len = setRhythm(c, part + 1);
                        prevLen = len;

                        //chord on the first beat of every bar
                        if (n % WHOLE == 0) { //beginning of every bar
                            //len = QUARTER;
                            //TODO check if consonant
                            MidiUtils.addNote(track, n + 64 * ((playable % 4) + 1), len, playable, dynamic / 4 * 3, channel);
                            notes[(n + 64 * ((playable % 4) + 1)) / 64][playable % 12] = true;

                            MidiUtils.addChord(track, n, len, currentKey.getNotesNumber(), -1, dynamic / 2, channel, false);
                            MidiUtils.addPowerChord(track, n, len, currentKey.getNotesNumber(), -2, dynamic / 2, channel);
                            notes[n / 64][currentKey.getNotesNumber().get(0) % 12] = true;
                            notes[n / 64][currentKey.getNotesNumber().get(1) % 12] = true;
                            notes[n / 64][currentKey.getNotesNumber().get(2) % 12] = true;
                            //len = prevLen;
                        } else {
                            MidiUtils.addNote(track, n, len, isConsonant(playable, n), dynamic, channel);
                            notes[n / 64][playable % 12] = true;
                        }
                        n += len;
                        previous = playable; //save previous to prevent going of the scale

                        //TODO Only input as much text as needed (remove filler words)
                        if (n > ((128 * tempo.getAverageBpm()) / 4)) {                        //finishing part
                            System.out.println("Used chars: " + chars + ", words:" + words + ", sen: " + sen);
                            return;
                        }
                    }
                }
            }
            //TODO add end phrase

        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    /**
     * adjusts a note that it yields a consonant sound
     *
     * @param note      the note which is played
     * @param startTick the point in time when the note is play
     * @return returns the adjusted note
     */

    private int isConsonant(int note, int startTick) {
        int size, newNote = note % 12;
        ArrayList<Integer> filled = new ArrayList<>();
        startTick /= 64;
        for (int i = 0; i < notes[startTick].length/*12*/; i++)
            if (notes[startTick][i]) {
                filled.add(i);
                if (i == newNote) {
                    // System.out.println("note was already used");
                    return note;
                }
            }

        size = filled.size();
        if (size == 0) {
            return note;
        } else if (size == 1) { //one note existing; add note for consonant interval
            while (!"3457".contains(String.valueOf(Math.abs(newNote - filled.get(0))))) { //check if already good
                newNote = ++note % 12;
            }
            return note;
        } else { //chord should be completed
            switch (filled.get(1) - filled.get(0)) { //existing interval
                case 3: //small third
                    if (currentKey.getMode() == Mode.Minor) //minor key
                        return note + 7 - newNote;
                    else
                        return note + 8 - newNote;
                case 4: //big third
                    if (currentKey.getMode() == Mode.Minor) //minor key
                        return note + 9 - newNote;
                    else
                        return note + 7 - newNote;
                case 5: // clean fourth
                    if (currentKey.getMode() == Mode.Minor) //minor key
                        return note + 8 - newNote;
                    else
                        return note + 9 - newNote;
                case 7: //clean fifth
                    return note + this.currentKey.getMode().third - newNote;
                default:
                    return note; //should not happen
            }
        }
    }

    /**
     * calculates note length based on the character value of the character in the text
     *
     * @param c           character value
     * @param inTwoVoices indicates for which voice the rhythm should be
     * @return calculated length of the note
     */
    private int setRhythm(int c, int inTwoVoices) {
        int len;
        switch (c % (6 / inTwoVoices)) {
            case 0:
                len = HALF;
                break;
            case 1:
                len = QUARTER + QUAVER;
                break;
            case 2:
                len = QUARTER;
                break;
            case 3:
                len = QUAVER;
                break;
            case 4:
                len = QUAVER + SEMIQUAVER;
                break;
            case 5:
                len = SEMIQUAVER;
                break;
            case 6:
                len = QUAVER;
                break;
            default:
                len = QUARTER;
        }
        return len;
    }

    /**
     * First the method checks if a tone is in the current scale
     * Second it adjusts the tone upwards to fit in the scale
     *
     * @param tone the tone which should be adjusted
     * @return the adjusted tone as an integer value
     */
    private int inScale(int tone) {
        int toneToCalc = tone % 12; // get tone to one octave
        int[] compNotes = {0, 2, currentKey.getMode().third, 5, 7, (currentKey.getMode() == Mode.Minor ? 8 : 9), (currentKey.getMode() == Mode.Minor ? 10 : 11)};
        // min: 0,2,3,5,7,8,10,12 ; maj : 0,2,4,5,7,9,11,12
        for (int i : compNotes)
            if (i == toneToCalc) { //tone is in the scale
                return tone;
            }
        return ++tone;
    }

    /**
     * <p>If a tone differs from the {@link Harmony#baseNoteMidi}  by two octaves (equal to 24 half-tone steps) it is rescaled to the octave above or below the basenote
     * </p>
     *
     * @param tone the tone which should be adjusted
     * @return the adjusted tone
     */
    private int catchOutliers(int tone, int previous) {
        int range;
        if (tone > 12 + previous)
            tone = previous + (tone % 12);
        else if (tone < previous - 12)
            tone = previous - (tone % 12);
        range = tone - this.baseKey.getBaseNoteMidi();
        if (range >= 24 || tone > 90) {
            tone = previous - (tone % 12);
        } else if (range <= -24 || tone < 36)
            tone = previous + (tone % 12);
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