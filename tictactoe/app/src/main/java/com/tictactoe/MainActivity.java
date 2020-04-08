package com.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.tictactoe.Checkers.EGameStatus;
import com.tictactoe.Checkers.MoveChecker;
import com.tictactoe.CustomElements.CustomButton;
import com.tictactoe.Data.Constants;
import com.tictactoe.Data.Node;
import com.tictactoe.PlayerEngine.PlayerEngine;
import android.os.Vibrator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public MainActivity activity = this;
    public MoveChecker moveChecker = new MoveChecker();
    public PlayerEngine playerEngine = new PlayerEngine(this);
    public Boolean firstPlayerMove = true;
    private Point lastButtonClicked = null;

    public CustomButton[][] buttons;
    public int mapSize = 0;
    public int buttonTextSize = 80;

    public final String firstPlayerMarker = "X";
    public final String secondPlayerMarker = "O";

    public EGameStatus gameStatus = EGameStatus.NOT_RUNNING;

    // GUI.
    public PopupWindow popupWindow = null;

    public TextView textViewWinsValue = null;
    public int winsCount = 0;
    public TextView textViewDrawsValue = null;
    public int drawsCount = 0;
    public TextView textViewLosesValue = null;
    public int losesCount = 0;

    public Bundle bundle;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.bundle = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewWinsValue = (TextView) findViewById(R.id.textViewWinsValue);
        textViewDrawsValue = (TextView) findViewById(R.id.textViewDrawsValue);
        textViewLosesValue = (TextView) findViewById(R.id.textViewLosesValue);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        int loadedWinsCount = sharedPreferences.getInt("WINS", -1);
        int loadedDrawsCount = sharedPreferences.getInt("DRAWS", -1);
        int loadedLosesCount = sharedPreferences.getInt("LOSES", -1);

        if (loadedWinsCount != -1) {
            winsCount = loadedWinsCount;
        }

        if (loadedDrawsCount != -1) {
            drawsCount = loadedDrawsCount;
        }

        if (loadedLosesCount != -1) {
            losesCount = loadedLosesCount;
        }

        textViewWinsValue.setText("" + winsCount);
        textViewDrawsValue.setText("" + drawsCount);
        textViewLosesValue.setText("" + losesCount);
    }


    @Override
    protected void onStart() {
        super.onStart();
        showPopupChooseMapSize("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

         */

        return super.onOptionsItemSelected(item);
    }

    private LinearLayout createTicTacToeHorizontalLayout(int id) {
        LinearLayout layout = new LinearLayout(this);

        layout.setId(id);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));

        return layout;
    }

    private CustomButton createTicTacToeButton(int id) {
        final CustomButton btn = new CustomButton(this);
        btn.setId(id);
        btn.setTextSize(buttonTextSize);
        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        btn.setIncludeFontPadding(false);
        btn.getBackground().setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.MULTIPLY);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!setMarker(btn)) return;
                if (lastButtonClicked != null) {
                    buttons[lastButtonClicked.x][lastButtonClicked.y].getBackground().setColorFilter(ContextCompat.getColor(activity, android.R.color.white), PorterDuff.Mode.MULTIPLY);
                    lastButtonClicked = null;
                }
                EGameStatus status = moveChecker.checkGameStatus(buttons, btn);

                if (status == EGameStatus.DRAW) {
                    drawsCount++;
                    textViewDrawsValue.setText("" + drawsCount);
                    showPopupChooseMapSize("It's a DRAW!");
                    gameStatus = EGameStatus.DRAW;
                    saveDrawsCount();

                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(400);

                    return;
                }
                else if (status == EGameStatus.WIN) {
                    winsCount++;
                    textViewWinsValue.setText("" + winsCount);
                    showPopupChooseMapSize("You WIN!");
                    gameStatus = EGameStatus.WIN;
                    saveWinsCount();

                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(400);

                    return;
                }
                else {

                    int[] buttonXY = playerEngine.getOptimalButton();
                    placeAMove(buttonXY[1], buttonXY[2], Constants.PLAYER_2_ID);
                    buttons[buttonXY[1]][buttonXY[2]].setText(secondPlayerMarker);
                    buttons[buttonXY[1]][buttonXY[2]].getBackground().setColorFilter(ContextCompat.getColor(activity, android.R.color.holo_orange_light), PorterDuff.Mode.MULTIPLY);

                    status = moveChecker.checkGameStatus(buttons, buttons[buttonXY[1]][buttonXY[2]]);

                    lastButtonClicked = new Point(buttonXY[1], buttonXY[2]);

                    if (status == EGameStatus.DRAW) {
                        drawsCount++;
                        textViewDrawsValue.setText("" + drawsCount);
                        saveDrawsCount();
                        showPopupChooseMapSize("It's a DRAW!");

                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(400);

                        return;
                    }
                    else if (status == EGameStatus.WIN) {
                        losesCount++;
                        textViewLosesValue.setText("" + losesCount);
                        showPopupChooseMapSize("You LOSE!");
                        gameStatus = EGameStatus.LOSE;
                        saveLosesCount();

                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(400);

                        return;
                    }
                    firstPlayerMove = true;

                }
            }
        });

        return btn;
    }

    private Boolean setMarker(CustomButton btn) {
        if (!btn.getText().equals("")) {
            Toast.makeText(this,"This button was clicked before!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (firstPlayerMove) {
            btn.setText(firstPlayerMarker);
            placeAMove(btn.getButtonX(), btn.getButtonY(), Constants.PLAYER_1_ID);
            firstPlayerMove = false;
            return true;
        }
        else {
            Toast.makeText(this,"It's not your turn!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void placeAMove(int x, int y, int player) {
        buttons[x][y].setPlayer(player);
    }

    public List<Node> generateNoClickedButtons() {
        List<Node> nodeList = new ArrayList<>();

        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                if (buttons[x][y].getPlayer() == 0) nodeList.add(new Node(x, y, Constants.NO_PLAYER));
            }
        }

        return nodeList;
    }

    private void newGame(int newMapSize) {
        firstPlayerMove = true;

        if (newMapSize == mapSize) {
            for (int i = 0; i < mapSize; i++) {
                for (int j = 0; j < mapSize; j++) {
                    buttons[j][i].setText("");
                    buttons[j][i].setPlayer(Constants.NO_PLAYER);
                }
            }
        }
        else {
            mapSize = newMapSize;
            moveChecker.setMapSize(mapSize);

            LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);

            buttons = new CustomButton[mapSize][mapSize];
            deleteAllElements(buttonLayout);

            for (int i = 0; i < mapSize; i++) {
                LinearLayout horizontalLayout = createTicTacToeHorizontalLayout(i);

                for (int j = 0; j < mapSize; j++) {
                    CustomButton btn = createTicTacToeButton((i * mapSize) + (j+1));
                    btn.setButtonX(j);
                    btn.setButtonY(i);
                    btn.setPlayer(Constants.NO_PLAYER);
                    horizontalLayout.addView(btn);
                    buttons[j][i] = btn;
                }

                buttonLayout.addView(horizontalLayout);
            }
        }
    }

    private void deleteAllElements(LinearLayout buttonLayout ) {
        if(buttonLayout.getChildCount() > 0) buttonLayout.removeAllViews();
    }

    private void saveWinsCount() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("WINS", winsCount);
        editor.commit();
    }

    private void saveDrawsCount() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("DRAWS", drawsCount);
        editor.commit();
    }

    private void saveLosesCount() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("LOSES", losesCount);
        editor.commit();
    }

    private void showPopupChooseMapSize(String message)
    {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // Inflate the popup_layout.xml
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = layoutInflater.inflate(R.layout.layout_popup, null);

        TextView txtMessage = (TextView) layout.findViewById(R.id.textViewStatus);
        txtMessage.setText(message);

        // Creating the PopupWindow
        popupWindow = new PopupWindow(this);
        popupWindow.setContentView(layout);
        popupWindow.setWidth(width);
        popupWindow.setHeight(height);
        popupWindow.setFocusable(true);

        // prevent clickable background
        popupWindow.setBackgroundDrawable(null);

        // Getting a reference to button one and do something
        Button btn5x5 = (Button) layout.findViewById(R.id.button5x5);
        btn5x5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                buttonTextSize = 80;
                newGame(5);
                popupWindow.dismiss();
                popupWindow = null;
            }
        });

        // Getting a reference to button two and do something
        Button btn6x6 = (Button) layout.findViewById(R.id.button6x6);
        btn6x6.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                buttonTextSize = 60;
                newGame(6);
                popupWindow.dismiss();
                popupWindow = null;
            }
        });

        // Getting a reference to button two and do something
        Button btn7x7 = (Button) layout.findViewById(R.id.button7x7);
        btn7x7.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                buttonTextSize = 50;
                newGame(7);
                popupWindow.dismiss();
                popupWindow = null;
            }
        });

        findViewById(R.id.buttonLayout).post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(layout, Gravity.CENTER, 1, 1);
            }
        });
        gameStatus = EGameStatus.RUNNING;
    }

    public MoveChecker getMoveChecker() {
        return moveChecker;
    }

}
