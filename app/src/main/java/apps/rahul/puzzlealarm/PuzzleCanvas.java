package apps.rahul.puzzlealarm;
import android.util.Log;

import android.view.SurfaceHolder;
import android.content.Context;
import android.view.SurfaceView;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.view.MotionEvent;

import java.util.List;

public  class PuzzleCanvas extends SurfaceView implements Runnable {

    private PuzzleGenerator  puzzleGenerator;
    private PicturePuzzle picturePuzzle;
    private SurfaceHolder puzzleGameSurfaceHolder;
    private Paint gameCanvasPaint;
    Thread puzzleGameThread;
    Canvas imageCanvas;
    private final int puzzleDimension = 3;
    private int canvasWidth;
    private int canvasHeight;
    private  volatile boolean _isPlaying =  true;

    private int game_FPS = 10;


    long FRAMES_SKIPPED = 5;
    long beginTime;
    long endTime;
    long sleepTime;
    int framesSkipped;
    private static final int FRAME_TIME = 17; // 1000/60 ~= 17

    private Bitmap bitmapToShuffle;


    public PuzzleCanvas(int fps, Context context){
        this(context);
        this.game_FPS = fps;

    }

     public PuzzleCanvas(Context context)
     {
        super(context);
         puzzleGameSurfaceHolder =  getHolder();
         gameCanvasPaint  = new Paint();
     }

    public void startGame(Bitmap bitmap){

        this.initialize(bitmap);
    }

    public PuzzleCanvas(Context context, AttributeSet attributeSet){
         super(context, attributeSet);
    }


    private  boolean obtainedSize(){
        canvasWidth= this.getHolder().getSurfaceFrame().width();
        canvasHeight = this.getHolder().getSurfaceFrame().height();
        return canvasWidth != 0 && canvasHeight != 0;
    }

     @Override
     public void run(){
         Log.d(PuzzleActivity.TAG, "Run");
         while(!obtainedSize());
         picturePuzzle = new PicturePuzzle(bitmapToShuffle,
                 canvasWidth,canvasHeight, puzzleDimension, game_FPS, puzzleGenerator);
         while(_isPlaying){
             beginTime = System.currentTimeMillis();
             framesSkipped = 0;

             update();
             draw();
             endTime = System.currentTimeMillis();
             sleepTime = FRAME_TIME - endTime;/*
             if(sleepTime > 0)
             {
                 try{
                     this.puzzleGameThread.sleep(sleepTime);
                 }
                 catch(InterruptedException ex)
                 {

                 }
                 // we 're good.
             }
             else{
                 while(sleepTime <0 && framesSkipped > FRAMES_SKIPPED){
                     this.update();
                     sleepTime += FRAME_TIME;
                     framesSkipped++;
                 }
             }*/
             controlLoopSpeed();
         }
     }

    private void controlLoopSpeed(){
//        try {
//            puzzleGameThread.sleep(17);  // Control the FPS!
//        }catch(InterruptedException ex)
//        {
//
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){

        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:

                Log.d(PuzzleActivity.TAG, "touched screen");
                picturePuzzle.handleTouchInput(motionEvent,MotionEvent.ACTION_DOWN);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(PuzzleActivity.TAG, "Moved screen");
                Log.d(PuzzleActivity.TAG, "Moved screen");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(PuzzleActivity.TAG, "Took finger up");
                break;
        }
        return true;
    }


    private void initialize(Bitmap bitmapToShuffle){
        Log.d(PuzzleActivity.TAG, "Initialize");
        this.bitmapToShuffle = bitmapToShuffle;
        Log.d(PuzzleActivity.TAG, "Creating the generator");
        puzzleGenerator = new PuzzleGenerator(puzzleDimension, 2, 2);
        puzzleGenerator.generateRandomPuzzleInstance();
        //  while(!obtainedSize()); // wait to get the  size.
        puzzleGameThread = new Thread(this);
        puzzleGameThread.start();
    }


    private void update(){
     //   Log.d(PuzzleActivity.TAG, "Update");
        picturePuzzle.update();
     }

    private boolean determinedSize = false;

    private void draw(){

        if(puzzleGameSurfaceHolder.getSurface().isValid())
        {
            imageCanvas = puzzleGameSurfaceHolder.lockCanvas();
            imageCanvas.drawColor(Color.argb(255,0,0,0));
//            Log.d(PuzzleActivity.TAG, "Height = " + imageCanvas.getHeight() + " and wirh = "  +
//                        imageCanvas.getWidth());
            gameCanvasPaint.setColor(Color.RED);
            picturePuzzle.draw(imageCanvas);
            imageCanvas.drawLine(10, 50, 200, 200, gameCanvasPaint);
            puzzleGameSurfaceHolder.unlockCanvasAndPost(imageCanvas);
        }

    }

 }