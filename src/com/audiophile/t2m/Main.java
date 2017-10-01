package com.audiophile.t2m;

import com.audiophile.t2m.reader.FileReader;
import com.audiophile.t2m.musicGenerator.Merger;
import com.audiophile.t2m.musicGenerator.Tempo;
import com.audiophile.t2m.text.TextAnalyser;
import com.audiophile.t2m.text.WordsDB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        if (!checkArguments(args))
            return;

        System.out.println("Starting T2M...");
        System.out.println("Loading word database");

        if (!loadDatabase("wordsDB.csv"))
            return;

        System.out.println("Input file: " + args[0]);

        StringBuffer buffer = new StringBuffer();
        if (!loadTextFile(args[0], buffer))
            return;

        TextAnalyser analyser = new TextAnalyser(buffer.toString());
        Merger merger = new Merger(new Tempo(analyser.getAvgWordLength()));
        long endTime = System.currentTimeMillis();
        System.out.println("Text analyze finished in " + (endTime - startTime) + "ms");
    }

    public static boolean loadDatabase(String file) {
        try {
            WordsDB.loadDB(file);
            return true;
        } catch (IOException e) {
            System.out.flush();
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static boolean checkArguments(String[] args) {
        try {
            validateArguments(args);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.flush();
            System.err.println(e.getMessage());
            return false;
        }

    }

    public static boolean loadTextFile(String file, StringBuffer buffer) {
        try {
            buffer.append(FileReader.ReadPlainFile(file));
            return true;
        } catch (FileNotFoundException e) {
            System.out.flush();
            System.err.println(e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.flush();
            System.err.println("Error reading file \"" + file + "\"");
            return false;
        }
    }

    /**
     * This methods validates the input arguments and makes sure the syntax is right <br>
     * Arguments: -[input file] -[output file] <br>
     * Example: article.txt "music/output.mp3"
     *
     * @param args Array of arguments, which are validated
     */
    private static void validateArguments(String[] args) throws IllegalArgumentException {
        // Check if enough arguments were provided
        if (args.length < 2)
            throw new IllegalArgumentException("Number of arguments is to low");
        // Ensure all filenames a valid
        for (int i = 0; i < 2; i++)
            if (!isFilenameValid(args[i]))
                throw new IllegalArgumentException("Argument " + (i + 1) + " is not a valid file name");

        //TODO Validate parameters
    }

    /**
     * Checks weather a file name is valid or not. <br>
     *
     * @param file Name of the file
     * @return Validity of the file name
     * @see File#getCanonicalPath()
     */
    private static boolean isFilenameValid(String file) {
        File f = new File(file);
        try {
            f.getCanonicalPath();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}