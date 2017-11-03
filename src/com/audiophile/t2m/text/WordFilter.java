package com.audiophile.t2m.text;

import com.audiophile.t2m.io.CSVTools;

import java.util.Arrays;

/**
 * This class filters all filler words from a text.
 * The filler words are defined by a CSV file
 *
 * @author Simon
 * Created on 22.10.2017.
 */
public class WordFilter {

    /**
     * List of filler words
     */
    private String[] fillWords;

    /**
     * Loads all fill words from the given CSV file and stores them in a array
     * @param file The CSV file with the fill words
     */
    WordFilter(String file) {
        try {
            String content[][] = CSVTools.ReadFile(file);
            if (content.length > 0)
                fillWords = content[0];
            else {
                fillWords = new String[0];
                throw new ArrayIndexOutOfBoundsException(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the given word is in the list of filler words
     * @param s1 The word to check
     * @return True, if it is a filler word
     */
    private boolean isFiller(String s1) {
        return Arrays.stream(fillWords).anyMatch(s1::equals);
    }

    /**
     * Marks all fill words in the given sentence.
     * @see Word#setFiller(boolean)
     * @param sentences The text split into sentences
     */
    void markFillers(Sentence[] sentences) {
        for (Sentence s : sentences)
            for (Word w : s.getWords()) {
                if (isFiller(w.getName())) {
                    w.setFiller(true);
                }
            }
    }
}
