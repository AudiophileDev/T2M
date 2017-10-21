package com.audiophile.t2m.music;

import com.audiophile.t2m.Utils;
import com.audiophile.t2m.io.CSVTools;
import com.audiophile.t2m.text.Sentence;
import com.audiophile.t2m.text.Word;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Track;
import java.io.IOException;
import java.util.ArrayList;

public class MelodyTrack implements TrackGenerator {
    private static final int numOfChars = 255, numOfNotes = 128;
    private static final int WHOLE = 512, HALF = 256, QUARTER = 128, QUAVER = 64, SEMIQUAVER = 32, DREISEMQUAVER = 16;
    private final Harmony baseKey;
    private int[] toneMapping;
    private Sentence[] sentences;
    private Harmony currentKey;
    private Tempo tempo;
    private int dramaLevel;
    private boolean[][] notes; // initialize in multiples of 64

    public MelodyTrack(MusicData musicData, Sentence[] text, String noteMappingFile) {
        this.sentences = text;
        this.loadToneMapping(noteMappingFile);
        this.baseKey = musicData.getKey();
        this.tempo = musicData.getTempo();
        this.currentKey = new Harmony(baseKey, 0);
        this.dramaLevel = text[baseKey.getBaseNoteMidi() % text.length].getWordCount() % 3;
        this.notes = new boolean[127][12];
    }

