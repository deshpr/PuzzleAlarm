package apps.rahul.puzzlealarm;

/**
 * Created by Rahul on 7/15/2016.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.graphics.Paint;
import android.graphics.Color;

public class Block extends GameComponent{

    public Bitmap  bitmapImage;
    public int blockWidth;
    public int blockHeight;
    public int x;
    public int y;
    public boolean isBlank = false;
    public boolean isMoving = false;
    public int xCoordinate;
    public int yCoordinate;
    public Direction directionOfMotion;
    public int maxX;
    public int maxY;
    public int minX;
    public int minY;
    public int fps;
    private int incX = 0;
    private int incY = 0;

    PicturePuzzle picturePuzzle;
    public Block(PicturePuzzle puzzle){
        this.picturePuzzle = puzzle;
    }


    private void calculateAdvancements(){

        int xDistance = this.blockWidth/fps;
        int yDistance = this.blockWidth/fps;
        this.incX = this.advanceX + this.blockWidth/fps  > this.blockWidth ?
                        this.blockWidth - this.advanceX : xDistance;

        this.incY = this.advanceY + this.blockHeight/fps  > this.blockHeight ?
                this.blockHeight - this.advanceY : yDistance;

     //     Log.d(PuzzleActivity.TAG, "Advancements are x = " +  this.incX + " and y = " + this.incY);
    }

    public enum Direction {
        Left,
        Right,
        Up,
        Down,
        None;
//
//        @Override
//        public String toString(){
//            switch(this){
//                case Left: return "Left";
//                case Right: return  "Right";
//                case Up: return
//            }
//        }
    }

    private int advanceX = 0;
    private int advanceY = 0;


    private void modifyCoordinatesBasedonDirection(){
        switch(directionOfMotion){
            case Left:
                if(xCoordinate > 0){
                    xCoordinate -= incX;
                    advanceX+=incX;
                }
                break;
            case Right:
                if(xCoordinate < maxX ){
                    xCoordinate+= incX;
                    advanceX+= incX;
                }
                break;
            case Up:
                if(yCoordinate > 0){
                    yCoordinate-= incY;
                    advanceY+=incY;
                }
                break;
            case Down:
                if(yCoordinate < maxY){
                    advanceY+=incY;
                    yCoordinate+=incY;
                }
                break;
        }
    }


    private void advancePosition(){
        this.calculateAdvancements();
        this.modifyCoordinatesBasedonDirection();
    }


    private void decideWhetherToMove(){
        // Motion is one-d
        if((directionOfMotion == Direction.Left || directionOfMotion == Direction.Right)
            && advanceX >= blockWidth){
            this.isMoving = false;
            advanceX = 0;
            picturePuzzle.swapBlocks();
            this.directionOfMotion = Direction.None;
        }
        if((directionOfMotion == Direction.Up || directionOfMotion == Direction.Down)
                && advanceY >= blockHeight){
            this.isMoving = false;
            advanceY = 0;
            picturePuzzle.swapBlocks();
            this.directionOfMotion = Direction.None;
        }
    }


    @Override
    public void update(){
        this.decideWhetherToMove();
        if(this.directionOfMotion != Direction.None) {
            this.advancePosition();
        }
     }

    @Override
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        if(isBlank){
            paint.setColor(Color.WHITE);
        }
        else{
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(xCoordinate, yCoordinate,
                    xCoordinate + blockWidth + 1, yCoordinate + blockHeight + 1, paint);
            canvas.drawBitmap(this.bitmapImage, xCoordinate + 1, yCoordinate, null);

//            canvas.drawBitmap(this.bitmapImage, x * blockWidth + 1, y * blockHeight+1, null);
        }
    }

}
