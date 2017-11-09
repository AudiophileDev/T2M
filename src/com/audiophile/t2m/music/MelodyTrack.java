package com.audiophile.t2m.music;

import com.audiophile.t2m.Utils;
import com.audiophile.t2m.io.CSVTools;
import com.audiophile.t2m.text.Sentence;
import com.audiophile.t2m.text.Word;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Track;
import java.io.IOException;
import java.util.ArrayList;

import static com.audiophile.t2m.music.MidiUtils.*;

public class MelodyTrack implements TrackGenerator {
    /**
     *
     */
    private static final int numOfChars = 255, numOfNotes = 128;
    /**
     * The basic key of the whole music piece
     */
    private final Harmony baseKey;
    /**
     * Maps each letter to a corresponding MidiValue
     */
    private int[] toneMapping;
    /**
     * Array of sentences of the analyzed text.
     */
    private Sentence[] sentences;
    /**
     * The current key during the melody track
     */
    private Harmony currentKey;
    /**
     * The tempo object containing the BPM and the resolution of the track.
     */
    private Tempo tempo;
    /**
     * An array storing the notes on every whole quarter.
     * It is used to check for dissonances across the different voices.
     */
    private boolean[][] notes; // initialize in multiples of 64
    /**
     * The dynamic and its gradient during the track
     */
    private Dynamic dynamic;
    /**
     * The ensemble playing the piece. Consists of {@link MyInstrument}.
     */
    private Ensemble ensemble;
    /**
     * Number of different voices playing the track
     */
    private int voices;

    MelodyTrack(MusicData musicData, Sentence[] text, String noteMappingFile, Ensemble ensemble) {
        this.sentences = text;
        loadToneMapping(noteMappingFile);
        this.baseKey = musicData.baseKey;
        this.tempo = musicData.tempo;
        this.currentKey = new Harmony(baseKey, 0);
        this.voices = ensemble.instruments.length;
        this.notes = new boolean[127][12];
        this.dynamic = musicData.dynamic;
        this.ensemble = ensemble;
    }

    /**
     * Creates a track based on the calculated {@link MusicData} and the input {@link MelodyTrack#sentences}.
     *
     * @param track   The track to write to
     * @param channel The channel to write to
     */
    @Override
    public void writeToTrack(Track track, int channel) {
        int dynamicIndex = 0, n = 0, currentVoice = 0; // Marks position an track
        int pitch;
        int len = QUARTER, prevLen = len; // Length of the notes in 128th per beat
        int playable, previous = baseKey.baseNoteMidi;
        try {
            MidiUtils.ChangeInstrument(ensemble.instruments[currentVoice], track, channel, 0);
            for (int i = 0; i < sentences.length; i++) {
                Sentence s = sentences[i];
                if (s.getSentenceType() == Sentence.SentenceType.Exclamation)       // Increase loudness for exclamation sentences
                    dynamic.initDynamic = 127;
                else dynamic.initDynamic = 64;
                for (Word w : s.getWords()) {
                    if (w.isFiller()) // Skip filler words
                        continue;
                    for (char c : Utils.normalizeText(w.getName()).toCharArray()) {
                        int tone = c >= toneMapping.length ? getClosestTone(c) : c;
                        if (n % (4 * WHOLE) == 0) currentKey = new Harmony(baseKey, 7);
                        else if (n % (2 * WHOLE) == 0) currentKey = new Harmony(baseKey, 5);

                        playable = toneMapping[tone];
                        playable = catchOutliers(playable, previous); // prevents notes jumping around or going too high or low
                        playable = inScale(playable); //ensures note is in scale

                        //handling punctuated notes
                        if (prevLen == SEMIQUAVER + QUAVER || prevLen == QUARTER + QUAVER) len = prevLen / 3;
                        else if (prevLen == SEMIQUAVER) len = SEMIQUAVER;
                        else len = setRhythm(c, currentVoice + 1);
                        prevLen = len;
                        //chord on the first beat of every bar
                        if (n % WHOLE <= QUAVER) { //beginning of every bar
                            if (dynamic.dynamicGradient.length > dynamicIndex && n > QUARTER) dynamicIndex++;
                            MidiUtils.addNote(track, n + 64 * ((playable % 4) + 1), len, playable, dynamic.initDynamic, channel);
                            notes[(n + 64 * ((playable % 4) + 1)) / 64][playable % 12] = true;
                            MidiUtils.addNote(track, n, len, isConsonant(playable, n), dynamic.initDynamic, channel);
                            notes[n / 64][playable % 12] = true;
                        } else {
                            if (dynamic.dynamicGradient.length > dynamicIndex)
                                if (Dynamic.isValidDynamic(dynamic.initDynamic + dynamic.dynamicGradient[dynamicIndex]))
                                    dynamic.initDynamic += dynamic.dynamicGradient[dynamicIndex];
                            MidiUtils.addNote(track, n, len, isConsonant(playable, n), dynamic.initDynamic, channel);
                            notes[n / 64][playable % 12] = true;
                        }
                        n += len;
                        previous = playable; //save previous to prevent going of the scale
                        if (i == sentences.length - 1) {
                            i = 0;
                        }
                        if (TicksInSecs(n, this.tempo.resolution) >= 15) {                        //finishing part
                            if (currentVoice >= this.voices) // Sets fixed track length of 15sec
                                return;
                            n = 0;
                            pitch = -12 * ((currentVoice <= 1) ? 0 : (currentVoice - 1));
                            this.currentKey = new Harmony(this.baseKey, pitch);
                            MidiUtils.ChangeInstrument(ensemble.instruments[currentVoice++], track, ++channel, 0);
                            dynamic.initDynamic -= 5;
                        }
                    }
                }
            }
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
                    if (currentKey.mode == Mode.Minor) //minor key
                        return note + 7 - newNote;
                    else
                        return note + 8 - newNote;
                case 4: //big third
                    if (currentKey.mode == Mode.Minor) //minor key
                        return note + 9 - newNote;
                    else
                        return note + 7 - newNote;
                case 5: // clean fourth
                    if (currentKey.mode == Mode.Minor) //minor key
                        return note + 8 - newNote;
                    else
                        return note + 9 - newNote;
                case 7: //clean fifth
                    return note + this.currentKey.mode.third - newNote;
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
        switch (c * 2 % (6 / inTwoVoices)) {
            case 0:
            case 1:
                len = HALF;
                break;
            case 2:
                len = QUARTER + QUAVER;
                break;
            case 3:
            case 4:
                len = QUARTER;
                break;
            case 5:
            case 6:
            case 10:
                len = QUAVER;
                break;
            case 7:
                len = QUAVER + SEMIQUAVER;
                break;
            case 8:
            case 9:
            case 11:
                len = SEMIQUAVER;
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
        int[] compNotes = {0, 2, currentKey.mode.third, 5, 7, (currentKey.mode == Mode.Minor ? 8 : 9), (currentKey.mode == Mode.Minor ? 10 : 11)};
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
        range = tone - this.baseKey.baseNoteMidi;
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