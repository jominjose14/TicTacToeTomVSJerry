package com.jomin.tictactoetomvsjerry;

import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    boolean gameOn;
    int activePlayer; //0=tom, 1=jerry
    int[] gameState = {2,2,2,2,2,2,2,2,2}; //0=tom, 1=jerry, 2=untouched
    int[][] winningPositions = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    Random rand;

    LinearLayout main;
    LinearLayout intro;
    LinearLayout endMessage;
    Switch playerSwitch;
    Button button;
    GridLayout grid;
    MediaPlayer introBGM;
    MediaPlayer tileClick;
    MediaPlayer[] sounds;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //random generator setup
        rand = new Random();

        //audio setup
        introBGM = new MediaPlayer().create(this,R.raw.tjintro);
        introBGM.setLooping(true);
        introBGM.start();

        tileClick = new MediaPlayer().create(this,R.raw.tileclick);

        sounds = new MediaPlayer[13];
        sounds[0] = new MediaPlayer().create(this,R.raw.apabata);
        sounds[1] = new MediaPlayer().create(this,R.raw.jyow);
        sounds[2] = new MediaPlayer().create(this,R.raw.googoo);
        sounds[3] = new MediaPlayer().create(this,R.raw.googoo);
        sounds[4] = new MediaPlayer().create(this,R.raw.cousin);
        sounds[5] = new MediaPlayer().create(this,R.raw.cry);
        sounds[6] = new MediaPlayer().create(this,R.raw.eegaro);
        sounds[7] = new MediaPlayer().create(this,R.raw.yow);
        sounds[8] = new MediaPlayer().create(this,R.raw.noise);
        sounds[9] = new MediaPlayer().create(this,R.raw.uncle);
        sounds[10] = new MediaPlayer().create(this,R.raw.yoohoo);
        sounds[11] = new MediaPlayer().create(this,R.raw.t_laugh);
        sounds[12] = new MediaPlayer().create(this,R.raw.tom_scream);

        i = 0;

        //widget setup
        main = findViewById(R.id.main);
        intro = findViewById(R.id.intro);
        endMessage = findViewById(R.id.endMessage);
        playerSwitch = findViewById(R.id.firstPlayer);
        button = findViewById(R.id.button);
        grid = findViewById(R.id.grid);

        //initial visibility setup
        main.setVisibility(View.INVISIBLE);
        endMessage.setVisibility(View.INVISIBLE);
        intro.setVisibility(View.VISIBLE);
    }

    public void hideIntro(View view) {

        introBGM.stop();
        intro.setVisibility(View.INVISIBLE);
        main.setVisibility(View.VISIBLE);

    }

    public void startGame(View view) {

        endMessage.setVisibility(View.INVISIBLE);

        //decide first player
        if(playerSwitch.isChecked()) {
            activePlayer = 1;
        }
        else {
            activePlayer = 0;
        }
        playerSwitch.setEnabled(false);

        //change button text
        button.setText("Reset");

        //reset gameState
        for(int i=0;i<gameState.length;i++)
            gameState[i] = 2;

        //clear all image sources on ImageViews in the grid
        for(int i=0;i<grid.getChildCount();i++) {
            ImageView image = (ImageView) grid.getChildAt(i);
            image.setImageResource(0);
        }

        //start the game
        gameOn = true;

    }

    public void tileClicked(View view) {

        //play audio for tile click
        tileClick.start();

        //get tag of clicked tile
        int tag = Integer.parseInt(view.getTag().toString());

        //update tile
        if(gameOn && gameState[tag] == 2) {

            ImageView tile = (ImageView) view;
            tile.setScaleX(-1f);

            gameState[tag] = activePlayer;

            if(activePlayer==0) {
                tile.setImageResource(R.drawable.tom2);
                activePlayer = 1;
            }
            else {
                tile.setImageResource(R.drawable.jerry);
                activePlayer = 0;
            }

            tile.animate().scaleX(1f).setDuration(300);

            //check gameState
            for(int[] wp : winningPositions) {

                if(gameState[wp[0]] != 2
                    && gameState[wp[0]] == gameState[wp[1]]
                    && gameState[wp[1]] == gameState[wp[2]])
                {

                    endGame(gameState[wp[0]]);
                    return;

                }

            }

            boolean gameOver = true;
            for(int tileState : gameState)
                if(tileState == 2)
                    gameOver = false;
            if(gameOver) {
                endGame(2);
            }

        }

    }

    public void endGame(int n) {

        sounds[i].setLooping(true);
        sounds[i].start();

        gameOn = false;
        main.setVisibility(View.INVISIBLE);

        ImageView winnerImage = findViewById(R.id.winnerImage);
        TextView display = findViewById(R.id.display);

        if(n == 0) {
            display.setText("Tom wins the game!");
            winnerImage.setImageResource(R.drawable.tom2);
        }
        else if(n == 1) {
            display.setText("Jerry wins the game!");
            winnerImage.setImageResource(R.drawable.jerry);
        }
        else {
            display.setText("Its a TIE!");
            winnerImage.setImageResource(R.drawable.tie);
        }

        endMessage.setVisibility(View.VISIBLE);

    }

    public void restartGame(View view) {

        sounds[i].stop();
        i = rand.nextInt(13);

        gameOn = false;
        endMessage.setVisibility(View.INVISIBLE);

        for(int i=0;i<gameState.length;i++)
            gameState[i] = 2;

        //clear all image sources on ImageViews in the grid
        for(int i=0;i<grid.getChildCount();i++) {
            ImageView image = (ImageView) grid.getChildAt(i);
            image.setImageResource(0);
        }

        playerSwitch.setEnabled(true);
        button.setText("Start");

        endMessage.setVisibility(View.INVISIBLE);
        main.setVisibility(View.VISIBLE);

    }
}
