package com.Game;

public class LargeLocation {

    int boardx;
    int boardy;
    int board;
    int cellx;
    int celly;
    int cell;
    boolean turn;
    
    public LargeLocation(int x1, int y1, int x2, int y2, boolean turn){

        boardx = x1;
        boardy = y1;
        cellx = x2;
        celly = y2;
        this.turn = turn;

        board = boardx + (boardy * 3);
        cell = cellx + (celly * 3);
    }

    public int getBX(){
        return boardx;
    }

    public int getBY(){
        return boardy;
    }

    public int getCX(){
        return cellx;
    }

    public int getCY(){
        return celly;
    }

    public int getMoveScore(){
        int score = 0;

        if(cell % 2 != 0){
            score += -3;
        }else if(cell == 4){
            score += -5;
        }else{
            score += -4;
        }

        //also prioritizes moves on certain boards
        if(board % 2 != 0){
            score += 1;
        }else if(board == 4){
            score += -1;
        }

        if(turn){
            score *= -1;
        }

        return score;
    }
}
