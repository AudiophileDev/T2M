package com.audiophile.t2m.text;

import com.audiophile.t2m.io.CSVTools;

import java.util.Arrays;

/**
 * This class filters all filler words from a text.
 * The filler words are defined by a CSV file
 * @author Simon
 * Created on 22.10.2017.
 */
public class WordFilter {

    private String[] fillWords;

    WordFilter(String file) {
        try {
            String content[][] = CSVTools.ReadFile(file);
            if (content.length > 0)
                fillWords = content[0];
            else {
                fillWords = new String[0];
                throw new ArrayIndexOutOfBoundsException(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isFiller(String s1) {
        return Arrays.stream(fillWords).anyMatch(s1::equals);
    }

    void markFillers(Sentence[] sentences) {
        int words = 0, filler = 0;
        for (Sentence s : sentences)
            for (Word w : s.getWords()) {
                words++;
                if (isFiller(w.getName())) {
                    w.setFiller(true);
                    filler++;
                }
            }
        System.out.println("Marked "+filler+" of "+words+" as filler ("+(filler/(float)words)*100+"%)");
    }
}
