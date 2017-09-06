package com.audiophile.t2m;

import com.audiophile.t2m.text.TextAnalyser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        try {
            validateArguments(args);
        } catch (IllegalArgumentException e) {
            // Flush out stream to ensure messages have the right order in the console
            System.out.flush();
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("Starting T2M...");
        System.out.println("Input file: " + args[0]);

        String inputFile = args[0],
                fileContent;
        try {
            fileContent = readTxtFile(inputFile);
        } catch (FileNotFoundException e) {
            System.out.flush();
            System.err.println(e.getMessage());
            return;
        } catch (IOException e) {
            System.out.flush();
            System.err.println("Error reading file \"" + inputFile + "\"");
            return;
        }

        TextAnalyser analyizer = new TextAnalyser(fileContent);
    }

    /**
     * This methods validates the input arguments and makes sure the syntax is right
     * Arguments: -[input file] -[output file]
     *
     * @param args String[] Array of arguments
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
     * Reads a given txt file
     *
     * @param fileName String Path to the file
     * @return String Plain File content
     * @throws IOException Throws an error if the file was not found or could not be read
     */
    private static String readTxtFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists())
            throw new FileNotFoundException("The file \"" + fileName + "\" does not exist");

        FileInputStream stream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        int fileLength = stream.read(data);
        stream.close();

        System.out.println("File length: " + fileLength + " bytes");
        return new String(data, "UTF-8");
    }

    /**
     * Checks weather a file name is valid or not.
     *
     * @param file String Name of the File
     * @return Validity of the file name
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