package apps.rahul.puzzlealarm;

/**
 * Created by Rahul on 7/17/2016.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.util.Log;
import java.util.Random;
import apps.rahul.puzzlealarm.Block;
import android.content.Context;

public class PuzzleGenerator {

    private int puzzle[][];

    private List<Block.Direction> previousMoves = null;
    private List<Block.Direction> moves;
    private List<Block.Direction> directionList;
    private int movesToSolve;
    private int minMovesToSolve = 4;
    private int maxMovesToSolve = 7;
    private Integer blankX;
    private Integer blankY;


    public PuzzleGenerator(int dimension, int blankX, int blankY)
    {
        puzzle = new int[dimension][dimension];
        this.blankY = blankY;
        this.blankX = blankX;
        int k = 0;
        for(int i = 0; i < dimension; i++)
        {
            for(int j = 0; j < dimension; j++)
            {
                puzzle[i][j] = k++;
            }
        }
        initDirectionList();
    }


    public List<Block.Direction> getSwapDirections(){
        return this.moves;
    }


    private boolean  canGoThere(Block.Direction direction)
    {
        Log.d(PuzzleActivity.TAG, "In canGoThere, x = " + blankX + " and y = " + blankY);
        Log.d(PuzzleActivity.TAG, "Direction = " + direction);
        switch(direction){
            case Left:
                if(blankX ==0)
                    return false;
          //      blankX -= 1;
                break;
            case Right:
                    if(blankX== 2)
                        return false;
          //          blankX += 1;
                break;
            case Up:
                   if(blankY == 0)
                       return false;
            //        blankY -= 1;
                break;
            case Down:
                if(blankY == 2)
                    return false;
            //    blankY += 1;
                 break;
            case None:
                throw new IllegalArgumentException();
        }
            return true;
    }

    private void initDirectionList(){
        directionList = new ArrayList<Block.Direction>();
        directionList.add(Block.Direction.Down);
        directionList.add(Block.Direction.Left);
        directionList.add(Block.Direction.Right);
        directionList.add(Block.Direction.Up);
    }

    private void saveData(Context context){
//        SharedPreferences sharedPreferences = context.getSharedPreferences("app")
    }



    private Block.Direction getReverseDirection(Block.Direction direction){
        switch(direction){
            case Up: return Block.Direction.Down;
            case Down: return Block.Direction.Up;
            case Left: return Block.Direction.Right;
            case Right: return Block.Direction.Left;
        }
        return direction;
    }


    private void  modifyDirections(Block.Direction direction){
        switch(direction){
            case Up: blankY -= 1;
                Log.d(PuzzleActivity.TAG, "Move up,  y becomes= " + blankY);
                    break;
            case Down: blankY += 1;
                Log.d(PuzzleActivity.TAG, "Move down,  y becomes= " + blankY);
                break;
            case Left: blankX -= 1;
                Log.d(PuzzleActivity.TAG, "Move left,  x becomes= " + blankX);
                break;
            case Right: blankX += 1;
                Log.d(PuzzleActivity.TAG, "Move right,  x becomes= " + blankX);
                break;
        }
        Log.d(PuzzleActivity.TAG, "Directions changed to: x = " + blankX + " and y = " + blankY);

    }

    private Block.Direction getRandomDirection(Block.Direction previousDirection)
    {
        Log.d(PuzzleActivity.TAG, "In random direction");
        Block.Direction chosenDirection = null;
        Random random = new Random();

        do {
             int index = random.nextInt(directionList.size()) + 0;
            Log.d(PuzzleActivity.TAG, "Size = " + directionList.size());

            //int index = (int)Math.random()*(directionList.size()-1);
            Log.d(PuzzleActivity.TAG,  "Index = " + index );
            chosenDirection =  directionList.get(0 + index);
            Log.d(PuzzleActivity.TAG, "Chosen direction randomly = " + chosenDirection);
        }while( (chosenDirection==previousDirection) ||
                !canGoThere(chosenDirection) ||
                previousDirection==getReverseDirection(chosenDirection));
        return chosenDirection;
    }




    private void displayMoves(){

        for(Block.Direction direction: moves){
            Log.d(PuzzleActivity.TAG, "Move to is = "+ direction.toString());
        }
    }

    private void calculatePercentageDifference(){

    }



    public void generateRandomPuzzleInstance()
    {
        blankX = 2;
        blankY = 2;
        Log.d(PuzzleActivity.TAG, "Make random instance");
        moves = new ArrayList<Block.Direction>();
        Block.Direction previousDirection = null;
        movesToSolve = minMovesToSolve + (int)Math.random()*(maxMovesToSolve - minMovesToSolve + 1);
        Log.d(PuzzleActivity.TAG, "Moves to solve = " + movesToSolve);
            int movesAdded = 0;
            while(movesAdded < movesToSolve)
            {

                Block.Direction direction = getRandomDirection(previousDirection);
              //  if(previousDirection == null)
                    previousDirection = direction;
                modifyDirections(direction);
                movesAdded++;
                Log.d(PuzzleActivity.TAG, "Added  = " + direction);
                moves.add(direction);
            }

        displayMoves();
    }

}
