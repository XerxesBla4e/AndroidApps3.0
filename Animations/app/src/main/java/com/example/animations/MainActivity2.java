package com.example.animations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.animations.databinding.ActivityMain2Binding;

import java.util.Arrays;

public class MainActivity2 extends AppCompatActivity {

    ActivityMain2Binding activityMainBinding;
    //0 = yellow, 1 = red
    int activePlayer = 0;

    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};
    //2 means unplayed

    boolean gameIsActive = true;

    ImageView[] counters;

    @SuppressLint("SetTextI18n")
    public void dropIn(View view) {
        ImageView counter = (ImageView) view;
        counter.setTranslationY(-1000f);
        int tappedCounter = Integer.parseInt(counter.getTag().toString());
        int[][] winningPositions = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},//horizontal
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},//vertical
                {0, 4, 8}, {2, 4, 6}};//diagonal

        if (gameState[tappedCounter] == 2 && gameIsActive) {
            gameState[tappedCounter] = activePlayer;

            if (activePlayer == 0) {
                counter.setImageResource(R.drawable.yellowcounter);
                activePlayer = 1;
            } else {
                counter.setImageResource(R.drawable.redcounter);
                activePlayer = 0;
            }
            counter.setEnabled(false);
            counter.animate().translationYBy(1000f).rotation(300).setDuration(300);

            for (int[] winningPosition : winningPositions) {
                if (gameState[winningPosition[0]] == gameState[winningPosition[1]]
                        && gameState[winningPosition[1]] == gameState[winningPosition[2]]
                        && gameState[winningPosition[0]] != 2) {

                    //Someone has won
                    gameIsActive = false;

                    String winner = "Red";
                    if (gameState[winningPosition[0]] == 0) {
                        winner = "Yellow";
                    }

                    TextView winnerMsg = (TextView) findViewById(R.id.winnerMessage);
                    winnerMsg.setText(winner + " has won!");
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.playAgainlayout);
                    linearLayout.setVisibility(View.VISIBLE);
                } else {

                    boolean gameIsOver = true;

                    for (int counterState : gameState) {
                        if (counterState == 2) {
                            gameIsOver = false;
                            break;
                        }
                    }

                    if (gameIsOver) {
                        gameIsActive = false;

                        TextView winnerMsg = (TextView) findViewById(R.id.winnerMessage);
                        winnerMsg.setText("It's draw!!");
                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.playAgainlayout);
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMain2Binding.inflate(
                getLayoutInflater()
        );
        setContentView(activityMainBinding.getRoot());

        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        counters = new ImageView[gridLayout.getChildCount()];
        for (int i = 0; i < counters.length; i++) {
            counters[i] = (ImageView) gridLayout.getChildAt(i);
        }
    }

    public void playAgain(View view) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.playAgainlayout);
        linearLayout.setVisibility(View.GONE);

        activePlayer = 0;

        gameIsActive = true;

        //  counter.setEnabled(true);

        Arrays.fill(gameState, 2);

        for (ImageView imageView : counters) {
            imageView.setEnabled(true);
            imageView.setImageResource(0);
        }

    }
}