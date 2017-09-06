package com.audiophile.t2m.text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class TextReader {
    private String fileContent;

    public TextReader(String inputFile){
        try {
            fileContent = readTxtFile(inputFile);
        } catch (FileNotFoundException e) {
            System.out.flush();
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.out.flush();
            System.err.println("Error reading file \"" + inputFile + "\"");
            System.exit(1);
        }
    }

     /**
     * Reads a given txt file
     *
     * @param fileName String Path to the file
     * @return String Plain File content
     * @throws IOException Throws an error if the file was not found or could not be read
     */
    private String readTxtFile(String fileName) throws IOException {
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


    public String getFileContent(){
        return fileContent;
    }
}
