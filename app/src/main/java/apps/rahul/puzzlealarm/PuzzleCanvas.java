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

public  class PuzzleCanvas extends SurfaceView implements Runnable {


    private PicturePuzzle picturePuzzle;
    private SurfaceHolder puzzleGameSurfaceHolder;
    private Paint gameCanvasPaint;
    Thread puzzleGameThread;
    Canvas imageCanvas;
    private final int puzzleDimension = 3;

     private  volatile boolean _isPlaying =  true;


    private int game_FPS = 60;


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


    private Bitmap bitmapToShuffle;

     @Override
     public void run(){
         Log.d(PuzzleActivity.TAG, "Run");
         Rect parentFrame = this.getHolder().getSurfaceFrame();
         int width = this.getHolder().getSurfaceFrame().width();
         int height = this.getHolder().getSurfaceFrame().height();
         picturePuzzle = new PicturePuzzle(bitmapToShuffle,  984,1581, puzzleDimension, game_FPS);
         while(_isPlaying){
             update();
             draw();
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
        puzzleGameThread = new Thread(this);
        puzzleGameThread.start();
    }


    private void update(){
     //   Log.d(PuzzleActivity.TAG, "Update");
        picturePuzzle.update();
     }

    private boolean determinedSize = false;

    private void draw(){
       // Log.d(PuzzleActivity.TAG, "Draw");

        if(puzzleGameSurfaceHolder.getSurface().isValid())
        {
            imageCanvas = puzzleGameSurfaceHolder.lockCanvas();
            imageCanvas.drawColor(Color.argb(255,0,0,0));
//            Log.d(PuzzleActivity.TAG, "Height = " + this.getHeight() + " and wirh = "  + this.getWidth());
            gameCanvasPaint.setColor(Color.RED);
            picturePuzzle.draw(imageCanvas);
            imageCanvas.drawLine(100, 100, 200, 200, gameCanvasPaint);
            puzzleGameSurfaceHolder.unlockCanvasAndPost(imageCanvas);
        }

    }

 }