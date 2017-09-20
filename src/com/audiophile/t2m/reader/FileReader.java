package com.audiophile.t2m.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A class that holds a collection of methods for file reading.
 *
 * @author Simon Niedermayr
 * Created on 11.09.2017
 */
public class FileReader {
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
            throw new FileNotFoundException("The file \"" + fileName + "\" does not exist");

        // Read bytes in file
        FileInputStream stream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        stream.read(data);
        stream.close();

        return new String(data, "UTF-8");
    }

}
