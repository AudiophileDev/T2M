package com.audiophile.t2m.io;

import java.io.*;

/**
 * A class that holds a collection of methods for file reading.
 *
 * @author Simon Niedermayr
 */
public class FileUtils {
    /**
     * The given file is read as plain text with UTF-8 encoding and converted to a <code>String</code>
     *
     * @param fileName Path to the file
     * @return Plain File content
     * @throws IOException Throws an error if the file was not found or could not be read
     * @see java.io.FileInputStream#FileInputStream(File)
     */
    public static String ReadPlainFile(String fileName) throws IOException {
        // Read bytes in file
        FileInputStream stream = new FileInputStream(fileName);
        byte[] data = new byte[stream.available()];
        stream.read(data);
        stream.close();

        return new String(data, "UTF-8");
    }

    /**
     * Writes the given <code>content</code> to the given file.
     * If <code>append</code> is true, the content is added to the end of the file.
     * Else the file is overridden.
     * @param fileName The file to write to
     * @param content The content to write to the file
     * @param append Append content to end of file
     * @throws IOException Thrown if writing to file was not possible
     */
    public static void WriteFile(String fileName, String content, boolean append) throws IOException {
        File file = new File(fileName);
        if (!file.exists())
            if (!file.createNewFile())
                throw new IOException("Could not create file\"" + fileName + "\"");
        FileWriter writer = new FileWriter(fileName);
        if (append)
            writer.append(content);
        else
            writer.write(content);
        writer.close();
    }

}
