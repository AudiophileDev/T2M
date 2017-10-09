package com.audiophile.t2m.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Niedermayr
 */
public class CSVTools {

    // Default split characters for csv files
    private static final char DEFAULT_SEPARATOR = ',';
    // Default quote characters for csv files
    private static final char DEFAULT_QUOTE = '"';

    /**
     * Reads a CSV (Comma-separated values) file and converts it to a table.
     * Every line in the file represents a single row.
     * The columns are separated by a {@value #DEFAULT_SEPARATOR} character and quotes are marked with the {@value #DEFAULT_QUOTE} character.
     *
     * @param fileName String Path of the file to be read
     * @return The CSV table as two-dimensional string array
     * @throws IOException Throws exception if the document could not be read or does not has the expected format
     * @see <a href="https://tools.ietf.org/html/rfc4180">Common Format and MIME Type for Comma-Separated Values (CSV) Files</a>
     * @see FileUtils#ReadPlainFile(String)
     */
    public static String[][] ReadFile(String fileName) throws IOException {
        String content = FileUtils.ReadPlainFile(fileName);
        if (content.isEmpty())
            return new String[0][0];
        String[][] table = null;
        String[] lines = content.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String[] columns;
            try {
                columns = parseLine(lines[i], DEFAULT_SEPARATOR, DEFAULT_QUOTE);
            } catch (Exception e) {
                throw new IOException("An error occurred in line " + i + " while reading the file \"" + fileName + "\"");
            }
            // Initialize array size with first row
            if (table == null)
                table = new String[lines.length][columns.length];
            else if (table[0].length != columns.length) // Throw error, if a row has to few columns
                throw new IOException("To few columns in line " + i + " in file \"" + fileName + "\"");

            // Copy values to array
            System.arraycopy(columns, 0, table[i], 0, columns.length);
        }
        // Return empty array if file is empty
        if (table == null)
            table = new String[0][0];
        return table;
    }

    /**
     * The function takes a line in CSV format and converts it to a column with individual cells.
     *
     * @param cvsLine     The column as string
     * @param separator   Character for splitting the line into columns. If no value is assigned the default value {@value #DEFAULT_SEPARATOR} is used.
     * @param customQuote Character that marks quotes. If no value is assigned the default value {@value #DEFAULT_QUOTE} is used.
     * @return Line split int columns
     * @throws IOException Throws exception if an error occurs while parsing the line
     */
    private static String[] parseLine(String cvsLine, char separator, char customQuote) throws IOException {

        List<String> result = new ArrayList<>();

        // Empty string returns empty array
        if (cvsLine == null || cvsLine.isEmpty()) {
            return new String[0];
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separator == ' ') {
            separator = DEFAULT_SEPARATOR;
        }

        //  Buffer for building the column values
        StringBuffer curVal = new StringBuffer();
        // Defines if the cursor is currently in a quote
        boolean inQuotes = false;
        // Defines if the following chars are seen as cell values
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        // Cursor runs through string
        for (char ch : chars) {
            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    // If cursor is in quote, the quote ends now
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {
                    // Allow empty quote ("")
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {
                    inQuotes = true;
                    // Allow empty quote ("")
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }
                    // Allow double quotes
                    if (startCollectChar) {
                        curVal.append('"');
                    }
                } else if (ch == separator) {
                    result.add(curVal.toString());
                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    // Ignore
                } else if (ch == '\n') {
                    throw new IOException();
                } else {
                    curVal.append(ch);
                }
            }

        }
        result.add(curVal.toString());

        // Return result as Array
        String[] array = new String[result.size()];
        result.toArray(array);
        return array;
    }

    /**
     * Writes an string array to a file in the CSV format.
     * If the file does not exist jet, it is created.
     * If the given content is null, nothing is written and no file is created.
     * <p>
     * Vales are separated by {@link CSVTools#DEFAULT_SEPARATOR}
     * {@link CSVTools#DEFAULT_QUOTE} is usesd as quoting character.
     * </p>
     *
     * @param fileName The path of the file to be written
     * @param content  The content which is written to the file
     * @throws IOException
     */
    public static void WriteFile(String fileName, String[][] content) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (String[] line : content) {
            for (int i = 0; i < line.length; i++) {
                String cell = line[i];
                if (cell != null) {
                    builder.append(DEFAULT_QUOTE);
                    builder.append(cell);
                    builder.append(DEFAULT_QUOTE);
                }
                builder.append(i == line.length - 1 ? "" : DEFAULT_SEPARATOR);
            }
            builder.append("\n");
        }
        FileUtils.WriteFile(fileName, builder.toString(), false);
    }
}
