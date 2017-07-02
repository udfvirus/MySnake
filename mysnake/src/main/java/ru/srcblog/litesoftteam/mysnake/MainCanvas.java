package ru.srcblog.litesoftteam.mysnake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by javavirys on 21.06.2017.
 */

public class MainCanvas extends View{

    public static String LOG_NAME = "MY_SNAKE";

    DataListener dListener;

    private Paint paint;

    private Rect rect;

    final int PART_COUNT = 36;

    int rectW;
    int rectH;

    Thread mThread;

    MainRunnable runnable;

    public Snake snake;

    public Heart heart;

    int score;
    int lives;

    int speed;

    public MainCanvas(Context context) {
        super(context);
        init();
    }

    public MainCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init()
    {
        Log.d(LOG_NAME,"init");

        speed = 0;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5.0f);
        paint.setColor(0xff5f8878);

        rect = new Rect();

        runnable = new MainRunnable(this);
        mThread = new Thread(runnable);
        mThread.start();
    }

    public int getRectW()
    {
        return rectW;
    }

    public int getRectH()
    {
        return rectH;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Меняем размеры всех зависящих переменных
        rectW = w / PART_COUNT;
        rectH = h / PART_COUNT;

        snake = new Snake(w,h,PART_COUNT);

        for(int i = 0; i < 5; i++)
            snake.addPart(new Part(i,0,rectW,rectH,PART_COUNT));
            //snake.addPart(new Part(i * rectW,0,rectW,rectH));

        heart = new Heart(rectW,rectH,PART_COUNT);

        Log.d(LOG_NAME,"onSizeChanged");
    }


    private int measureHeight(int measureSpec)
    {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        // Размер по умолчанию, если ограничения не были установлены.
        int result = 250;
        if (specMode == MeasureSpec.AT_MOST) {
            // Рассчитайте идеальный размер вашего
            // элемента в рамках максимальных значений.
            // Если ваш элемент заполняет все доступное
            // пространство, верните внешнюю границу.
        } else if (specMode == MeasureSpec.EXACTLY) {
            // Если ваш элемент может поместиться внутри этих границ, верните это значение.
            result = specSize;
        }
        return result;
    }

    private int measureWidth(int measureSpec)
    {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        // Размер по умолчанию, если ограничения не были установлены.
        int result = 200;
        if (specMode == MeasureSpec.AT_MOST) {
            // Рассчитайте идеальный размер вашего
            // элемента в рамках максимальных значений.
            // Если ваш элемент заполняет все доступное
            // пространство, верните внешнюю границу.
            //result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            // Если ваш элемент может поместиться внутри этих границ, верните это значение.
            result = specSize;
        }
        return result;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum


        /*int minW = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();

        int w = Math.max(minW, MeasureSpec.getSize(widthMeasureSpec));


        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minH = w  + getPaddingBottom() + getPaddingTop();
        int h = Math.min(MeasureSpec.getSize(heightMeasureSpec), minH);*/

        int w = measureWidth(widthMeasureSpec);
        int h = measureHeight(heightMeasureSpec);

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(255,100,255,255);

        /*
            Рисуем сетку
         */

        for(int i = 0; i < PART_COUNT; i++)
            for(int j = 0; j < PART_COUNT; j++){
                rect.set(i * rectW,j * rectH,rectW + (rectW * i),rectH + (rectH * j));
                canvas.drawRect(rect, paint);
            }

        /*
            Рисуем змейку
         */
        snake.draw(canvas);


        /*
            Рисуем сердечко
         */
        heart.draw(canvas);
    }

    public void stop()
    {
        runnable.stop();
    }

    public void setDataListener(DataListener listener)
    {
        dListener = listener;
    }

    public int getLives()
    {
        return lives;
    }

    public void setLives(int value)
    {
        lives = value;
    }

    public int getScore()
    {
        return score;
    }

    public int getHigh()
    {
        return speed;
    }

    public void setHigh(int speed)
    {
        this.speed = speed;
    }

}

interface DataListener
{
    void onScoresChanged(int scores);
    void onLivesChanged(int lives);
}
