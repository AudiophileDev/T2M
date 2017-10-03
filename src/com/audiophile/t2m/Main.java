package com.audiophile.t2m;

import com.audiophile.t2m.music.Composer;
import com.audiophile.t2m.reader.FileReader;
import com.audiophile.t2m.text.DatabaseHandler;
import com.audiophile.t2m.text.TextAnalyser;
import com.audiophile.t2m.writer.MusicWriter;

import javax.sound.midi.Sequence;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private final static Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        LOGGER.setLevel(Level.SEVERE);
        long startTime = System.currentTimeMillis();
        // Check if the given arguments are valid
        if (!checkArguments(args))
            return;

        // Load word database
        if (!loadDatabase("wordsDB.csv"))
            return;

        // Enable/Disable precise search in database
        DatabaseHandler.PRECISE_SEARCH = hasArgument("precise", args);

        // Load article
        StringBuffer buffer = new StringBuffer();
        if (!loadTextFile(args[0], buffer))
            return;

        // Analyse article
        TextAnalyser analyser = new TextAnalyser(buffer.toString());
        long endTime = System.currentTimeMillis();
        System.out.println("Analyzed \""+args[0]+"\" in " + (endTime - startTime) + "ms");

        // Generate music
        startTime = System.currentTimeMillis();
        Composer composer = new Composer(analyser);
        endTime = System.currentTimeMillis();
        System.out.println("Generated music in " + (endTime - startTime) + "ms");

        Sequence sequence = composer.getSequence();

        // Output music
        String outputType = extractArgument("output", args, "mp3");
        outputMusic(outputType, args[1], sequence);
    }

    /**
     * Outputs the music with the given output type.
     *
     * @param outputType The output form. {@link MusicWriter#MP3} is default.
     * @param fileName   The file to write to
     * @param sequence   The MIDI sequence to be written
     * @see MusicWriter
     */
    private static void outputMusic(String outputType, String fileName, Sequence sequence) {
        switch (outputType) {
            case MusicWriter.PLAY:
                System.out.println("Playing generated music");
                MusicWriter.play(sequence);
                break;
            case MusicWriter.MIDI:
                try {
                    System.out.println("Writing MIDI file to \""+fileName+"\"");
                    MusicWriter.writeMidi(sequence, fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case MusicWriter.WAV:
                try {
                    System.out.println("Writing WAV file to \""+fileName+"\"");
                    MusicWriter.writeWav(sequence, fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case MusicWriter.MP3:
            default: // mp3 is default
                try {
                    System.out.println("Writing MP3 file to \""+fileName+"\"");
                    MusicWriter.writeMP3(sequence, fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * Loads the database and logs all errors to the console.
     *
     * @param file The database file to load
     * @return True if no errors occurred during loading
     * @see DatabaseHandler#LoadDB(String)
     */
    public static boolean loadDatabase(String file) {
        try {
            LOGGER.info("Loading words database file \"" + file + "\"");
            DatabaseHandler.LoadDB(file);
            return true;
        } catch (IOException e) {
            System.err.println("An Error occurred while loading the words database \"" + file + "\"");
            LOGGER.severe("Error loading database  \"" + file + "\"");
            LOGGER.severe(e.getMessage());
            return false;
        }
    }

    /**
     * Checks if the application was run with correct arguments.
     *
     * @param args The arguments the to check
     * @return True if arguments are valid
     * @see Main#validateArguments(String[])
     */
    private static boolean checkArguments(String[] args) {
        try {
            LOGGER.info("Arguments: " + String.join(" ", args));
            validateArguments(args);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.flush();
            System.err.println(e.getMessage());
            LOGGER.severe(e.getMessage());
            return false;
        }

    }

    /**
     * Tries to load the content of a file to the given {@link StringBuffer}.
     * All errors are logged to the console.
     *
     * @param file   The path of the file to read
     * @param buffer The buffer to which the file content is written to.
     * @return True if no exception occurred during reading.
     * @see FileReader#ReadPlainFile(String)
     */
    public static boolean loadTextFile(String file, StringBuffer buffer) {
        try {
            buffer.append(FileReader.ReadPlainFile(file));
            LOGGER.info("Loading file \"" + file + "\"");
            return true;
        } catch (FileNotFoundException e) {
            System.out.flush();
            System.err.println(e.getMessage());
            LOGGER.severe(e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.flush();
            System.err.println("Error reading file \"" + file + "\"");
            LOGGER.severe(e.getMessage());
            return false;
        }
    }

    private static final String CLI_USAGE = "Usage: articleFile outputFile -db databaseFile -[output] ["
            + MusicWriter.MP3 + " "
            + MusicWriter.WAV + " "
            + MusicWriter.MIDI + " "
            + MusicWriter.PLAY + " "
            + "] -[precise] -db databaseFile";

    /**
     * This methods validates the input arguments and makes sure the syntax is right <br>
     *
     * @param args Array of arguments, which are validated
     * @see Main#CLI_USAGE
     */
    private static void validateArguments(String[] args) throws IllegalArgumentException {
        // Check if enough arguments were provided. input and output file is required
        if (args.length < 2)
            throw new IllegalArgumentException(CLI_USAGE);
        // Ensure all filenames a valid
        for (int i = 0; i < 2; i++)
            if (!isFilenameValid(args[i]))
                throw new IllegalArgumentException("Argument " + (i + 1) + " is not a valid file name");

        //TODO Validate parameters
    }

    /**
     * Extract an argument from the run parameters.
     * If the argument is not provided a empty string is returned.
     * If the argument was provided wrong, null is returned.
     * e.g. getArgument({"t2m", "-output", "play"},"output") => play
     *
     * @param param The parameter to extract for
     * @param args  Run parameters from main method
     * @return The argument content.
     */
    private static String extractArgument(String param, String[] args, String escapeValue) {
        for (int i = 0; i < args.length; i++)
            if (args[i].equals("-" + param))
                if (i + 1 >= args.length || args[i + 1].startsWith("-")) {
                    System.err.println("Argument \"" + param + "\" was not provided correctly");
                    return escapeValue;
                } else
                    return args[i + 1];
        return escapeValue;
    }

    /**
     * Checks if the run parameters contain a certain argument.
     *
     * @param param The parameter to look for
     * @param args  The run arguments
     * @return True if param is present in args
     */
    private static boolean hasArgument(String param, String[] args) {
        for (String arg : args)
            if (arg.equals("-" + param))
                return true;
        return false;
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