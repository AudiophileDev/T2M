package com.audiophile.t2m.text;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;

/**
 * @author Simon
 * Created on 05.09.2017.
 */
public class Sentence {

    private SentenceType sentenceType; // Type of the sentence
    private int wordCount; // Number of words in sentence
    private float avgWordLength; // Average characters per word
    private int[] wordLength; // Length of every word
    private String[] words; // All words in the sentence as array

    public Sentence(String text) {
        analyse(text);
        System.out.println("Sentence: wordCount:" + this.wordCount + "; avgWordLength: " + avgWordLength + "; sentenceType: " + sentenceType);
    }

    /**
     * Analyses the sentence and calculates the meta data of the sentence
     *
     * @param text String The sentence as plain text
     */
    private void analyse(String text) {
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
        this.words = new String[wordList.size()];
        this.wordLength = new int[wordList.size()];

        // Convert ArrayList to array an calculate word lengths
        this.avgWordLength = 0;
        for (int i = 0; i < wordList.size(); i++) {
            this.words[i] = wordList.get(i);
            this.wordLength[i] = words[i].length();
            this.avgWordLength += this.wordLength[i];
        }
        this.wordCount = this.words.length; // Set number of words
        this.avgWordLength /= (float)this.wordCount;

        // Define the sentence type by last word/character
        this.sentenceType = mapSentenceType(words[wordCount - 1]);
        // If mapping failed, try second last word (last word might be a quotation mark)
        if (sentenceType == null && wordCount > 1) {
            this.sentenceType = mapSentenceType(words[wordCount - 2]);
            if (sentenceType == null)
                this.sentenceType = SentenceType.Statement;
        }
    }

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

    public String[] getWords() {
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
