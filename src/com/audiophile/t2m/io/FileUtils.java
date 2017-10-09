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
        File file = new File(fileName);
        if (!file.exists())
            throw new FileNotFoundException("The file \"" + file.getAbsolutePath() + "\" does not exist");

        // Read bytes in file
        FileInputStream stream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        stream.read(data);
        stream.close();

        return new String(data, "UTF-8");
    }

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
