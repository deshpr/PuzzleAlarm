package apps.rahul.puzzlealarm;


import  java.util.List;
import android.graphics.Bitmap;
import android.util.Log;
import android.graphics.Canvas;
import android.view.MotionEvent;
import com.

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
    private int blankX;
    private int blankY;
    private PuzzleGenerator puzzleGenerator;


    private Bitmap scaledBitmap(Bitmap  bitmap, int newWidth, int newHeight)
    {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
    }


    private void swapCoordinates()
    {
        this.blocks[currentMovingY][currentMovingX].xCoordinate =
                this.blocks[currentMovingY][currentMovingX].xCoordinate ^ this.blocks[moveToEmptyY][moveToEmptyX].xCoordinate;
        this.blocks[moveToEmptyY][moveToEmptyX].xCoordinate  = this.blocks[currentMovingY][currentMovingX].xCoordinate ^ this.blocks[moveToEmptyY][moveToEmptyX].xCoordinate;
        this.blocks[currentMovingY][currentMovingX].xCoordinate =  this.blocks[currentMovingY][currentMovingX].xCoordinate ^ this.blocks[moveToEmptyY][moveToEmptyX].xCoordinate;
        this.blocks[currentMovingY][currentMovingX].yCoordinate =
                this.blocks[currentMovingY][currentMovingX].yCoordinate ^ this.blocks[moveToEmptyY][moveToEmptyX].yCoordinate;
        this.blocks[moveToEmptyY][moveToEmptyX].yCoordinate  = this.blocks[currentMovingY][currentMovingX].yCoordinate ^ this.blocks[moveToEmptyY][moveToEmptyX].yCoordinate;
        this.blocks[currentMovingY][currentMovingX].yCoordinate =  this.blocks[currentMovingY][currentMovingX].yCoordinate ^ this.blocks[moveToEmptyY][moveToEmptyX].yCoordinate;


    }


    private void modifyPicturePuzzle(List<Block.Direction> directions)
    {
        boolean flag = false;
        for(Block.Direction direction: directions)
        {
            Log.d(PuzzleActivity.TAG, "Checking for dir = " + direction);
            switch(direction)
            {
                case Left:
                            currentMovingX = moveToEmptyX  - 1;
                            currentMovingY  = moveToEmptyY;
                            swapBlocks();
                            break;
                case Right:
                            currentMovingX = moveToEmptyX+ 1;
                            currentMovingY = moveToEmptyY;
                            swapBlocks();
                            break;
                case Up:
                            currentMovingY = moveToEmptyY- 1;
                            currentMovingX = moveToEmptyX;
                            swapBlocks();
                            break;
                case Down:
                            currentMovingX = moveToEmptyX;
                            currentMovingY = moveToEmptyY + 1;
                            swapBlocks();
                            break;
                case None:  flag = true;
                            break;
                default: flag = true;
                            break;

            }
            if(!flag)
                swapCoordinates();
        }
    }



    private void initBlocks(int dimension){
        this.dimension = dimension;
        this.blocks = new  Block[dimension][dimension];
        int blockWidth = this.width/dimension;
        int blockHeight = this.height/dimension;
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                Block b =  new Block(this);
                b.bitmapImage = Bitmap.createBitmap(this.puzzleBitmap, j* blockWidth ,
                            i * blockHeight , blockWidth - 1, blockHeight - 1);
                b.blockWidth = blockWidth;
                b.blockHeight = blockHeight;
                b.x = j;
                b.y = i;
                b.directionOfMotion = Block.Direction.None;
                b.maxX = this.width;
                b.maxY = this.height;
                b.minX = 0;
                b.minY = 0;
                b.fps = fps;
                b.xCoordinate = blockWidth * j;
                b.yCoordinate = blockHeight * i;
                this.blocks[i][j] = b;
            }
        }
        this.blocks[dimension - 1][dimension - 1].isBlank = true;
    }


    private int moveToEmptyX = 2;
    private int moveToEmptyY = 2;
    private int currentMovingX;
    private int currentMovingY;


    private Block.Direction determineIfWhetherToMove(int r, int c)
    {
        if (this.blocks[r][c].isBlank)
            return Block.Direction.None;
        if(moveToEmptyY == r +1){
            return Block.Direction.Down;
        }
        else if(moveToEmptyY == r - 1){
            return Block.Direction.Up;
        }else if(moveToEmptyX == c + 1){
            return Block.Direction.Right;
        }else if(moveToEmptyX == c - 1)
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
        Log.d(PuzzleActivity.TAG, "Tapped at R  = " + y + " ad C = " + x);
        Log.d(PuzzleActivity.TAG, "Blankr = " + moveToEmptyY + " and bank c - " + moveToEmptyX);
        this.blocks[y][x].isMoving = true;
        currentMovingX = x; // for matrix reference.
        currentMovingY = y;
        Block.Direction toMoveIn = determineIfWhetherToMove(y, x); // y is the row, x is the column
        if(toMoveIn != Block.Direction.None)
            transitionState = true;
        this.blocks[y][x].directionOfMotion = toMoveIn;

    }



    public void swapBlocks(){
 //       Log.d(PuzzleActivity.TAG, "Swap blocks");
        Log.d(PuzzleActivity.TAG, "Current = r,c= (" + currentMovingY + "," + currentMovingX + ") to" +
                " (" + moveToEmptyY + "," + moveToEmptyX + ")");
        // When using as index in matrix, the x is the column number,
        // amd y is the row number.
        Block b = this.blocks[currentMovingY][currentMovingX];
        this.blocks[currentMovingY][currentMovingX]  = this.blocks[moveToEmptyY][moveToEmptyX];
        this.blocks[moveToEmptyY][moveToEmptyX] = b;
        // Implement XOR operation swapping
        // Swap Xs so moveToEmpty refers to the latest one.
        currentMovingX = currentMovingX ^ moveToEmptyX;
        moveToEmptyX  = currentMovingX ^ moveToEmptyX;
        currentMovingX =  currentMovingX ^ moveToEmptyX;
        currentMovingY = currentMovingY ^ moveToEmptyY;
        moveToEmptyY  = currentMovingY ^ moveToEmptyY;
        currentMovingY =  currentMovingY ^ moveToEmptyY;

        Log.d(PuzzleActivity.TAG, "After swapping, r,c = (" + currentMovingY + "," + currentMovingX + ") to" +
                " (" + moveToEmptyY + "," + moveToEmptyX + ")");
        transitionState = false;
    }

    public PicturePuzzle(Bitmap bitmapToShow, int width, int height, int dimension, int fps, PuzzleGenerator puzzleGenerator)
    {
        Log.d(PuzzleActivity.TAG, "Init model");
        Log.d(PuzzleActivity.TAG, "In constructor, Height = " + height + " and width = " + width);
        this.puzzleBitmap = scaledBitmap(bitmapToShow, width, height);
        this.width = width;
        this.height = height;
        this.dimension = dimension;
        this.fps = fps;
        this.initBlocks(dimension);
        this.moveToEmptyX = dimension - 1;
        this.moveToEmptyY = dimension  - 1;
        this.puzzleGenerator = puzzleGenerator;
        puzzleGenerator.generateRandomPuzzleInstance();
       // this.blocks[1][2].isBlank = true;
        this.modifyPicturePuzzle(puzzleGenerator.getSwapDirections());
    }


    private void tempSwap()
        {
            currentMovingX = 2;
            currentMovingY = 1;
            swapBlocks();
            swapCoordinates();
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
