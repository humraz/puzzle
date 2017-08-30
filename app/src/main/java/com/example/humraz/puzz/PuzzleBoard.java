package com.example.humraz.puzz;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;


public class PuzzleBoard {
    private int steps;
    private PuzzleBoard previousBoard;
    private static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
    };
    private ArrayList<PuzzleTile> tiles;

    public PuzzleBoard getPreviousBoard()
    {
        return previousBoard;
    }

    PuzzleBoard(Bitmap bitmap, int parentWidth) {
        previousBoard = null;
        steps = 0;
        tiles = new ArrayList<>();
        int tileWidth = bitmap.getWidth() / NUM_TILES;
        int tileHeight = bitmap.getHeight() / NUM_TILES;
        int tileScaledWidth = parentWidth / NUM_TILES;
        int tileScaledHeight = parentWidth / NUM_TILES;
        for (int y = 0; y < NUM_TILES; y++) {


            for (int x = 0; x < NUM_TILES; x++) {
                Bitmap tileBitmap = Bitmap.createBitmap(bitmap, x * tileWidth, y * tileHeight, tileWidth, tileHeight);
                Bitmap scaledTileBitmap = Bitmap.createScaledBitmap(tileBitmap, tileScaledWidth, tileScaledHeight, false);
                PuzzleTile tile = new PuzzleTile(scaledTileBitmap, x + y * NUM_TILES);
                if (x == NUM_TILES - 1 && y == NUM_TILES - 1) {
                    tiles.add(null);

                } else {
                    tiles.add(tile);
                }
            }
        }
    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
        steps= otherBoard.steps+1;
        previousBoard =otherBoard;
    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
        steps=0;
        previousBoard =null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    public ArrayList<PuzzleBoard> neighbours() {
        ArrayList<PuzzleBoard> neighbours = new ArrayList<>();
        int emptytile = -1;
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i) == null) {
                emptytile = i;
            }
        }
        int emptyTileX = emptytile % NUM_TILES;
        int emptyTileY = emptytile / NUM_TILES;
        for (int[] delta : NEIGHBOUR_COORDS) {
            int tileX = emptyTileX + delta[0];
            int tileY = emptyTileY + delta[1];
            if (tileX >= 0 && tileX < NUM_TILES && tileY >= 0 && tileY < NUM_TILES) {
                PuzzleBoard boardcopy = new PuzzleBoard(this);
                boardcopy.swapTiles(XYtoIndex(tileX, tileY), XYtoIndex(emptyTileX, emptyTileY));
                neighbours.add(boardcopy);
            }
        }

        return neighbours;
    }

    public int dist() {
        return priority() - steps;
    }

    public int compareStates(PuzzleBoard rhs) {
        if (tiles.equals(rhs.tiles))
            return 0;
        else {
            if (this.priority() < rhs.priority()) {
                return -1;
            }
        }
        return 0;
    }


    public int priority() {
        int manhattan=0;
        for(int y =0;y<NUM_TILES;y++)
        {
            for(int x=0;x<NUM_TILES;x++){
                PuzzleTile tile= tiles.get(XYtoIndex(x,y));
                if(tile!=null)
                {
                    int tileNumber = tile.getNumber();
                    int tilex= tileNumber%NUM_TILES;
                    int tileY= tileNumber/NUM_TILES;
                    manhattan +=(Math.abs(x- tilex)+ Math.abs(y-tileY));
                }
            }
        }
        return steps+manhattan;
    }

}
