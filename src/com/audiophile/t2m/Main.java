package com.audiophile.t2m;

import com.audiophile.t2m.reader.FileReader;
import com.audiophile.t2m.text.Sentence;
import com.audiophile.t2m.text.TextAnalyser;
import com.audiophile.t2m.text.WordsDB;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        try {
            validateArguments(args);
        } catch (IllegalArgumentException e) {
            // Flush out stream to ensure messages have the right order in the console
            System.out.flush();
            System.err.println(e.getMessage());
            return;
        }

        System.out.println("Starting T2M...");

        try {
            System.out.println("Loading word database");
            WordsDB.loadDB("wordsDB.csv");
        } catch (IOException e) {
            System.out.flush();
            System.err.println(e.getMessage());
            return;
        }

        System.out.println("Input file: " + args[0]);

        String inputFile = args[0], fileContent;
        try {
            fileContent = FileReader.ReadPlainFile(inputFile);
        } catch (FileNotFoundException e) {
            System.out.flush();
            System.err.println(e.getMessage());
            return;
        } catch (IOException e) {
            System.out.flush();
            System.err.println("Error reading file \"" + inputFile + "\"");
            return;
        }


        TextAnalyser analyser = new TextAnalyser(fileContent);
        long endTime = System.currentTimeMillis();
        System.out.println("Text analyze finished in " + (endTime - startTime) + "ms");
        dbTest(analyser);
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

    /**
     * Function opens a <code>JFrame</code> where the input text is shown and the calculated data
     * about the text is visualized via highlighting
     *
     * @param analyser Finished text analysis
     */
    private static void dbTest(TextAnalyser analyser) {
        JFrame frame = new JFrame("T2M - Text Analyser Text");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);


        DefaultStyledDocument document = new DefaultStyledDocument();
        JTextPane textpane = new JTextPane(document);
        textpane.setEditable(false);
        //textpane.getCaret().deinstall(textpane); // Disabled user selection
        textpane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        HashMap<String, Color> colorMapping = new HashMap<>(5);
        colorMapping.put("Bad", Color.red);
        colorMapping.put("Negative", new Color(255, 100, 18));
        colorMapping.put("Neutral", Color.gray);
        colorMapping.put("Positive", new Color(41, 132, 255));
        colorMapping.put("Good", Color.green);

        StyleContext context = new StyleContext();
        // Build styles
        Style defaultStyle = context.addStyle("Default", null);
        colorMapping.forEach((k, v) -> context.addStyle(k, null).addAttribute(StyleConstants.Foreground, v));
        HashMap<Integer, Pair<String, Pair<String, WordsDB.WordAttributes>>> wordIndex = new HashMap<>(100);
        char[] punctationMarks = new char[]{'.', '?', ',', '!', ';', '"', '\'',};
        try {
            int cursor = 0;
            for (Sentence s : analyser.getSentences()) {
                Sentence.SentenceType type = s.getSentenceType();
                String[] words = s.getWords();
                for (int i = 0; i < words.length; i++) {
                    String w = words[i];
                    Pair<String, WordsDB.WordAttributes> attr = WordsDB.GetWordAttribute(w);
                    Style style = attr != null ? context.getStyle(attr.getValue().tendency.toString()) : defaultStyle;
                    switch (type) {
                        case Question:
                            style.addAttribute(StyleConstants.Background, new Color(221, 221, 221));
                            style.addAttribute(StyleConstants.Italic, true);
                            break;
                        case Exclamation:
                            style.addAttribute(StyleConstants.Background, new Color(221, 221, 221));
                            style.addAttribute(StyleConstants.Bold, true);
                            break;
                        default:
                            style.removeAttribute(StyleConstants.Italic);
                            style.removeAttribute(StyleConstants.Bold);
                            style.removeAttribute(StyleConstants.Background);
                    }
                    wordIndex.put(cursor, new Pair<>(w, attr));
                    document.insertString(cursor, w, style);
                    cursor += w.length();
                    if (!contains(punctationMarks, words[Math.min(words.length - 1, i + 1)].charAt(0)) && i + 1 != words.length) {
                        document.insertString(cursor, " ", style);
                        cursor++;
                    }
                }
                document.insertString(cursor, "\n", null);
                cursor++;
            }

        } catch (BadLocationException | IOException e) {
            e.printStackTrace();
        }

        JPanel infoContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField wordLabel = new JTextField();
        wordLabel.setPreferredSize(new Dimension(150, wordLabel.getPreferredSize().height));
        wordLabel.setEditable(false);
        JLabel infinitiveLabel = new JLabel("Database:");
        JLabel infinitive = new JLabel("");
        //infinitiveLabel.setForeground(Color.green);
        infoContainer.add(wordLabel);
        infoContainer.add(infinitiveLabel);
        infoContainer.add(infinitive);
        textpane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int pos = textpane.viewToModel(e.getPoint());
                int index = wordIndex.keySet().stream().filter(k -> k < pos).max(Comparator.comparingInt(v -> v)).orElse(0);
                wordLabel.setText(wordIndex.get(index).getKey());
                Pair<String, WordsDB.WordAttributes> p = wordIndex.get(index).getValue();
                if (p != null)
                    infinitive.setText(p.getKey());
                else
                    infinitive.setText("-");
            }
        });

        JScrollPane scrollPane = new JScrollPane(textpane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel topBar = new JPanel();
        //topBar.setPreferredSize(new Dimension(0,100));

        // Color samples
        colorMapping.forEach((k, v) -> {
            JPanel container = new JPanel(),
                    colorContainer = new JPanel();
            colorContainer.setBackground(v);
            container.add(colorContainer);
            container.add(new Label(k));
            topBar.add(container);
        });

        // Exclamation sample
        JPanel container = new JPanel();
        JLabel sample = new JLabel(" A ");
        sample.setOpaque(true);
        Font font = sample.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        sample.setBackground(Color.lightGray);
        sample.setFont(font.deriveFont(attributes));
        container.add(sample);
        container.add(new Label("Exclamation"));
        topBar.add(container);

        // Question sample
        JPanel container2 = new JPanel();
        JLabel sample2 = new JLabel(" A ");
        sample2.setOpaque(true);
        sample2.setFont(new Font("Arial", Font.ITALIC, 13));
        sample2.setBackground(Color.lightGray);
        container2.add(sample2);
        container2.add(new Label("Question"));
        topBar.add(container2);

        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(topBar, BorderLayout.NORTH);
        frame.add(infoContainer, BorderLayout.SOUTH);
        frame.setVisible(true);
        frame.toFront();
    }

    private static boolean contains(char[] array, char c) {
        for (char ch : array)
            if (c == ch)
                return true;
        return false;
    }
}