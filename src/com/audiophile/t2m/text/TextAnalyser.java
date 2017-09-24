package com.audiophile.t2m.text;

import com.audiophile.t2m.music.Generator;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * @author Simon Niedermayr
 * Created on 05.09.2017.
 */
public class TextAnalyser {
    private Sentence[] sentences;
    private float[] avgWordLength;

    /**
     * Creates a new <code>TextAnalyser</code>, which starts splitting the text into sentences and words.<br>
     * Furthermore the average word length is calculated and each sentence is analysed individually.
     *
     * @param text text to be analysed as string
     * @see com.audiophile.t2m.text.Sentence
     */
    public TextAnalyser(String text) {
        String[] sentencesList = splitSentences(text);
        sentences = new Sentence[sentencesList.length];
        avgWordLength = new float[sentencesList.length];
        for (int i = 0; i < sentences.length; i++) {
            this.sentences[i] = new Sentence(sentencesList[i]);
            float avg = sentences[i].getAvgWordLength();
            this.avgWordLength[i] = Float.isFinite(avg)?avg:0;
        }
        Generator g = new Generator(avgWordLength);
    }

    /**
     * Returns sentences as array
     *
     * @return Sentences as array
     */
    public Sentence[] getSentences() {
        return sentences;
    }

    /**
     * Returns average word length for further generation processes
     *
     * @return average word length as array
     */
    public float[] getAvgWordLength() {
        return avgWordLength;
    }

    /**
     * Splits string into sentences by line breaks and punctuation marks.
     *
     * @param text the text to be split
     * @return Sentences as string array
     * @see java.text.BreakIterator#getSentenceInstance(Locale)
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
                        .filter(s -> s.length() > 0 && !s.equals("\r"))
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
