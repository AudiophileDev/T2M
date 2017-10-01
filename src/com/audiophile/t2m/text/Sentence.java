package com.audiophile.t2m.text;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A class that represents a sentence in a text and holds it meta data and analytics values.
 *
 * @author Simon
 * Created on 05.09.2017.
 */
public class Sentence {

    private SentenceType sentenceType; // Type of the sentence
    private int wordCount; // Number of words in sentence
    private float avgWordLength; // Average characters per word
    private int[] wordLength; // Length of every word
    private Word[] words; // All words in the sentence as array

    /**
     * Creates a <code>Sentence</code> and analyses it.
     *
     * @param text sentence as text
     * @see #analyse(String, double)
     */
    public Sentence(String text) {
        analyse(text, DatabaseHandler.DEFAULT_IN_SIMILARITY);
    }

    /**
     * Analyses the sentence and calculates the meta data of the sentence. <br>
     * Every word, which can be found in the database, is linked with the corresponding database entry.
     * Therefore the given sentence is first split into words and the following values are calculated:
     * <ul>
     * <li><code>SentenceType</code></li>
     * <li>word count</li>
     * <li>average word length</li>
     * <li>word length for every word</li>
     * </ul>
     *
     * @param text The sentence as plain text
     */
    private void analyse(String text, double minWordSimilarity) {
        // Breaking sentence into words
        BreakIterator iterator = BreakIterator.getWordInstance(Locale.GERMAN);
        iterator.setText(text);
        ArrayList<String> wordList = new ArrayList<>(text.length() / 6); // Avg word length in german is 5.7
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            String word = text.substring(start, end).trim();
            if (word.length() > 0)
                wordList.add(word);
        }
        wordList.trimToSize(); // Remove unused indices if initialCapacity is bigger than the actual needed one
        this.words = new Word[wordList.size()];
        this.wordLength = new int[wordList.size()];

        // Convert ArrayList to array an calculate word lengths
        this.avgWordLength = 0;
        for (int i = 0; i < wordList.size(); i++) {
            String w = wordList.get(i);
            // Default <values
            DatabaseHandler.Entry entry = null;
            try {
                entry = DatabaseHandler.FindWord(w, minWordSimilarity);

            } catch (IOException e) {
                e.printStackTrace();
            }
            this.words[i] = new Word(w, entry);
            this.wordLength[i] = words[i].getName().length();
            this.avgWordLength += this.wordLength[i];
        }
        this.wordCount = this.words.length; // Set number of words
        this.avgWordLength /= (float) this.wordCount;

        if (wordCount > 0) {
            // Define the sentence type by last word/character
            this.sentenceType = mapSentenceType(words[wordCount - 1].getName());
            // If mapping failed, try second last word (last word might be a quotation mark)
            if (sentenceType == null && wordCount > 1) {
                this.sentenceType = mapSentenceType(words[wordCount - 2].getName());
                if (sentenceType == null)
                    this.sentenceType = SentenceType.Statement;
            }
        }
    }

    /**
     * Maps a character to a valid <code>SentenceType</code>
     *
     * @param character character to be mapped
     * @return <code>SentenceType</code> of the sentence
     */
    private SentenceType mapSentenceType(String character) {
        switch (character) {
            case "?":
                return SentenceType.Question;
            case "!":
                return SentenceType.Exclamation;
            case ".":
            case ":":
            case ";":
                return SentenceType.Statement;
            default:
                return null;
        }
    }

    public SentenceType getSentenceType() {
        return sentenceType;
    }

    public int getWordCount() {
        return wordCount;
    }

    public float getAvgWordLength() {
        return avgWordLength;
    }

    public int[] getWordLength() {
        return wordLength;
    }

    public Word[] getWords() {
        return words;
    }

    /**
     * Type of a sentence, defined by its ending character
     * .|:|; = Statement
     * ? = Question
     * ! = Exclamation
     */
    public enum SentenceType {
        Statement, Question, Exclamation
    }
}
