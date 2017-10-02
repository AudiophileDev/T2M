package com.audiophile.t2m;

import com.audiophile.t2m.reader.FileReader;
import com.audiophile.t2m.musicGenerator.Merger;
import com.audiophile.t2m.musicGenerator.Tempo;
import com.audiophile.t2m.text.TextAnalyser;
import com.audiophile.t2m.text.DatabaseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    static int testTime = 10;

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

    /**
     * Loads the database and logs all errors to the console.
     * @param file The database file to load
     * @return True if no errors occurred during loading
     * @see DatabaseHandler#LoadDB(String)
     */
    public static boolean loadDatabase(String file) {
        try {
            DatabaseHandler.LoadDB(file);
            return true;
        } catch (IOException e) {
            System.out.flush();
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Checks if the application was run with correct arguments.
     * @param args The arguments the to check
     * @return True if arguments are valid
     * @see Main#validateArguments(String[])
     */
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

    /**
     * Tries to load the content of a file to the given {@link StringBuffer}.
     * All errors are logged to the console.
     * @param file The path of the file to read
     * @param buffer The buffer to which the file content is written to.
     * @return True if no exception occurred during reading.
     * @see FileReader#ReadPlainFile(String)
     */
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
            throw new IllegalArgumentException("Usage: t2m -[articleFile] -[outputFile]");
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