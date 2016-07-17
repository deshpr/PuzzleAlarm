package apps.rahul.puzzlealarm;


import  java.util.List;
import android.graphics.Bitmap;
import android.util.Log;
import java.util.ArrayList;
import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by Rahul on 7/15/2016.
 */
public class PicturePuzzle {

    public Block[][] blocks = null;
    private Bitmap  puzzleBitmap;
    private int dimension;
    private int width;
    private int height;
    private int fps;

    private Bitmap scaledBitmap(Bitmap  bitmap, int newWidth, int newHeight)
    {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
    }


    private void initBlocks(int dimension){
        this.dimension = dimension;
        this.blocks = new  Block[dimension][dimension];
        int blockWidth = this.width/dimension;
        int blockHeight = this.height/dimension;
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                Block b =  new Block(this);
                b.bitmapImage = Bitmap.createBitmap(this.puzzleBitmap,i * blockWidth ,
                            j * blockHeight , blockWidth - 1, blockHeight - 1);
                b.blockWidth = blockWidth;
                b.blockHeight = blockHeight;
                b.x = i;
                b.y = j;
                b.directionOfMotion = Block.Direction.None;
                b.maxX = this.width;
                b.maxY = this.height;
                b.minX = 0;
                b.minY = 0;
                b.fps = fps;
                b.xCoordinate = blockWidth * i;
                b.yCoordinate = blockHeight * j;
                this.blocks[i][j] = b;
            }
        }
        this.blocks[dimension - 1][dimension - 1].isBlank = true;
    }

    private int moveToEmptyX = 2;
    private int moveToEmptyY = 2;
    private int currentMovingX;
    private int currentMovingY;


    private Block.Direction determineIfWhetherToMove(int x, int y)
    {
        if (this.blocks[x][y].isBlank)
            return Block.Direction.None;
        if(moveToEmptyY == y +1){
            return Block.Direction.Down;
        }
        else if(moveToEmptyY == y - 1){
            return Block.Direction.Up;
        }else if(moveToEmptyX == x + 1){
            return Block.Direction.Right;
        }else if(moveToEmptyX == x - 1)
        {
         return  Block.Direction.Left;
        }
        return Block.Direction.None;
    }


    private boolean transitionState = false;

    public void handleTouchInput(MotionEvent event, int motionType)
    {
        if(transitionState)
            return;

        float xCoordinate = event.getX();
        float yCoordinate = event.getY();
        int x = (int)xCoordinate/(this.width/dimension);
        int y =(int) yCoordinate/(this.height/dimension);
        Log.d(PuzzleActivity.TAG, "denomX = " +  this.width/dimension
                 + " and denomY = " + this.height/dimension);

        Log.d(PuzzleActivity.TAG, "x  " + x  + " and y = " + y);
        Log.d(PuzzleActivity.TAG, "Empt.y Space: (" + moveToEmptyX + ", " + moveToEmptyY + ")");
        this.blocks[x][y].isMoving = true;
        currentMovingX = x;
        currentMovingY = y;
        Block.Direction toMoveIn = determineIfWhetherToMove(x, y);
        if(toMoveIn != Block.Direction.None)
            transitionState = true;
        this.blocks[x][y].directionOfMotion = toMoveIn;

    }


    public void swapBlocks(){
        Log.d(PuzzleActivity.TAG, "Swap blocks");
        Log.d(PuzzleActivity.TAG, "Current = (" + currentMovingX + "," + currentMovingY + ") to" +
                " (" + moveToEmptyX + "," + moveToEmptyY + ")");
        Block b = this.blocks[currentMovingX][currentMovingY];
        this.blocks[currentMovingX][currentMovingY]  = this.blocks[moveToEmptyX][moveToEmptyY];
        this.blocks[moveToEmptyX][moveToEmptyY] = b;
        // Implement XOR operation swapping
        // Swap Xs so moveToEmpty refers to the latest one.
        currentMovingX = currentMovingX ^ moveToEmptyX;
        moveToEmptyX  = currentMovingX ^ moveToEmptyX;
        currentMovingX =  currentMovingX ^ moveToEmptyX;

        currentMovingY = currentMovingY ^ moveToEmptyY;
        moveToEmptyY  = currentMovingY ^ moveToEmptyY;
        currentMovingY =  currentMovingY ^ moveToEmptyY;

        Log.d(PuzzleActivity.TAG, "After swapping, Current = (" + currentMovingX + "," + currentMovingY + ") to" +
                " (" + moveToEmptyX + "," + moveToEmptyY + ")");
        transitionState = false;
    }

    public PicturePuzzle(Bitmap bitmapToShow, int width, int height, int dimension, int fps)
    {
        Log.d(PuzzleActivity.TAG, "Init model");
        Log.d(PuzzleActivity.TAG, "In constructor, Height = " + height + " and width = " + width);
        this.puzzleBitmap = scaledBitmap(bitmapToShow, width, height);
        this.width = width;
        this.height = height;
        this.dimension = dimension;
        this.fps = fps;
        this.initBlocks(dimension);
    }


    public void update(){
        for(int i = 0; i < dimension; i++)
        {
            for(int j= 0; j  < dimension; j++)
            {
             //   Log.d(PuzzleActivity.TAG, "Updated block i = " + i + " and j = "+ j);
                blocks[i][j].update();
            }
        }
    }



    public boolean  isSolved(){
        return false;
    }

    public void draw(Canvas canvas){
        for(int i = 0; i < dimension; i++)
        {
            for(int j = 0; j < dimension; j++)
            {
                blocks[i][j].draw(canvas);
            }
        }
    }

}
