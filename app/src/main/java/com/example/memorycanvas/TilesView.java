package com.example.memorycanvas;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;


import androidx.annotation.Nullable;

import java.util.ArrayList;

import static androidx.core.app.NavUtils.navigateUpTo;

class Card {
    Paint p = new Paint();

    public Card(float x, float y, float width, float height, int color) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    int color, backColor = Color.DKGRAY;
    boolean isOpen = false; // цвет карты
    float x, y, width, height;
    public void draw(Canvas c) {
        // нарисовать карту в виде цветного прямоугольника
        if (isOpen) {
            p.setColor(color);
        } else p.setColor(backColor);
        c.drawRect(x,y, x+width, y+height, p);
    }
    public boolean flip (float touch_x, float touch_y) {
        if (touch_x >= x && touch_x <= x + width && touch_y >= y && touch_y <= y + height) {
            isOpen = ! isOpen;
            return true;
        } else return false;
    }

}



public class TilesView extends View {
    // пауза для запоминания карт
    final int PAUSE_LENGTH = 1; // в секундах
    boolean isOnPauseNow = false;

    // число открытых карт
    ArrayList<Card> openedCard = new ArrayList<>();

    ArrayList<Card> cards = new ArrayList<>();

    int width, height; // ширина и высота канвы
    private Context activity;


    public TilesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


        // 1) заполнить массив tiles случайными цветами
        // сгенерировать поле 2*n карт, при этом

        // должно быть ровно n пар карт разных цветов

        activity = (MainActivity) context;
        cards.add(new Card(10,10, 325, 200, Color.RED));
        cards.add(new Card(10+325+50, 10, 325, 200, Color.YELLOW));

        cards.add(new Card(10,10+250, 325, 200, Color.GREEN));
        cards.add(new Card(10+325+50, 10+250, 325, 200, Color.RED));

        cards.add(new Card(10,10+500, 325, 200, Color.BLUE));
        cards.add(new Card(10+325+50, 10+500, 325, 200, Color.YELLOW));

        cards.add(new Card(10,10+750, 325, 200, Color.BLUE));
        cards.add(new Card(10+325+50, 10+750, 325, 200, Color.GREEN));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();
        // 2) отрисовка плиток
        // задать цвет можно, используя кисть
        for (Card c: cards) {
            c.draw(canvas);
        }

    }





    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 3) получить координаты касания
        int x = (int) event.getX();
        int y = (int) event.getY();
        // 4) определить тип события
        if (event.getAction() == MotionEvent.ACTION_DOWN && !isOnPauseNow)
        {
            // палец коснулся экрана
            if (cards.size() == 0){
                Toast toast = Toast.makeText(activity, "Вы выиграли", Toast.LENGTH_LONG);
                toast.show();

            }
            for (Card c: cards) {

                if (openedCard.size() == 0) {
                    if (c.flip(x, y)) {
                        Log.d("mytag", "card flipped: " + openedCard);
                        openedCard.add(c);
                        invalidate();
                        return true;
                    }
                }

                if (openedCard.size() == 1) {
                    if (c.flip(x, y)) {
                        openedCard.add(c);
                        invalidate();
                        return true;
                    }
                }

                if (openedCard.size() == 2) {
                        for(int i = 0; i < openedCard.size();){
                            for(int i2 = 1; i2 < openedCard.size();){
                                if(openedCard.get(i2).color == openedCard.get(i).color ) {
                                    cards.remove(openedCard.get(i2));
                                    cards.remove(openedCard.get(i));
                                    openedCard.clear();
                                    PauseTask task = new PauseTask();
                                    task.execute(PAUSE_LENGTH);
                                    isOnPauseNow = true;
                                    invalidate();
                                    return true;
                                }


                                if(openedCard.get(i2).color != openedCard.get(i).color){
                                    invalidate();
                                    PauseTask task = new PauseTask();
                                    task.execute(PAUSE_LENGTH);
                                    isOnPauseNow = true;
                                    return true;
                                }

                        }
                    }




                    }}
                                    // 2) проверить, остались ли ещё карты
                        // иначе сообщить об окончании игры

                        // если карты открыты разного цвета - запустить задержку

                    }





         // заставляет экран перерисоваться
        return true;
    }


    public void newGame() {
        cards.clear();

        cards.add(new Card(10,10, 325, 200, Color.GREEN));
        cards.add(new Card(10+325+50, 10, 325, 200, Color.RED));

        cards.add(new Card(10,10+250, 325, 200, Color.YELLOW));
        cards.add(new Card(10+325+50, 10+250, 325, 200, Color.BLUE));

        cards.add(new Card(10,10+500, 325, 200, Color.RED));
        cards.add(new Card(10+325+50, 10+500, 325, 200, Color.BLUE));

        cards.add(new Card(10,10+750, 325, 200, Color.GREEN));
        cards.add(new Card(10+325+50, 10+750, 325, 200, Color.YELLOW));




    }




    class PauseTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            Log.d("mytag", "Pause started");
            try {
                Thread.sleep(integers[0] * 1000); // передаём число секунд ожидания
            } catch (InterruptedException e) {}
            Log.d("mytag", "Pause finished");
            return null;
        }

        // после паузы, перевернуть все карты обратно


        @Override
        protected void onPostExecute(Void aVoid) {
            for (Card c: cards) {
                if (c.isOpen) {
                    c.isOpen = false;
                }
            }
            openedCard.clear();
            isOnPauseNow = false;
            invalidate();
        }
    } }
