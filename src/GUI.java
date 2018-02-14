/**
 The GUI class creates the user interface. It displays the scene containing
 GridPane with pane #1, which displays the sample text to be typed and pane
 #2, where the user types their input. GridPane gridStatus displays pane #3
 which contains the timer, words typed and words-per-minute.

 @author Hicham El-Abbadi
 @version 1.0
 @since 02/14/2018
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.Scene;

public class GUI extends Application {

    private AnimationTimer timer;
    private Label timerLabel;
    private Label startTyping;
    private Label wordsTyped;
    private Label WPM;
    private int seconds;
    private int timerSeconds;
    private int charCount;
    private int wordCount;
    private double WordsPerMin;
    private boolean space;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Initialize values
        charCount = 0;
        wordCount = 0;
        space = false;

        //Title of the window
        primaryStage.setTitle("Typing Test");

        //Program icon
        primaryStage.getIcons().add(new Image("file:keyboard.png"));

        //Labels
        timerLabel = new Label ("Time: 0 sec");
        startTyping = new Label("Start typing sample text to begin test...");
        wordsTyped = new Label("Words typed: 0");
        WPM = new Label("Words/Minute: ");
        startTyping.setWrapText(true);

        //Timer method to start, stop and update timer
        timer = new AnimationTimer() {

            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime != 0) {
                    if (now > lastTime + 1_000_000_000) {
                        seconds++;
                        timerSeconds = seconds;
                        timerLabel.setText("Time: " + Integer.toString(seconds) + " sec");
                        lastTime = now;
                    }
                } else {
                    lastTime = now;

                }
            }

            @Override
            public void stop() {
                super.stop();
                lastTime = 0;
                seconds = 0;
            }
        };

        /*Creates instance of TypingTest class and calls method textMethod
          to retrieve sample text from text file. charLen stores the
          number of characters in the string.
         */
        TypingTest test = new TypingTest();
        StringBuilder sb = test.textMethod();
        String sampleText = sb.toString();
        int charLen = sampleText.length();

        //GridPanes for left and right sides of UI
        GridPane gridText = new GridPane();
        gridText.setPadding(new Insets(10, 10, 10, 10));
        gridText.setPrefWidth(300);
        gridText.setVgap(8);

        GridPane gridStatus = new GridPane();
        gridStatus.setPadding(new Insets(10, 10, 10, 10));
        gridStatus.setPrefWidth(170);
        gridStatus.setVgap(8);

        BorderPane borderPane = new BorderPane();

        //Create TextAreas for pane #1 and pane #2
        TextArea sampleField = new TextArea(sampleText);
        sampleField.setEditable(false);
        sampleField.setWrapText(true);
        TextArea textField = new TextArea();
        textField.setWrapText(true);
        textField.setPromptText("Begin typing here...");

        GridPane.setConstraints(sampleField, 0, 0);
        GridPane.setConstraints(textField, 0, 1);

        GridPane.setConstraints(startTyping, 0, 1);
        GridPane.setConstraints(timerLabel, 0, 7);
        GridPane.setConstraints(wordsTyped, 0, 8);
        GridPane.setConstraints(WPM, 0, 15);

        //Add labels to corresponding panes
        gridStatus.getChildren().addAll(startTyping, timerLabel, wordsTyped, WPM);
        gridText.getChildren().addAll(sampleField, textField);
        borderPane.setCenter(gridText);
        borderPane.setRight(gridStatus);

        //Starts timer when first character is entered, tracks character and word count
        textField.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent ke) {
                startTyping.setText("");
                if(charCount != charLen) {
                    timer.start();

                    //If backspace used, decrements charCount
                    if (ke.getCode().equals(KeyCode.BACK_SPACE) && charCount > 0) {
                        charCount--;
                        //
                        if(space) {
                            if(wordCount > 0) {
                                wordCount--;
                            }
                            space = false;
                        }
                    } else if (!(ke.getCode().equals(KeyCode.SHIFT)) ||
                            ke.getCode().equals(KeyCode.CAPS)) {
                        charCount++;
                        space = false;
                    }

                    /*Increment wordCount when space is pressed. Sets boolean space
                      to true so if backspace is pressed, wordCount is decremented.
                     */
                    if (ke.getCode().equals(KeyCode.SPACE)) {
                        wordCount++;
                        space = true;
                        wordsTyped.setText("Words typed: " + wordCount);
                    }

                    /*
                      Once number of characters typed equals characters in sample
                      text, stop timer, disable input, and calculate Words-Per-Minute.
                     */
                    if (charCount == charLen) {
                        wordCount++;
                        timer.stop();
                        textField.setEditable(false);

                        wordsTyped.setText("Words typed: " + wordCount);
                        double dbl = (double)timerSeconds;
                        dbl /= 60.0;
                        WordsPerMin = (double)wordCount / dbl;
                        String text = String.format("%.2f", WordsPerMin);
                        WPM.setText("Words/Minute: " + text);
                    }
                }
            }
        });

        Scene scene = new Scene(borderPane, 750, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
