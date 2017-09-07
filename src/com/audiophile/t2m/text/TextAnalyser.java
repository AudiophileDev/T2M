package com.audiophile.t2m.text;

import com.audiophile.t2m.music.Generator;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * @author Simon
 * Created on 05.09.2017.
 */
public class TextAnalyser {
    private Sentence[] sentences;
    private float[] avgWordLength;

    public TextAnalyser(String text) {
        String[] sentencesList = splitSentences(text);
        sentences = new Sentence[sentencesList.length];
        avgWordLength = new float[sentencesList.length];
        for (int i = 0; i < sentences.length; i++) {
            this.sentences[i] = new Sentence(sentencesList[i]);
            this.avgWordLength[i] = sentences[i].getAvgWordLength();
        }
        Generator g = new Generator(avgWordLength);
    }

    /**
     * Returns sentences as array
     *
     * @return Sentence[] Sentences as array
     */
    public Sentence[] getSentences() {
        return sentences;
    }

    /**
     * Returns average word length for further generation processes
     *
     * @return float[] average word length as array
     */
    public float[] getAvgWordLength() {
        return avgWordLength;
    }

    /**
     * Splits string into sentences
     *
     * @param text String text
     * @return String[] Sentences as string array
     */
    private String[] splitSentences(String text) {
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.GERMAN);
        iterator.setText(text);
        ArrayList<String> sentenceList = new ArrayList<>(text.length() / 6); // Avg word length in german is 5.7
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            String sentence = text.substring(start, end).trim();
            // Exclude empty sentences
            if (sentence.length() > 0) {
                Stream.of(sentence.split("\n"))
                        .filter(s -> s.length() > 0)
                        .forEach(sentenceList::add);
            }
        }
        sentenceList.trimToSize(); // Remove unused indices

        // Convert ArrayList to array
        String[] sentences = new String[sentenceList.size()];
        sentenceList.toArray(sentences);
        return sentences;
    }
}
