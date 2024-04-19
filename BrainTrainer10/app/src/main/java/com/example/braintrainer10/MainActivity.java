package com.example.braintrainer10;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.braintrainer10.databinding.ActivityMainBinding;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mainBinding;
    Button startButton, button0, button1, button2, button3, playAgain;
    RelativeLayout gameRelativeLayout;
    TextView resulttextview, pointstextview, sumtextview, timertextview;
    ArrayList<Integer> answers = new ArrayList<>();
    HashMap<String, String> corrections = new HashMap<>();
    HashMap<String, String> incorrectCorrections = new HashMap<>();
    int locationOfCorrectAnswer;
    int score = 0;

    int numberOfQuestions = 0;
    Random rand;

    @SuppressLint("SetTextI18n")
    public void playAgain(View view) {
        score = 0;
        numberOfQuestions = 0;

        button0.setEnabled(true);
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        timertextview.setText("30s");
        pointstextview.setText("0/0");
        resulttextview.setText("");

        playAgain.setVisibility(View.INVISIBLE);

        new CountDownTimer(30100, 1000) {

            @Override
            public void onTick(long l) {

                timertextview.setText(String.valueOf(l / 1000) + "s");
            }

            @Override
            public void onFinish() {
                button0.setEnabled(false);
                button1.setEnabled(false);
                button2.setEnabled(false);
                button3.setEnabled(false);
                playAgain.setVisibility(View.VISIBLE);
                timertextview.setText("0s");
                resulttextview.setText("Your score: " + score + "/" + numberOfQuestions);
                showCorrections();
            }

        }.start();
    }

    @SuppressLint("SetTextI18n")
    private void showCorrections() {
        if (score < numberOfQuestions) {
            // Create a dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.corrections_dialog, null);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();

            // Set title
            TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
            titleTextView.setText("Corrections");

            // Populate corrections text
            TextView correctionsTextView = dialogView.findViewById(R.id.corrections_text);
            StringBuilder correctionsText = new StringBuilder();
            for (Map.Entry<String, String> entry : incorrectCorrections.entrySet()) {
                correctionsText.append("Question: ").append(entry.getKey()).append("\n")
                        .append("Correct Answer: ").append(entry.getValue()).append("\n\n");
            }
            correctionsTextView.setText(correctionsText.toString());

            // Close button click listener
            Button closeButton = dialogView.findViewById(R.id.close_button);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss(); // Dismiss the dialog
                }
            });

            // Show the dialog
            dialog.show();
        }

    }

    @SuppressLint("SetTextI18n")
    public void generateQuestion() {
        rand = new Random();
        char[] symbols = {'+', '-', '*', '/'};
        int randSymbol = rand.nextInt(symbols.length);

        double a = rand.nextInt(21);
        double b = rand.nextInt(21);

        if (symbols[randSymbol] == '+') {
            sumtextview.setText((int) a + " + " + (int) b);
            locationOfCorrectAnswer = rand.nextInt(4);
            double res = a + b;
            generateAnswers(res);
            corrections.put((int) a + " + " + (int) b, String.valueOf((int) res));
        } else if (symbols[randSymbol] == '-') {
            sumtextview.setText((int) a + " - " + (int) b);
            locationOfCorrectAnswer = rand.nextInt(4);
            double res = a - b;
            generateAnswers(res);
            corrections.put((int) a + " - " + (int) b, String.valueOf((int) res));
        } else if (symbols[randSymbol] == '*') {
            sumtextview.setText((int) a + " * " + (int) b);
            locationOfCorrectAnswer = rand.nextInt(4);
            double res = a * b;
            generateAnswers(res);
            corrections.put((int) a + " * " + (int) b, String.valueOf((int) res));
        } else {
            if (b != 0) {
                sumtextview.setText((int) a + " / " + (int) b);
                locationOfCorrectAnswer = rand.nextInt(4);
                double res = a / b;
                generateAnswers(res);
                corrections.put((int) a + " / " + (int) b, String.valueOf(res));
            } else {
                generateQuestion();
                return;
            }
        }

        button0.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));
    }


    private void generateAnswers(double correctAnswer) {
        answers.clear();
        int incorrectAnswer;

        for (int i = 0; i < 4; i++) {
            if (i == locationOfCorrectAnswer) {
                answers.add(Integer.parseInt(String.valueOf(correctAnswer)));
            } else {
                incorrectAnswer = rand.nextInt(41);
                while (incorrectAnswer == correctAnswer) {
                    incorrectAnswer = rand.nextInt(41);
                }
                answers.add(incorrectAnswer);
            }
        }
    }


    @SuppressLint("SetTextI18n")
    public void chooseanswer(View view) {
        if (view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))) {
            score++;
            resulttextview.setText("Correct!");
        } else {
            resulttextview.setText("Wrong!");
            incorrectCorrections.put(sumtextview.getText().toString(), corrections.get(sumtextview.getText().toString()));
        }
        numberOfQuestions++;
        pointstextview.setText(score + "/" + numberOfQuestions);
        generateQuestion();
    }

    public void start(View view) {
        startButton.setVisibility(View.INVISIBLE);
        gameRelativeLayout.setVisibility(View.VISIBLE);
        playAgain(mainBinding.playAgainbutton);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        startButton = mainBinding.startbutton;
        sumtextview = mainBinding.sumtextview;
        button0 = mainBinding.button0;
        button1 = mainBinding.button1;
        button2 = mainBinding.button2;
        button3 = mainBinding.button3;
        resulttextview = mainBinding.resulttextView;
        pointstextview = mainBinding.pointstextView;
        timertextview = mainBinding.timertextView;
        playAgain = mainBinding.playAgainbutton;
        gameRelativeLayout = mainBinding.gameRelativeLayout;
        generateQuestion();
        gameRelativeLayout.setVisibility(View.INVISIBLE);

    }

}