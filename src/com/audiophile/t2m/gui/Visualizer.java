package com.audiophile.t2m.gui;

import com.audiophile.t2m.Main;
import com.audiophile.t2m.Utils;
import com.audiophile.t2m.text.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.InlineCssTextArea;

import javax.swing.filechooser.FileSystemView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class Visualizer extends Application {

    private static TextAnalyser analyser;
    private HashMap<Integer, Word> wordIndex = new HashMap<>(100);

    public static void main(String[] args) {
        launch(args);
    }

    //TODO do calculation in own thread and updated document when finished
    private void updateText(InlineCssTextArea textArea, double minSimilarity, HashMap<String, Color> colorMapping) {
        double scrollOffset = textArea.getEstimatedScrollY(); // make sure the user scroll stays in position
        char[] punctuationMarks = new char[]{'.', '?', ',', '!', ';', '"', '\'',};
        textArea.clear();
        try {
            int cursor = 0;
            for (Sentence s : analyser.getSentences()) {
                Word[] words = s.getWords();
                for (int i = 0; i < words.length; i++) {
                    String w = words[i].getName();
                    DatabaseHandler.Entry entry = DatabaseHandler.FindWord(w, minSimilarity);
                    wordIndex.put(cursor, new Word(w, entry));
                    textArea.insertText(cursor, w);
                    int old = cursor;
                    cursor += w.length();
                    if (entry != null) {
                        Color c = colorMapping.get(entry.getTendency().toString());
                        textArea.setStyle(old, cursor, "-fx-fill: #" + colorToHex(c) + ";-fx-font-weight: bold;");
                    }
                    if (/*!contains(punctuationMarks, words[Math.min(words.length - 1, i + 1)].charAt(0)) && */i + 1 != words.length) {
                        textArea.insertText(cursor, " ");
                        textArea.setStyle(cursor, cursor + 1, "");
                        cursor++;
                    }
                }
                textArea.insertText(cursor, "\n");
                cursor++;
            }
            Platform.runLater(() -> textArea.scrollYToPixel(scrollOffset));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean contains(char[] array, char c) {
        for (char ch : array)
            if (c == ch)
                return true;
        return false;
    }

    private String colorToHex(Color c) {
        String red = Integer.toHexString((int) (c.getRed() * 255)),
                green = Integer.toHexString((int) (c.getGreen() * 255)),
                blue = Integer.toHexString((int) (c.getBlue() * 255));
        return String.format("%2s%2s%2s", red, green, blue).replace(' ', '0');
    }

    private File chooseFile(FileChooser.ExtensionFilter extension, String title, Stage parentStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(FileSystemView.getFileSystemView().getDefaultDirectory().getAbsoluteFile());
        fileChooser.getExtensionFilters().add(extension);
        return fileChooser.showOpenDialog(parentStage);
    }

    private static final FileChooser.ExtensionFilter txtExtension = new FileChooser.ExtensionFilter("Text file", "*.txt");

    private File chooseArticle(Stage parent) {
        final File[] f = {null};
        try {
            Parent root = FXMLLoader.load(getClass().getResource("articleDialog.fxml"));
            Stage dialog = new Stage();
            dialog.setScene(new Scene(root));
            dialog.setResizable(false);
            dialog.initOwner(parent);
            dialog.setScene(root.getScene());
            dialog.initModality(Modality.APPLICATION_MODAL);

            Button ok = (Button) root.lookup("#ok"),
                    fileBtn = (Button) root.lookup("#chooserBtn"),
                    cancel = (Button) root.lookup("#cancel");
            TextField fileField = (TextField) root.lookup("#fileField");
            cancel.onActionProperty().setValue((e) -> dialog.close());
            fileBtn.requestFocus();

            fileBtn.onActionProperty().setValue((e) -> {
                f[0] = chooseFile(txtExtension, "Choose an article", parent);
                if (f[0] != null && f[0].exists()) {
                    fileField.setText(f[0].getAbsolutePath());
                    ok.setDisable(false);
                }
            });
            ok.onActionProperty().setValue((e) -> dialog.close());
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        if (f[0] == null)
            System.exit(0);
        return f[0];
    }

    private XYChart.Series<String, Number> arrayToSeries(XYChart.Series<String, Number> series, float[] array, String name) {
        if (series == null) {
            series = new XYChart.Series<>();
            series.setName(name);
            for (int i = 0; i < array.length; i++) {
                series.getData().add(new XYChart.Data<>("" + i, array[i]));
            }
        } else {
            for (int i = 0; i < array.length; i++) {
                series.getData().set(i, new XYChart.Data<>("" + i, array[i]));
            }
        }
        return series;
    }

    private void loadAvgWordLengthChart(LineChart<String, Number> chart, int radius) {
        XYChart.Series<String, Number> raw = chart.getData().stream().filter((v) -> v.getName().equals("raw")).findFirst().orElse(null);
        XYChart.Series<String, Number> smooth = chart.getData().stream().filter((v) -> v.getName().equals("smooth")).findFirst().orElse(null);
        boolean sn = smooth == null,
                rn = raw == null;
        raw = arrayToSeries(raw, analyser.getAvgWordLength(), "raw");
        smooth = arrayToSeries(smooth, Utils.BlurData(analyser.getAvgWordLength(), radius), "smooth");
        if (rn) {
            chart.getData().add(raw);
        }
        if (sn) {
            chart.getData().add(smooth);
        }
    }

    private void loadWordsPerSentence(LineChart<String, Number> chart, int radius) {
        Sentence[] sentences = analyser.getSentences();
        float[] wps = new float[sentences.length];
        for (int i = 0; i < sentences.length; i++) {
            wps[i] = sentences[i].getWordCount();
        }
        XYChart.Series raw = arrayToSeries(null, wps, "raw");
        XYChart.Series smooth = arrayToSeries(null, Utils.BlurData(wps, radius), "smooth");
        chart.getData().clear();
        chart.getData().addAll(raw, smooth);
    }

    private HashMap<LineChart<String, Number>, Stage> loadedCharts = new HashMap<>();

    private void enlargeChart(LineChart<String, Number> chart, BiConsumer<LineChart<String, Number>, Integer> consumer, Stage parent) {
        Stage dialog = loadedCharts.get(chart);
        if (dialog == null)
            try {
                Parent root = FXMLLoader.load(getClass().getResource("chartBig.fxml"));
                dialog = new Stage();
                dialog.setScene(new Scene(root));
                dialog.setTitle(chart.getTitle());
                dialog.initOwner(parent);
                dialog.setResizable(true);
                dialog.setScene(root.getScene());
                dialog.initModality(Modality.NONE);
                LineChart<String, Number> bigChart = (LineChart<String, Number>) root.lookup("#chart");
                consumer.accept(bigChart, 3);
                Slider slider = (Slider) root.lookup("#slider");
                slider.valueProperty().addListener((observable, oldValue, newValue) ->
                        consumer.accept(bigChart, newValue.intValue()));

                Stage finalDialog = dialog;
                dialog.setOnCloseRequest(e -> finalDialog.hide());
                loadedCharts.put(chart, dialog);
                dialog.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        else
            dialog.show();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("visualizer.fxml"));
        primaryStage.setTitle("T2M - Tool");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(300);
        primaryStage.show();

        File articleFile = null;

        if (getParameters().getRaw().size() > 0) {
            File argFile = new File(getParameters().getRaw().get(0));
            if (argFile.exists())
                articleFile = argFile;
        }

        if (articleFile == null) {
            root.setEffect(new GaussianBlur(7));
            articleFile = chooseArticle(primaryStage);
            root.setEffect(null);
        }

        String defaultDBFile = "wordsDB.csv";
        File dbFile = new File(defaultDBFile);
        if (!dbFile.exists() || !Main.loadDatabase(defaultDBFile)) {
            if (!dbFile.createNewFile() || !Main.loadDatabase(defaultDBFile))
                System.err.println("Error writing new Database file");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        if (!Main.loadTextFile(articleFile.getPath(), buffer))
            return;
        String text = buffer.toString();
        analyser = new TextAnalyser(text);


        // Get all components
        BorderPane scrollContainer = (BorderPane) root.lookup("#scrollContainer");
        TextField similarityField = (TextField) root.lookup("#similarityField");
        FlowPane colorContainer = (FlowPane) root.lookup("#colorContainer");
        Label wordLabel = (Label) root.lookup("#wordLabel");
        TextField databaseWordField = (TextField) root.lookup("#databaseWord");
        Button updateButton = (Button) root.lookup("#updateButton");
        Slider tendencySlider = (Slider) root.lookup("#tendencySlider");
        TextField effectField = (TextField) root.lookup("#effectField");
        Slider smoothRadiusSlider = (Slider) root.lookup("#smoothRadiusSlider");
        LineChart<String, Number> avgWordLengthChart = (LineChart) root.lookup("#avgWordLengthChart");
        LineChart<String, Number> wordsPerSentenceChart = (LineChart) root.lookup("#wordsPerSentenceChart");

        // Load charts
        int defaultRadius = (int) smoothRadiusSlider.getValue();
        loadAvgWordLengthChart(avgWordLengthChart, defaultRadius);
        loadWordsPerSentence(wordsPerSentenceChart, defaultRadius);


        HashMap<String, Color> colorMapping = new HashMap<>(5);
        colorMapping.put("Bad", Color.RED);
        colorMapping.put("Negative", Color.DARKORANGE);
        colorMapping.put("Neutral", Color.GRAY);
        colorMapping.put("Positive", Color.DODGERBLUE);
        colorMapping.put("Good", Color.GREEN);

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

        similarityField.setText(String.valueOf(DatabaseHandler.DEFAULT_IN_SIMILARITY));

        InlineCssTextArea textArea = new InlineCssTextArea(text);
        VirtualizedScrollPane scrollPane1 = new VirtualizedScrollPane<>(textArea);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        scrollContainer.setCenter(scrollPane1);

        StyleContext context = new StyleContext();

        colorMapping.forEach((k, v) -> context.addStyle(k, null)
                .addAttribute(StyleConstants.Foreground, new java.awt.Color((int) (v.getRed() * 255), (int) (v.getGreen() * 255), (int) (v.getBlue() * 255))));

        updateText(textArea, Double.parseDouble(similarityField.getText()), colorMapping);


        final Word[] databaseWord = new Word[1];
        textArea.setOnMouseReleased(event -> {
            int pos = textArea.getCaretPosition();
            // Get next last word w here index is lower than pos
            int index = wordIndex.keySet().stream().filter(k -> k < pos).max(Comparator.comparingInt(v -> v)).orElse(0);
            if (wordIndex.get(index) == null)
                return;
            Word p = wordIndex.get(index);
            String selected = p.getName();
            databaseWord[0] = p;
            // Update later to avoid thread problems
            Platform.runLater(() -> {
                wordLabel.setText(selected);
                if (p.getEntry() != null) {
                    databaseWordField.setText(p.getEntry().getName());
                    updateButton.setText("Remove");
                    tendencySlider.setValue(p.getEntry().getTendency().ordinal());
                    effectField.setText(p.getEntry().getEffect());

                } else {
                    databaseWordField.setText(selected);
                    updateButton.setText("Add");
                    tendencySlider.setValue(2);
                }
            });
        });

        // Enable clicking on words


        tendencySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (databaseWord[0] == null || databaseWord[0].getEntry() == null)
                updateButton.setText("Add");
            else if (databaseWord[0].getEntry().getTendency().ordinal() != newValue.intValue())
                updateButton.setText("Update");
            else
                updateButton.setText("Remove");
        });


        databaseWordField.textProperty().addListener((observable, oldValue, newValue) -> {
            newValue = newValue == null ? "" : newValue;
            updateButton.setDisable(newValue.length() == 0);
            updateButton.setText(newValue.equals(databaseWord[0] == null ? "" : databaseWord[0].getName()) ? "Remove" : "Add");
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
                        DatabaseHandler.SetWord(
                                databaseWordField.getText(),
                                Word.Tendency.map(String.valueOf((int) tendencySlider.getValue())),
                                effectField.getText()
                        );
                        updateButton.setText("Remove");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "Remove":
                    try {
                        DatabaseHandler.RemoveWord(databaseWordField.getText());
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

        smoothRadiusSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                Platform.runLater(() -> {
                    // reload charts
                    loadAvgWordLengthChart(avgWordLengthChart, newValue.intValue());
                    loadWordsPerSentence(wordsPerSentenceChart, newValue.intValue());
                }));

        avgWordLengthChart.setOnMouseClicked(event -> {
            enlargeChart(avgWordLengthChart, this::loadAvgWordLengthChart, primaryStage);
        });
        wordsPerSentenceChart.setOnMouseClicked(event -> {
            enlargeChart(wordsPerSentenceChart, this::loadWordsPerSentence, primaryStage);
        });

    }
}
