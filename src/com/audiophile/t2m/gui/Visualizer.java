package com.audiophile.t2m.gui;

import com.audiophile.t2m.Main;
import com.audiophile.t2m.text.Sentence;
import com.audiophile.t2m.text.TextAnalyser;
import com.audiophile.t2m.text.WordsDB;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.InlineCssTextArea;

import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
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
    public void updateText(InlineCssTextArea textArea, double minSimilarity, HashMap<String, Color> colorMapping) {
        //char[] punctuationMarks = new char[]{'.', '?', ',', '!', ';', '"', '\'',};
        textArea.clear();
        try {
            int cursor = 0;
            for (Sentence s : analyser.getSentences()) {
                String[] words = s.getWords();
                for (int i = 0; i < words.length; i++) {
                    String w = words[i];
                    Pair<String, WordsDB.WordAttributes> attr = WordsDB.GetWordAttribute(w, minSimilarity);
                    wordIndex.put(cursor, new Pair<>(w, attr));
                    textArea.insertText(cursor, w);
                    int old = cursor;
                    cursor += w.length();
                    if (attr != null) {
                        Color c = colorMapping.get(attr.getValue().tendency.toString());
                        textArea.setStyle(old, cursor, "-fx-fill: #" + colorToHex(c) + ";-fx-font-weight: bold;");
                    }
                    if (/*!contains(punctuationMarks, words[Math.min(words.length - 1, i + 1)].charAt(0)) &&*/ i + 1 != words.length) {
                        textArea.insertText(cursor, " ");
                        textArea.setStyle(cursor, cursor + 1, "");
                        cursor++;
                    }
                }
                textArea.insertText(cursor, "\n");
                cursor++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String colorToHex(Color c) {
        String red = Integer.toHexString((int) (c.getRed() * 255)),
                green = Integer.toHexString((int) (c.getGreen() * 255)),
                blue = Integer.toHexString((int) (c.getBlue() * 255));
        return String.format("%2s%2s%2s", red, green, blue).replace(' ', '0');
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

        BorderPane scrollContainer = (BorderPane) root.lookup("#scrollContainer");
        TextField similarityField = (TextField) root.lookup("#similarityField");
        similarityField.setText(String.valueOf(WordsDB.DEFAULT_IN_SIMILARITY));

        InlineCssTextArea textArea = new InlineCssTextArea(text);
        VirtualizedScrollPane scrollPane1 = new VirtualizedScrollPane<>(textArea);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        scrollContainer.setCenter(scrollPane1);

        StyleContext context = new StyleContext();

        colorMapping.forEach((k, v) -> context.addStyle(k, null)
                .addAttribute(StyleConstants.Foreground, new java.awt.Color((int) (v.getRed() * 255), (int) (v.getGreen() * 255), (int) (v.getBlue() * 255))));

        updateText(textArea, Double.parseDouble(similarityField.getText()), colorMapping);


        Label wordLabel = (Label) root.lookup("#wordLabel");
        TextField databaseWordField = (TextField) root.lookup("#databaseWord");
        Button updateButton = (Button) root.lookup("#updateButton");
        Slider tendencySlider = (Slider) root.lookup("#tendencySlider");
        TextField effectField = (TextField) root.lookup("#effectField");

        final Pair<String, WordsDB.WordAttributes>[] databaseWord = new Pair[1];
        textArea.setOnMouseReleased(event -> {
            int pos = textArea.getCaretPosition();
            // Get next last word where index is lower than pos
            int index = wordIndex.keySet().stream().filter(k -> k < pos).max(Comparator.comparingInt(v -> v)).orElse(0);
            String selected = wordIndex.get(index).getKey();
            Pair<String, WordsDB.WordAttributes> p = wordIndex.get(index).getValue();
            databaseWord[0] = p;
            // Update later to avoid thread problems
            Platform.runLater(() -> {
                wordLabel.setText(selected);
                if (p != null) {
                    databaseWordField.setText(p.getKey());
                    updateButton.setText("Remove");
                    tendencySlider.setValue(p.getValue().tendency.ordinal());
                    effectField.setText(p.getValue().effect);

                } else {
                    databaseWordField.setText(selected);
                    updateButton.setText("Add");
                    tendencySlider.setValue(2);
                }
            });
        });

        // Enable clicking on words


        tendencySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (databaseWord[0] == null)
                updateButton.setText("Add");
            else if (databaseWord[0].getValue().tendency.ordinal() != newValue.intValue())
                updateButton.setText("Update");
            else
                updateButton.setText("Remove");
        });


        databaseWordField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateButton.setDisable(newValue.length() == 0);
            updateButton.setText(newValue.equals(databaseWord[0]) ? "Remove" : "Add");
        });

        similarityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}([.]\\d{0,4})?"))
                similarityField.setText(oldValue);
        });
        similarityField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue)
                updateText(textArea, Double.parseDouble(similarityField.getText()), colorMapping);
        });

        similarityField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER)
                root.requestFocus();
        });

        updateButton.onActionProperty().setValue(event -> {
            String value = updateButton.getText();
            switch (value) {
                case "Update":
                case "Add":
                    try {
                        WordsDB.setWord(databaseWordField.getText(),
                                new WordsDB.WordAttributes(
                                        WordsDB.WordTendency.map(String.valueOf((int) tendencySlider.getValue())),
                                        effectField.getText()));
                        updateButton.setText("Remove");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "Remove":
                    try {
                        WordsDB.removeWord(databaseWordField.getText());
                        updateButton.setText("Add");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.err.println("Error processing database method \"" + value + "\"");
                    return;
            }
            updateText(textArea, Double.parseDouble(similarityField.getText()), colorMapping);
        });

    }

}