    @Override
    public void writeToTrack(Track track, int channel) {
        try {
            MidiUtils.ChangeInstrument(1, track, channel, 0);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        int n = 0; // Marks position an track
        int len = QUARTER, oldLen = len; // Length of the notes in 128th per beat
        int vel = 64; // Loudness
        int part = 0;
        int playable, previous = baseKey.getBaseNoteMidi();
        int compTone;

        try {
            for (Sentence s : sentences) {
                // Increase loudness for exclamation sentences
                if (s.getSentenceType() == Sentence.SentenceType.Exclamation)
                    vel = 127;
                else vel = 64;
                for (Word w : s.getWords())
                    for (char c : Utils.normalizeText(w.getName()).toCharArray()) {
                        int tone = c >= toneMapping.length ? getClosestTone(c) : c;
                        if (n % (4 * WHOLE) == 0) {
                            currentKey = new Harmony(baseKey, 7);
                        } else if (n % (2 * WHOLE) == 0) {
                            currentKey = new Harmony(baseKey, 5);
                        }
                        playable = toneMapping[tone];
                        playable = catchOutliers(playable, previous);
                        playable = inScale(playable);
                        if (oldLen == SEMIQUAVER + QUAVER || oldLen == QUARTER + QUAVER) {
                            len = oldLen / 3;
                        } else len = setRhythm(c, part + 1);
                        //chord on one
                        oldLen = len;
                        if (n % WHOLE == 0) {
                            len = QUARTER;
                            //TODO save notes to array
                            //TODO check if consonant

                            MidiUtils.addNote(track, n + 64 * ((playable % 4) + 1), len, playable, vel / 4 * 3, channel);
                            notes[(n + 64 * ((playable % 4) + 1)) / 64][playable % 12] = true;

                            MidiUtils.addChord(track, n, len, currentKey.getNotesNumber(), -1, vel / 2, channel, false);
                            MidiUtils.addPowerChord(track, n, len, currentKey.getNotesNumber(), -2, vel / 2, channel);
                            notes[n / 64][currentKey.getNotesNumber().get(0) % 12] = true;
                            notes[n / 64][currentKey.getNotesNumber().get(1) % 12] = true;
                            notes[n / 64][currentKey.getNotesNumber().get(2) % 12] = true;
                            //System.out.print(currentKey.getNotesNumber() + " ->( " + playable + ", " + (len + 64 * ((playable % 4) + 1)) + ") ");
                            len = oldLen;

                        } else {
                            MidiUtils.addNote(track, n, len, isConsonant(playable, n), vel, channel);
                            notes[n / 64][playable % 12] = true;
                        }
                        // System.out.println(", " + len);
                        n += len;
                        previous = playable;

                        //TODO Only input as much text as needed (remove filler words)

                        if (n > ((128 * tempo.getAverageBpm()) / 4)) {
                            part++;
                            if (part == dramaLevel - 1) {
                                n = 0;
                                this.currentKey = new Harmony(this.baseKey, -12);

                            } else if (part == dramaLevel) {
                                n = 0;
                                this.currentKey = new Harmony(this.baseKey, +12);
                                // MidiUtils.ChangeInstrument(44, track, channel, 0);
                            }
                            if (part > dramaLevel) // Sets fixed track length of 15sec
                            {
                                //Gaudi ende
                                n += QUARTER;
                                len = QUAVER;
                                Harmony cadenceKey = new Harmony(baseKey, 7);
                                MidiUtils.addNote(track, n, len, cadenceKey.getBaseNoteMidi() - 12, vel, channel);
                                MidiUtils.addNote(track, n, len, cadenceKey.getNotesNumber().get(1) - 12, vel, channel);
                                MidiUtils.addNote(track, n, len, cadenceKey.getNotesNumber().get(2) - 12, vel, channel);
                                MidiUtils.addNote(track, n + QUAVER, len, baseKey.getBaseNoteMidi(), vel, channel);
                                MidiUtils.addNote(track, n + QUAVER, len, baseKey.getNotesNumber().get(1), vel, channel);
                                MidiUtils.addNote(track, n + QUAVER, len, baseKey.getNotesNumber().get(2), vel, channel);
                                System.out.println("in " + part + " parts");
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

    public int isConsonant(int note, int startTick) {
        int newNote = note % 12;
        startTick /= 64;
        int difference = 0;
        int min = 11;
        ArrayList<Integer> filled = new ArrayList<>();
        for (int i = 0; i < notes[startTick].length/*12*/; i++)
            if (notes[startTick][i]) {
                filled.add(i);
                if (i == newNote) {
                    // System.out.println("note was already used");
                    return note;
                }

            }

        int size = filled.size();
        if (size == 0) {
            //System.out.println("no note used");

            return note;
        } else if (size == 1) {
            while (!"3457".contains(String.valueOf(Math.abs(newNote - filled.get(0))))) {
                newNote = ++note % 12;
            }
            //System.out.println("notes played:" + newNote + "," + filled.get(0));

            return note;
        } else {
            switch (filled.get(1) - filled.get(0)) {
                case 3:
                    if (currentKey.getMode() == 3) //minor key
                        //8
                        return note + 7 - newNote;
                    else
                        return note + 8 - newNote;
                case 4:
                    if (currentKey.getMode() == 3) //minor key
                        //8
                        return note + 9 - newNote;
                    else
                        return note + 7 - newNote;
                case 5:
                    if (currentKey.getMode() == 3) //minor key
                        //8
                        return note + 8 - newNote;
                    else
                        return note + 9 - newNote;

                case 7:
                    if (currentKey.getMode() == 3) //minor key
                        //8
                        return note + 3 - newNote;
                    else
                        return note + 4 - newNote;
                default:
                    return note; //should not happen
            }
        }
        //return note;
    }


    public int setRhythm(int c, int inTwoVoices) {
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

    public void parseRhythm(String rhythm) {
        char[] singeValues = rhythm.toCharArray();

    }

    /**
     * First the method checks if a tone is in the current scale
     * Secont it adjusts the tone upwards to fit in the scale
     *
     * @param tone the tone which should be adjusted
     * @return the adjusted tone as an integer value
     */
    public int inScale(int tone) {
        int mode = currentKey.getMode();
        int baseNote = currentKey.getBaseNoteMidi();
        int toneToCalc = tone % 12; // get tone to one octave
        int[] compNotes = {0, 2, mode, 5, 7, (mode == 3 ? 8 : 9), (mode == 3 ? 10 : 11)};
        // min: 0,2,3,5,7,8,10,12 ; maj : 0,2,4,5,7,9,11,12
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
    public int catchOutliers(int tone, int previous) {
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