package com.audiophile.t2m.text;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * @author Simon Niedermayr
 */
public class TextAnalyser {

    //TODO documentation
    public static Sentence[] analyseSentences(String text) {
        String[] sentencesList = splitSentences(text);
        Sentence[] sentences = new Sentence[sentencesList.length];
        for (int i = 0; i < sentences.length; i++) {
            sentences[i] = new Sentence(sentencesList[i]);
        }
        WordFilter filter = new WordFilter("fillwords.csv");
        filter.markFillers(sentences);
        return sentences;
    }

    //TODO documentation
    public static float[] getAvgWordLength(Sentence[] sentences) {
        float[] avgWordLength = new float[sentences.length];
        for (int i = 0; i < sentences.length; i++) {
            float avg = sentences[i].getAvgWordLength();
            avgWordLength[i] = Float.isFinite(avg) ? avg : 0;
        }
        return avgWordLength;
    }

    //TODO documentation
    public static Word.Tendency getAvgWordTendency(Sentence[] sentences) {
        int count = 0, sum = 0;
        for (Sentence s : sentences)
            for (Word w : s.getWords())
                if (w.getEntry() != null) {
                    count++;
                    sum += w.getEntry().getTendency().value;
                }
        if (count < 1)
            return Word.Tendency.Neutral;
        return Word.Tendency.map((int) (sum / (float) count));
    }

    /**
     * Splits string into sentences by line breaks and punctuation marks.
     *
     * @param text the text to be split
     * @return Sentences as string array
     * @see java.text.BreakIterator#getSentenceInstance(Locale)
     */
    private static String[] splitSentences(String text) {
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
