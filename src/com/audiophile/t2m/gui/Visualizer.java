package com.audiophile.t2m.gui;

import com.audiophile.t2m.Main;
import com.audiophile.t2m.text.Sentence;
import com.audiophile.t2m.text.TextAnalyser;
import com.audiophile.t2m.text.WordsDB;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;

public class Visualizer extends Application {

    private static TextAnalyser analyser;
    private static String text;
    HashMap<Integer, Pair<String, Pair<String, WordsDB.WordAttributes>>> wordIndex = new HashMap<>(100);

    public static void main(String[] args) {
        if (!Main.checkArguments(args))
            return;

        if (!Main.loadDatabase("wordsDB.csv"))
            return;

        StringBuffer buffer = new StringBuffer();
        if (!Main.loadTextFile(args[0], buffer))
            return;
        text = buffer.toString();
        analyser = new TextAnalyser(text);

        launch(args);
    }


    private static boolean contains(char[] array, char c) {
        for (char ch : array)
            if (c == ch)
                return true;
        return false;
    }

    //TODO do calculation in own thread and updated document when finished
    private void updateDocument(DefaultStyledDocument document, StyleContext context, double minSimilarity) {
        Style defaultStyle = context.addStyle("Default", null);
        char[] punctuationMarks = new char[]{'.', '?', ',', '!', ';', '"', '\'',};
        try {
            int cursor = 0;
            for (Sentence s : analyser.getSentences()) {
                Sentence.SentenceType type = s.getSentenceType();
                String[] words = s.getWords();
                for (int i = 0; i < words.length; i++) {
                    String w = words[i];
                    Pair<String, WordsDB.WordAttributes> attr = WordsDB.GetWordAttribute(w, minSimilarity);
                    Style style = attr != null ? context.getStyle(attr.getValue().tendency.toString()) : defaultStyle;

                    //word type highlighting
                    switch (type) {
                        case Question:
                            style.addAttribute(StyleConstants.Background, new java.awt.Color(221, 221, 221));
                            style.addAttribute(StyleConstants.Italic, true);
                            break;
                        case Exclamation:
                            style.addAttribute(StyleConstants.Background, new java.awt.Color(221, 221, 221));
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
                    if (!contains(punctuationMarks, words[Math.min(words.length - 1, i + 1)].charAt(0)) && i + 1 != words.length) {
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
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ui.fxml"));
        primaryStage.setTitle("T2M - Tool");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


        HashMap<String, Color> colorMapping = new HashMap<>(5);
        colorMapping.put("Bad", Color.RED);
        colorMapping.put("Negative", Color.DARKORANGE);
        colorMapping.put("Neutral", Color.GRAY);
        colorMapping.put("Positive", Color.DODGERBLUE);
        colorMapping.put("Good", Color.GREEN);

        FlowPane colorContainer = (FlowPane) root.lookup("#colorContainer");
        colorMapping.forEach((k, v) -> {
            HBox box = new HBox();
            box.setSpacing(5);
            box.setAlignment(Pos.CENTER);
            Pane color = new Pane();
            color.setBackground(new Background(new BackgroundFill(v, CornerRadii.EMPTY, Insets.EMPTY)));
            int size = 15;
            color.setPrefSize(size, size);
            color.setMinSize(size, size);
            color.setMaxSize(size, size);
            Label label = new Label(k);
            box.getChildren().addAll(color, label);
            colorContainer.getChildren().add(box);
        });

        SwingNode swingNode = (SwingNode) root.lookup("#textContainer");
        TextField similarityField = (TextField) root.lookup("#similarityField");
        Slider similaritySlider = (Slider) root.lookup("#similaritySlider");
        similaritySlider.setValue(WordsDB.DEFAULT_IN_SIMILARITY);
        similarityField.setText(String.valueOf(WordsDB.DEFAULT_IN_SIMILARITY));

        DefaultStyledDocument document = new DefaultStyledDocument();
        JTextPane textPane = new JTextPane(document);
        //textPane.setSize(100,Short.MAX_VALUE);
        swingNode.setContent(textPane);
        textPane.setEditable(false);
        textPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));


        StyleContext context = new StyleContext();

        colorMapping.forEach((k, v) -> context.addStyle(k, null)
                .addAttribute(StyleConstants.Foreground, new java.awt.Color((int) (v.getRed() * 255), (int) (v.getGreen() * 255), (int) (v.getBlue() * 255))));

        updateDocument(document, context, similaritySlider.getValue());
        textPane.setSize(200, 500);
        textPane.validate();

        //textPane.setPreferredSize(new Dimension(200,600));

        TextField wordField = (TextField) root.lookup("#wordField");
        Label databaseWord = (Label) root.lookup("#databaseWord");

        // Enable clicking on words
        textPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int pos = textPane.viewToModel(e.getPoint()); // Get index in text
                // Get next last word where index is lower than pos
                int index = wordIndex.keySet().stream().filter(k -> k < pos).max(Comparator.comparingInt(v -> v)).orElse(0);
                wordField.setText(wordIndex.get(index).getKey());
                Pair<String, WordsDB.WordAttributes> p = wordIndex.get(index).getValue();
                // Update later to avoid thread problems
                Platform.runLater(() -> {
                    if (p != null)
                        databaseWord.setText(p.getKey());
                    else
                        databaseWord.setText("-");
                });
            }
        });

        similaritySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double value = ((int) (newValue.doubleValue() * 100)) / 100.0;
            similarityField.setText(value + "");
            similaritySlider.setValue(value);
        });
        similarityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}([.]\\d{0,4})?")) {
                similarityField.setText(oldValue);
            } else {
                updateDocument(document, context, similaritySlider.getValue());
            }
        });
        similarityField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue)
                similaritySlider.setValue(Double.parseDouble(similarityField.getText()));
        });
        similarityField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER)
                root.requestFocus();
        });
    }

}
